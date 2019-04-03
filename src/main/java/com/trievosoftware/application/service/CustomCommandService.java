package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.domain.GuildRoles;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.CustomCommandException;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing CustomCommand.
 */
public interface CustomCommandService {

    /**
     * Save a customCommand.
     *
     * @param customCommand the entity to save
     * @return the persisted entity
     */
    CustomCommand save(CustomCommand customCommand);

    /**
     * Get all the customCommands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomCommand> findAll(Pageable pageable);


    /**
     * Get the "id" customCommand.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CustomCommand> findOne(Long id);

    /**
     * Delete the "id" customCommand.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<CustomCommand> findAllGuildCommands(Guild guild);

    Boolean commandExists(Guild guild, String commandName);

    CustomCommand getCommand(Guild guild, String commandName) throws CustomCommandException.NoCommandExistsException;

    CustomCommand addNewCommand(Sia sia, Guild guild, String commandName, List<Role> roles, String message, GuildSettings guildsettings)
        throws CustomCommandException.CommandExistsException, CustomCommandException.CommandInvalidParamException;

    void removeCommand(Guild guild, String commandName) throws CustomCommandException.NoCommandExistsException;

    void addRoles(Guild guild, String commandName, GuildRoles... guildRoles) throws CustomCommandException.NoCommandExistsException;

    void removeRoles(Guild guild, String commandName, GuildRoles... guildRoles) throws CustomCommandException.NoCommandExistsException;

    CustomCommand changeCommandName(Guild guild, String oldCommandName, String newCommandName)
        throws CustomCommandException.NoCommandExistsException;

    CustomCommand addCommandRoles(Sia sia, Guild guild, String commandName, List<Role> roles) throws CustomCommandException.NoCommandExistsException;

    CustomCommand removeCommandRoles(Sia sia, Guild guild, String commandName, List<Role> roles)
        throws CustomCommandException.NoCommandExistsException;

    CustomCommand changeCommandMessage(Guild guild, String commandName, String message)
        throws CustomCommandException.CommandInvalidParamException, CustomCommandException.NoCommandExistsException;

    Message checkCustomCommand(Sia sia, List<Role> role, Guild guild, GuildSettings settings, Message m)
        throws CustomCommandException.NoCommandExistsException, CustomCommandException.NoPrefixSetException, CustomCommandException.InvalidRolePermissionException;
}
