package com.trievosoftware.application.service.impl;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.domain.WelcomeMessage;
import com.trievosoftware.application.exceptions.IncorrectWelcomeMessageParamsException;
import com.trievosoftware.application.exceptions.NoActiveWelcomeMessage;
import com.trievosoftware.application.exceptions.NoWelcomeMessageFound;
import com.trievosoftware.application.repository.WelcomeMessageRepository;
import com.trievosoftware.application.service.WelcomeMessageService;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.FormatUtil;
import com.trievosoftware.discord.utils.Pair;
import com.trievosoftware.discord.utils.Validate;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing WelcomeMessage.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class WelcomeMessageServiceImpl implements WelcomeMessageService {

    private final Logger log = LoggerFactory.getLogger(WelcomeMessageServiceImpl.class);

    private final WelcomeMessageRepository welcomeMessageRepository;

    public WelcomeMessageServiceImpl(WelcomeMessageRepository welcomeMessageRepository) {
        this.welcomeMessageRepository = welcomeMessageRepository;
    }

    /**
     * Save a welcomeMessage.
     *
     * @param welcomeMessage the entity to save
     * @return the persisted entity
     */
    @Override
    public WelcomeMessage save(WelcomeMessage welcomeMessage) {
        log.debug("Request to save WelcomeMessage : {}", welcomeMessage);
        return welcomeMessageRepository.save(welcomeMessage);
    }

    /**
     * Get all the welcomeMessages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WelcomeMessage> findAll(Pageable pageable) {
        log.debug("Request to get all WelcomeMessages");
        return welcomeMessageRepository.findAll(pageable);
    }


    /**
     * Get one welcomeMessage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WelcomeMessage> findOne(Long id) {
        log.debug("Request to get WelcomeMessage : {}", id);
        return welcomeMessageRepository.findById(id);
    }

    /**
     * Delete the welcomeMessage by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WelcomeMessage : {}", id);
        welcomeMessageRepository.deleteById(id);
    }

    @Override
    public List<WelcomeMessage> findAllByDiscordGuild(DiscordGuild discordGuild) {
        log.debug("Request to find all WelcomeMessages for Guild={}", discordGuild.getGuildId());
        return welcomeMessageRepository.findAllByDiscordGuild(discordGuild);
    }

    @Override
    public Optional<WelcomeMessage> findByDiscordGuildAndActive(DiscordGuild discordGuild) {
        log.debug("Request to find Active WelcomeMessage for Guild={}", discordGuild.getGuildId());
        return welcomeMessageRepository.findByDiscordGuildAndActive(discordGuild, true);
    }

    @Override
    public Optional<WelcomeMessage> findByDiscordGuildAndName(DiscordGuild discordGuild, String name) {
        log.debug("Request to find WelcomeMessage={} for Guild={}", name, discordGuild);
        return welcomeMessageRepository.findByDiscordGuildAndName(discordGuild, name);
    }

    @Override
    public Boolean welcomeMessageExists(DiscordGuild discordGuild, String name)
    {
        log.debug("Request to see if WelcomeMessage={} exists", name);
        Optional<WelcomeMessage> welcomeMessage = findByDiscordGuildAndName(discordGuild, name);
        return welcomeMessage.isPresent();
    }

    @Override
    public Boolean hasActiveMessage(DiscordGuild discordGuild) {
        log.debug("Request to see if Guild={} has Active WelcomeMessage", discordGuild.getGuildId());
        Optional<WelcomeMessage> welcomeMessage = findByDiscordGuildAndActive(discordGuild);
        return welcomeMessage.isPresent();
    }

    @Override
    public Boolean hasWelcomeMessage(DiscordGuild discordGuild) {
        log.debug("Request to see if Guild={} has WelcomeMessage", discordGuild.getGuildId());
        List<WelcomeMessage> welcomeMessages = findAllByDiscordGuild(discordGuild);
        return !welcomeMessages.isEmpty();
    }

    @Override
    public WelcomeMessage getActiveWelcome(DiscordGuild discordGuild) throws NoActiveWelcomeMessage {
        log.debug("Request to get active WelcomeMessage for Guild={}", discordGuild.getGuildId());
        Optional<WelcomeMessage> welcomeMessage = findByDiscordGuildAndActive(discordGuild);
        if ( !welcomeMessage.isPresent() )
            throw new NoActiveWelcomeMessage("This Guild has no active Welcome Message set");

        return welcomeMessage.get();
    }

    @Override
    public void createWelcomeMessage(WelcomeMessage welcomeMessage)
    {
        log.debug("Request to create new WelcomeMessage={}", welcomeMessage.getMessageTitle());

        // if this message is active, we want to make sure no others are as well
        if (welcomeMessage.isActive())
        {
            // Check if there is a current meessage marked as active
            Optional<WelcomeMessage> activeMessage = findByDiscordGuildAndActive(welcomeMessage.getDiscordGuild());
            if ( activeMessage.isPresent() )
            {
                // if there is make it inactive
                activeMessage.get().setActive(false);
                save(activeMessage.get());
            }
        }
        save(welcomeMessage);
    }

    @Override
    public void removeWelcomeMessage(WelcomeMessage welcomeMessage)
    {
        log.debug("Request to delete WelcomeMessage={}", welcomeMessage.getMessageTitle());
        delete(welcomeMessage.getId());
    }

    @Override
    public void setActive(WelcomeMessage welcomeMessage)
    {
        log.debug("Request to change active welcome message");
        Optional<WelcomeMessage> activeMessage = findByDiscordGuildAndActive(welcomeMessage.getDiscordGuild());

        // Set the old message to inactive
        if ( activeMessage.isPresent() )
        {
            activeMessage.get().setActive(false);
            save(activeMessage.get());
        }

        // Set the new welcome message to active
        welcomeMessage.setActive(true);
        save(welcomeMessage);
    }

    @Override
    public WelcomeMessage generateWelcomeMessage(CommandEvent event, String name, String title, String body,
                                                 String footer, String url, String logo, String activeStr,
                                                 DiscordGuild discordGuild)
        throws IncorrectWelcomeMessageParamsException
    {
        log.debug("Building welcome message for Guild={}", event.getGuild().getName());

        WelcomeMessage.WelcomeMessageBuilder builder = new WelcomeMessage.WelcomeMessageBuilder();
        if ( welcomeMessageExists(discordGuild, name) )
            throw new IncorrectWelcomeMessageParamsException("A Welcome Message with that name already exists");
        if ( body.length() > 2000 )
            throw new IncorrectWelcomeMessageParamsException("The body of a Welcome Message must not be longer than " +
                "2000 characters. Current Length `" + body.length() + "`");
        if ( !url.equals("")  ) {
            if (url.length() > 255)
                throw new IncorrectWelcomeMessageParamsException("Your website url is long. " +
                    "Please shorten the URL using a service like https://bitly.com/");
            Pair<Boolean, String> result = Validate.validateUrl(url);
            if (!result.getKey())
                throw new IncorrectWelcomeMessageParamsException(result.getValue());
        }

        if ( !logo.equals("")) {
            if (logo.length() > 255)
                throw new IncorrectWelcomeMessageParamsException("Your logo url is long. " +
                    "Please shorten the URL using a service like https://bitly.com/");
            // validate the url
            Pair<Boolean, String> result = Validate.validateUrl(logo);
            if ( !result.getKey() )
                throw new IncorrectWelcomeMessageParamsException(result.getValue());
        }


        Boolean active = parseBoolean(activeStr);

        builder.name(name);
        builder.messageTitle(title);
        builder.body(body);
        builder.footer(footer);
        builder.websiteUrl(url);
        builder.logoUrl(logo);
        builder.isActive(active);
        builder.discordGuild(discordGuild);

        return builder.build();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public WelcomeMessage modifyWelcomeMessage(CommandEvent event, String name, String title, String body,
                                               String footer, String url, String logo, String activeStr,
                                               DiscordGuild discordGuild)
        throws IncorrectWelcomeMessageParamsException, NoActiveWelcomeMessage
    {
        log.debug("Modifying welcome message for Guild={}", event.getGuild().getName());

        Optional<WelcomeMessage> welcomeMessage = findByDiscordGuildAndActive(discordGuild);
        if ( !welcomeMessage.isPresent() ) throw new NoActiveWelcomeMessage("Error getting greeting `" + name + "`");

        if ( body.length() > 2000 )
            throw new IncorrectWelcomeMessageParamsException("The body of a Welcome Message must not be longer than " +
                "2000 characters. Current Length `" + body.length() + "`");
        if ( !url.equals("") && ( !url.contains("https") || !url.contains("http") ) )
            throw new IncorrectWelcomeMessageParamsException("A website must contain the full path url. " +
                "Good: https://trievosoftware.com | Bad: www.trievosoftware.com");
        if ( !url.equals("") )
            if ( url.length() > 255 )
                throw new IncorrectWelcomeMessageParamsException("Your website url is long. "+
                    "Please shorten the URL using a service like https://bitly.com/");
        if ( !logo.equals(""))
            if ( logo.length() > 255 )
                throw new IncorrectWelcomeMessageParamsException("Your logo url is long. "+
                    "Please shorten the URL using a service like https://bitly.com/");


        Boolean active = parseBoolean(activeStr);
        welcomeMessage.get().setName(name);
        welcomeMessage.get().setMessageTitle(title);
        welcomeMessage.get().setBody(body);
        welcomeMessage.get().setFooter(footer);
        welcomeMessage.get().setWebsiteUrl(url);
        welcomeMessage.get().setLogoUrl(logo);
        welcomeMessage.get().setActive(active);

        return welcomeMessage.get();
    }


    @Override
    public Message displayActiveWelcomeMessage(GuildMemberJoinEvent event, DiscordGuild discordGuild) throws NoWelcomeMessageFound {
        Optional<WelcomeMessage> welcomeMessage = findByDiscordGuildAndActive(discordGuild);
        if (!welcomeMessage.isPresent()) throw new NoWelcomeMessageFound("Guild has no welcome message");
        return FormatUtil.formatWelcomeMessage(welcomeMessage.get(), event);
    }

    private String formatMessage(Message message)
    {
        return message.getContentRaw().trim().replaceAll(" ", "_").toUpperCase();
    }

    private Boolean parseBoolean(Message message) throws IncorrectWelcomeMessageParamsException {
        String boolStr = message.getContentRaw().trim().toUpperCase();
        return parseBoolean(boolStr);
    }
    private Boolean parseBoolean(String boolStr) throws IncorrectWelcomeMessageParamsException {
        switch (boolStr)
        {
            case "Y":       return true;
            case "YES":     return true;
            case "Y/YES":   return true;
            case "N":       return false;
            case "NO":      return false;
            case "N/NO":    return false;
            default: throw new IncorrectWelcomeMessageParamsException("Answer must be either `Y/YES` or `N/NO`");
        }
    }
}
