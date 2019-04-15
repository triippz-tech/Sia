package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.GuildEventException;
import com.trievosoftware.application.exceptions.InvalidTimeUnitException;
import com.trievosoftware.application.exceptions.NonTimeInputException;
import com.trievosoftware.application.exceptions.StringNotIntegerException;
import com.trievosoftware.application.service.GuildEventService;
import com.trievosoftware.application.domain.GuildEvent;
import com.trievosoftware.application.repository.GuildEventRepository;
import com.trievosoftware.discord.utils.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing GuildEvent.
 */
@Service
@Transactional
public class GuildEventServiceImpl implements GuildEventService {

    private final Logger log = LoggerFactory.getLogger(GuildEventServiceImpl.class);

    private final GuildEventRepository guildEventRepository;

    public GuildEventServiceImpl(GuildEventRepository guildEventRepository) {
        this.guildEventRepository = guildEventRepository;
    }

    /**
     * Save a guildEvent.
     *
     * @param guildEvent the entity to save
     * @return the persisted entity
     */
    @Override
    public GuildEvent save(GuildEvent guildEvent) {
        log.debug("Request to save GuildEvent : {}", guildEvent);
        return guildEventRepository.save(guildEvent);
    }

    /**
     * Get all the guildEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GuildEvent> findAll(Pageable pageable) {
        log.debug("Request to get all GuildEvents");
        return guildEventRepository.findAll(pageable);
    }


    /**
     * Get one guildEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GuildEvent> findOne(Long id) {
        log.debug("Request to get GuildEvent : {}", id);
        return guildEventRepository.findById(id);
    }

    /**
     * Delete the guildEvent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GuildEvent : {}", id);
        guildEventRepository.deleteById(id);
    }

    /**
     * Gets all Guild Events for a guild
     * @param settings a guild's settings
     * @return List
     */
    @Override
    public List<GuildEvent> getEventsForGuild(GuildSettings settings)
    {
        log.debug("Request to get all GuildEvents for Guild={}", settings.getGuildId());
        return guildEventRepository.findAllByGuildsettings(settings);
    }

    /**
     * Gets all Guild events which are expired
     *
     * @param now Current Time
     * @return List
     */
    @Override
    public List<GuildEvent> findAllByEventStartLessThan(Instant now)
    {
        log.debug("Request to find all expired GuildEvents");
        return guildEventRepository.findAllByEventStartLessThan(now);
    }

    /**
     * Gets all Expired GuildEvents for a Guild
     *
     * @param settings Settings for a Guild
     * @param now Current Time
     * @return List
     */
    @Override
    public List<GuildEvent> findAllByGuildsettingsAndEventStartLessThan(GuildSettings settings, Instant now)
    {
        log.debug("Request to find all expired GuildEvents for Guild={}", settings.getGuildId());
        return guildEventRepository.findAllByGuildsettingsAndEventStartLessThan(settings, now);
    }

    /**
     * Gets all GuildEvents for a Guild
     *
     * @param guildSettings Settings for Guild
     * @param isExpired Whether the Event has expired or not
     * @return List
     */
    @Override
    public List<GuildEvent> findAllByGuildSettingsAndExpired(GuildSettings guildSettings, boolean isExpired)
    {
        log.debug("Requeest to find all GuildEvents for Guild={}", guildSettings.getGuildId());
        return guildEventRepository.findAllByGuildsettingsAndExpired(guildSettings, isExpired);
    }

//    /**
//     * Gets a GuildEvent by it's Guild and Name
//     *
//     * @param settings Settings for Guild
//     * @param eventName
//     * @return
//     * @throws GuildEventException.NoGuildEventFound
//     */
//    @Override
//    public GuildEvent getGuildEvent(GuildSettings settings, String eventName) throws GuildEventException.NoGuildEventFound {
//        log.debug("Request to get GuildEvent={} for Guild={}", eventName, settings.getGuildId());
//
//        Optional<GuildEvent> guildEvent = guildEventRepository.findByGuildsettingsAndEventName(settings, eventName.toUpperCase());
//
//        if ( !guildEvent.isPresent() )
//            throw new GuildEventException.NoGuildEventFound("No Guild Event `" + eventName + "` was found.");
//
//        return guildEvent.get();
//    }

    /**
     * Creates a new GuildEvent
     * @param guildEvent Guild event to save
     */
    @Override
    public void createGuildEvent(GuildEvent guildEvent)
    {
        log.debug("Request to create new GuildEvent={}", guildEvent.getEventName());
        save(guildEvent);
    }

    /**
     * Gets a GuildEvent from its ID
     * @param id ID of saved event
     * @return GuildEvent
     * @throws GuildEventException.NoGuildEventFound
     */
    @Override
    public GuildEvent getGuildEvent(Long id) throws GuildEventException.NoGuildEventFound {
        log.debug("Request to get GuildEvent={}", id);
        Optional<GuildEvent> guildEvent = findOne(id);

        if ( !guildEvent.isPresent() )
            throw new GuildEventException.NoGuildEventFound("No Guild Event was found.");

        return guildEvent.get();
    }

