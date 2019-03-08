package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.Actions;
import com.trievosoftware.application.domain.Punishment;
import com.trievosoftware.application.exceptions.NoActionsExceptions;
import com.trievosoftware.discord.Action;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Actions.
 */
public interface ActionsService {

    /**
     * Save a actions.
     *
     * @param actions the entity to save
     * @return the persisted entity
     */
    Actions save(Actions actions);

    /**
     * Get all the actions.
     *
     * @return the list of entities
     */
    List<Actions> findAll();


    /**
     * Get the "id" actions.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Actions> findOne(Long id);

    /**
     * Delete the "id" actions.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<Actions> findAllByGuildId(long guildId);

    List<Actions> findAllByGuildIdAndNumStrikesIs(Long guildId, Integer numStrikes);

    boolean useDefaultSettings(Guild guild); // only activates if none set

    void removeAction(Guild guild, int numStrikes) throws NoActionsExceptions;

    void setAction(Guild guild, int numStrikes, Action action);

    void setAction(Guild guild, int numStrikes, Action action, int time);

    List<Punishment> getAllPunishments(Guild guild) throws NoActionsExceptions;

    List getPunishments(Guild guild, int from, int to);

    MessageEmbed.Field getAllPunishmentsDisplay(Guild guild);
}
