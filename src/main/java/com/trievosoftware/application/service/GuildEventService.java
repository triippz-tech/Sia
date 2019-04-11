package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.GuildEvent;

import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.GuildEventException;
import com.trievosoftware.application.exceptions.InvalidTimeUnitException;
import com.trievosoftware.application.exceptions.NonTimeInputException;
import com.trievosoftware.application.exceptions.StringNotIntegerException;
import net.dv8tion.jda.core.JDA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GuildEvent.
 */
public interface GuildEventService {

    /**
     * Save a guildEvent.
     *
     * @param guildEvent the entity to save
     * @return the persisted entity
     */
    GuildEvent save(GuildEvent guildEvent);

    /**
     * Get all the guildEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GuildEvent> findAll(Pageable pageable);


    /**
     * Get the "id" guildEvent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GuildEvent> findOne(Long id);

    /**
     * Delete the "id" guildEvent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<GuildEvent> getEventsForGuild(GuildSettings settings);

    List<GuildEvent> findAllByEventStartLessThan(Instant now);

    List<GuildEvent> findAllByGuildsettingsAndEventStartLessThan(GuildSettings settings, Instant now);

    List<GuildEvent> findAllByGuildSettingsAndExpired(GuildSettings guildSettings, boolean isExpired);

//    GuildEvent getGuildEvent(GuildSettings settings, String eventName) throws GuildEventException.NoGuildEventFound;

    void createGuildEvent(GuildEvent guildEvent);

    GuildEvent getGuildEvent(Long id) throws GuildEventException.NoGuildEventFound;

    GuildEvent generateGuildEvent(GuildSettings guildSettings, String eventName, String eventMessage,
                                  String eventImageUrl, String eventStartStr)
        throws InvalidTimeUnitException, StringNotIntegerException, NonTimeInputException,
                GuildEventException.IncorrectGuildEventParamsException;

    void checkExpiredGuildEvents(JDA jda);

    void cleanExpiredGuildEvents();
}
