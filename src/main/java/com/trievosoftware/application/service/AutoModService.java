package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.AutoMod;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing AutoMod.
 */
public interface AutoModService {

    /**
     * Save a autoMod.
     *
     * @param autoMod the entity to save
     * @return the persisted entity
     */
    AutoMod save(AutoMod autoMod);

    /**
     * Get all the autoMods.
     *
     * @return the list of entities
     */
    List<AutoMod> findAll();


    /**
     * Get the "id" autoMod.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AutoMod> findOne(Long id);

    /**
     * Delete the "id" autoMod.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get one autoMod by guildId.
     *
     * @param guildId the of the entity
     * @return the entity
     */
    Optional<AutoMod> findByGuildId(Long guildId);

    AutoMod getSettings(Guild guild);

    boolean hasSettings(Guild guild);

    MessageEmbed.Field getSettingsDisplay(Guild guild);

    void disableMaxMentions(Guild guild);

    void setResolveUrls(Guild guild, boolean value);

    void setResolveUrls(long guildId, boolean value);

    void setMaxMentions(Guild guild, int max);

    void setMaxRoleMentions(Guild guild, int max);

    void setMaxLines(Guild guild, int max);

    void setAutoRaidMode(Guild guild, int number, int time);

    void setInviteStrikes(Guild guild, int strikes);

    void setRefStrikes(Guild guild, int strikes);

    void setCopypastaStrikes(Guild guild, int strikes);

    void setEveryoneStrikes(Guild guild, int strikes);

    void setDupeSettings(Guild guild, int strikes, int deleteThresh, int strikeThresh);

    void setDehoistChar(Guild guild, char dehoistChar);

}
