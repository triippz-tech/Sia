package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.DiscordGuild;

import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.Pair;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DiscordGuild.
 */
public interface DiscordGuildService {

    /**
     * Save a discordGuild.
     *
     * @param discordGuild the entity to save
     * @return the persisted entity
     */
    DiscordGuild save(DiscordGuild discordGuild);

    /**
     * Get all the discordGuilds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DiscordGuild> findAll(Pageable pageable);


    /**
     * Get the "id" discordGuild.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DiscordGuild> findOne(Long id);

    /**
     * Delete the "id" discordGuild.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Pair<Boolean, DiscordGuild> discordGuildExists(Guild guild);

    DiscordGuild getDiscordGuild(Guild guild);

    void updateDiscordGuilds(Sia jda);
}
