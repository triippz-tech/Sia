package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.domain.GiveAway;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.webhook.WebhookClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import net.dv8tion.jda.core.entities.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GiveAway.
 */
public interface GiveAwayService {

    /**
     * Save a giveAway.
     *
     * @param giveAway the entity to save
     * @return the persisted entity
     */
    GiveAway save(GiveAway giveAway);

    /**
     * Get all the giveAways.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GiveAway> findAll(Pageable pageable);

    /**
     * Get all the GiveAway with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<GiveAway> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" giveAway.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GiveAway> findOne(Long id);

    /**
     * Delete the "id" giveAway.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<GiveAway> findAllByDiscordGuildAndExpired(DiscordGuild discordGuild, Boolean isExpired);

    GiveAway getGiveAway(DiscordGuild discordGuild, Long messageId);

    GiveAway createGiveAway(String name, String message, Instant finish, DiscordGuild discordGuild);

    void enterVote(Sia sia, GiveAway giveAway, User user);

    void checkForExpiredGiveAways(JDA jda);

    void cleanExpiredGiveAways(Sia sia);
}
