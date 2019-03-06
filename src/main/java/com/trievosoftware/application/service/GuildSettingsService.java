package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.GuildSettings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GuildSettings.
 */
public interface GuildSettingsService {

    /**
     * Save a guildSettings.
     *
     * @param guildSettings the entity to save
     * @return the persisted entity
     */
    GuildSettings save(GuildSettings guildSettings);

    /**
     * Get all the guildSettings.
     *
     * @return the list of entities
     */
    List<GuildSettings> findAll();


    /**
     * Get the "id" guildSettings.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GuildSettings> findOne(Long id);

    /**
     * Delete the "id" guildSettings.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get one guildSettings by id.
     *
     * @param guild the guild of the entity
     * @return the entity
     */
    MessageEmbed.Field getSettingsDisplay(Guild guild);

    public GuildSettings getSettings(Guild guild);

    Optional<GuildSettings> findByGuildId(Long guildId);

    void setModLogChannel(Guild guild, TextChannel tc);

    void setServerLogChannel(Guild guild, TextChannel tc);

    void setMessageLogChannel(Guild guild, TextChannel tc);

    void setVoiceLogChannel(Guild guild, TextChannel tc);

    void setAvatarLogChannel(Guild guild, TextChannel tc);

    void setAvatarLogChannel(long guildId, TextChannel tc);

    void setModeratorRole(Guild guild, Role role);

    void setPrefix(Guild guild, String prefix);

    void setTimezone(Guild guild, ZoneId zone);

    void enableRaidMode(Guild guild);

    Guild.VerificationLevel disableRaidMode(Guild guild);

    boolean hasSettings(Guild guild);

}