    /**
     * Generates a Non-Persisted GuildEvent
     * @param guildSettings Settings for Guild
     * @param eventName The name (Title) of the event
     * @param eventMessage The message (Body) of the event
     * @param eventImageUrl The URL of the image to embed
     * @param eventStartStr The time which the event starts
     * @return GuildEvent
     * @throws InvalidTimeUnitException If a unit of time is not correct
     * @throws StringNotIntegerException If the timestring has no numeric values
     * @throws NonTimeInputException No Time given
     * @throws GuildEventException.IncorrectGuildEventParamsException If any parameters are incorrect
     */
    @Override
    public GuildEvent generateGuildEvent(GuildSettings guildSettings, String eventName, String eventMessage,
                                         String eventImageUrl, String eventStartStr)
        throws InvalidTimeUnitException, StringNotIntegerException, NonTimeInputException,
                GuildEventException.IncorrectGuildEventParamsException
    {
        log.debug("Request to create new GuildEvent={} for Guild={}", eventName, guildSettings);

        // format the params
        eventName = eventName.trim().replaceAll(" ", "_").toUpperCase();
        Instant eventStart = OtherUtil.getTime(eventStartStr.trim().toUpperCase());

        // Perform validations
        if ( eventName.length() > 250 )
            throw new GuildEventException.IncorrectGuildEventParamsException("The title of the event must not be longer than" +
                " 250 characters. Length of title was `" + eventName.length() + "`");
        if ( !eventImageUrl.equals("")) {
            if (eventImageUrl.length() > 250)
                throw new GuildEventException.IncorrectGuildEventParamsException("The log url for your event is long. " +
                    "Please shorten the URL using a service like https://bitly.com/");
            // validate the url
            Pair<Boolean, String> result = Validate.validateUrl(eventImageUrl);
            if ( !result.getKey() )
                throw new GuildEventException.IncorrectGuildEventParamsException(result.getValue());
        }
        if ( eventMessage.length() > 1500 )
            throw new GuildEventException.IncorrectGuildEventParamsException("The message of the event must not be longer than" +
                " 1500 characters. Length of message was `" + eventMessage.length() + "`");

        return new GuildEvent(eventName, eventImageUrl, eventMessage, eventStart, 0L, guildSettings);
    }

    /**
     * Sets the expired flag for an Event. So it will not rerun during the ScheduledThread
     *
     * @param guildEventId Id of the Event
     */
    private void setExpiredFlag(Long guildEventId)
    {
        log.debug("Request to expire GuildEvent={}", guildEventId);
        Optional<GuildEvent> guildEvent = findOne(guildEventId);

        if ( guildEvent.isPresent() )
        {
            guildEvent.get().setExpired(true);
            save(guildEvent.get());
        }
    }

    /**
     * Checks for expired GuildEvent. If one or more are found. Sia will attempt to notify users in the follwing format:
     *      1. The Guild's default channel
     *      2. The Guild's owner in a private channel, to notify the Owner to let users know
     * @param jda
     */
    @Override
    public void checkExpiredGuildEvents(JDA jda)
    {
        log.debug("Request to check for expired GuildEvent");
        List<GuildEvent> guildEvents = findAllByEventStartLessThan(Instant.now());

        for ( GuildEvent guildEvent : guildEvents )
        {
            if ( !guildEvent.isExpired() )
            {
                Guild guild = jda.getGuildById(guildEvent.getGuildsettings().getGuildId());

                // Attempt to send to chosen channel
                try {
                    guild.getTextChannelById(guildEvent.getTextChannelId())
                        .sendMessage(FormatUtil.formatGuildEvent(guild, guildEvent, true, false)).complete();
                } catch (NullPointerException e) {
                    try {
                        guild.getDefaultChannel().sendMessage(FormatUtil.formatGuildEvent(guild, guildEvent, true, false)).complete();
                    } catch (NullPointerException e1) {
                        // if No default found, send message in private channel to the owner
                        guild.getOwner().getUser().openPrivateChannel().complete().sendMessage(FormatUtil.formatGuildEvent(guild, guildEvent, true, false)).complete();
                        guild.getOwner().getUser().openPrivateChannel().complete().sendMessage("This message was sent to you here because your server does not have" +
                            " a default channel set up.").complete();
                    }
                }

                log.info("GuildEvent={} completed, message sent", guildEvent.getEventName());

                // now set the expired flag so we dont notify anyone again
                setExpiredFlag(guildEvent.getId());
            }
        }
    }

    /**
     * Checks for expired GuildEvents and removes them from the database.
     * To ensure we do not bloat the repository with data no longer needed.
     */
    @Override
    public void cleanExpiredGuildEvents()
    {
        log.debug("Request to clean expired GuildEvents");
        Instant now = Instant.now();

        List<GuildEvent> guildEvents = guildEventRepository.findAllByExpired(true);

        int removed = 0;
        for ( GuildEvent event : guildEvents )
        {
            Duration difference = Duration.between( event.getEventStart(), now);
            if ( difference.toDays() > 14 ) { // clean up all expired events after 2 weeks
                delete(event.getId());
                removed++;
            }
        }

        if ( removed > 0 )
            log.info("{} expired GuildEvents were cleaneed", removed);
    }
}
