package com.trievosoftware.application.service;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.domain.WelcomeMessage;
import com.trievosoftware.application.exceptions.IncorrectWelcomeMessageParamsException;
import com.trievosoftware.application.exceptions.NoActiveWelcomeMessage;
import com.trievosoftware.application.exceptions.NoWelcomeMessageFound;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing WelcomeMessage.
 */
public interface WelcomeMessageService {

    /**
     * Save a welcomeMessage.
     *
     * @param welcomeMessage the entity to save
     * @return the persisted entity
     */
    WelcomeMessage save(WelcomeMessage welcomeMessage);

    /**
     * Get all the welcomeMessages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WelcomeMessage> findAll(Pageable pageable);


    /**
     * Get the "id" welcomeMessage.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WelcomeMessage> findOne(Long id);

    /**
     * Delete the "id" welcomeMessage.
     *
     * @param id the id of the entity
     */
    void delete(Long id);


    List<WelcomeMessage> findAllByGuildsettings(GuildSettings guildSettings);

    Optional<WelcomeMessage> findByGuildsettingsAndActive(GuildSettings guildSettings);

    Optional<WelcomeMessage> findByGuildsettingsAndName(GuildSettings guildSettings, String name);

    Boolean welcomeMessageExists(GuildSettings guildSettings, String name);

    Boolean hasActiveMessage(GuildSettings guildSettings);

    Boolean hasWelcomeMessage(GuildSettings guildSettings);

    WelcomeMessage getActiveWelcome(GuildSettings guildSettings) throws NoActiveWelcomeMessage;

    void createWelcomeMessage(WelcomeMessage welcomeMessage);

    void removeWelcomeMessage(WelcomeMessage welcomeMessage);

    void setActive(WelcomeMessage welcomeMessage);

    @SuppressWarnings("Duplicates")
    WelcomeMessage generateWelcomeMessage(CommandEvent event,Sia sia, String name, String title, String body,
                                          String footer, String url, String logo, String activeStr,
                                          GuildSettings guildSettings) throws IncorrectWelcomeMessageParamsException;

    @SuppressWarnings("Duplicates")
    WelcomeMessage modifyWelcomeMessage(CommandEvent event, Sia sia, String name, String title, String body,
                                        String footer, String url, String logo, String activeStr,
                                        GuildSettings guildSettings)
        throws IncorrectWelcomeMessageParamsException, NoActiveWelcomeMessage;

    Message displayActiveWelcomeMessage(GuildMemberJoinEvent event, GuildSettings settings) throws NoWelcomeMessageFound;
}
