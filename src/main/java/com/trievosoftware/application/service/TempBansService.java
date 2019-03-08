package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.TempBans;
import com.trievosoftware.application.exceptions.NoBanFoundExcetion;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing TempBans.
 */
public interface TempBansService {

    /**
     * Save a tempBans.
     *
     * @param tempBans the entity to save
     * @return the persisted entity
     */
    TempBans save(TempBans tempBans);

    /**
     * Get all the tempBans.
     *
     * @return the list of entities
     */
    List<TempBans> findAll();


    /**
     * Get the "id" tempBans.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TempBans> findOne(Long id);

    /**
     * Delete the "id" tempBans.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Optional<TempBans> findFirstByGuildIdAndUserId(Long guildId, Long userId);

    List<TempBans> findAllByFinishIsLessThan(Instant now);

    void setBan(Guild guild, long userId, Instant finish);

    void clearBan(Guild guild, long userId) throws NoBanFoundExcetion;

    int timeUntilUnban(Guild guild, long userId);

    void checkUnbans(JDA jda);
}
