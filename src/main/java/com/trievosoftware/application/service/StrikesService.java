package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.Strikes;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Strikes.
 */
public interface StrikesService {

    /**
     * Save a strikes.
     *
     * @param strikes the entity to save
     * @return the persisted entity
     */
    Strikes save(Strikes strikes);

    /**
     * Get all the strikes.
     *
     * @return the list of entities
     */
    List<Strikes> findAll();


    /**
     * Get the "id" strikes.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Strikes> findOne(Long id);

    /**
     * Delete the "id" strikes.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Optional<Strikes> findAllByGuildIdAndUserId(Long guildId, Long userId);

    int[] addStrikes(Guild guild, long targetId, int strikes);

    int[] removeStrikes(Member target, int strikes);

    int[] removeStrikes(Guild guild, long targetId, int strikes);

    int getStrikes(Member target);

    int getStrikes(Guild guild, long targetId);
}
