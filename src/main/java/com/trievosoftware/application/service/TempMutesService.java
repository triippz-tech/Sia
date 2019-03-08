package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.TempMutes;
import com.trievosoftware.application.exceptions.UserNotMutedException;
import io.swagger.models.auth.In;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing TempMutes.
 */
public interface TempMutesService {

    /**
     * Save a tempMutes.
     *
     * @param tempMutes the entity to save
     * @return the persisted entity
     */
    TempMutes save(TempMutes tempMutes);

    /**
     * Get all the tempMutes.
     *
     * @return the list of entities
     */
    List<TempMutes> findAll();


    /**
     * Get the "id" tempMutes.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TempMutes> findOne(Long id);

    /**
     * Delete the "id" tempMutes.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Optional<TempMutes> findFirstByGuildIdAndUserId(Long guildId, Long userId);

    List<TempMutes> findAllByFinishIsLessThan(Instant now);

    Optional<TempMutes> findFirstByGuildIdAndUserIdAndFinishGreaterThan(Long guildId, Long userId, Instant now);

    boolean isMuted(Member member);

    void setMute(Guild guild, long userId, Instant finish);

    void overrideMute(Guild guild, long userId, Instant finish);

    void removeMute(Guild guild, long userId) throws UserNotMutedException;

    int timeUntilUnmute(Guild guild, long userId);

    void checkUnmutes(JDA jda, GuildSettingsService guildSettingsService);
}
