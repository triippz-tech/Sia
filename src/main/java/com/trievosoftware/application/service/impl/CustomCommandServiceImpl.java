package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.service.CustomCommandService;
import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.domain.GuildRoles;
import com.trievosoftware.application.exceptions.CustomCommandException;
import com.trievosoftware.application.repository.CustomCommandRepository;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing CustomCommand.
 */
@Service
@Transactional
public class CustomCommandServiceImpl implements CustomCommandService {

    private final Logger log = LoggerFactory.getLogger(CustomCommandServiceImpl.class);

    private final CustomCommandRepository customCommandRepository;

    public CustomCommandServiceImpl(CustomCommandRepository customCommandRepository) {
        this.customCommandRepository = customCommandRepository;
    }

    /**
     * Save a customCommand.
     *
     * @param customCommand the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomCommand save(CustomCommand customCommand) {
        log.debug("Request to save CustomCommand : {}", customCommand);
        return customCommandRepository.save(customCommand);
    }

    /**
     * Get all the customCommands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomCommand> findAll(Pageable pageable) {
        log.debug("Request to get all CustomCommands");
        return customCommandRepository.findAll(pageable);
    }


    /**
     * Get one customCommand by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomCommand> findOne(Long id) {
        log.debug("Request to get CustomCommand : {}", id);
        return customCommandRepository.findById(id);
    }

    /**
     * Delete the customCommand by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomCommand : {}", id);
        customCommandRepository.deleteById(id);
    }


    @Override
    public List<CustomCommand> findAllGuildCommands(Guild guild)
    {
        log.debug("Request to get all Custom Commands for Guild={}", guild.getName());
        return customCommandRepository.findAllByGuildId(guild.getIdLong());
    }

    @Override
    public Boolean commandExists(Guild guild, String commandName)
    {
        log.debug("Request to see if Command={} exists for Guild={}", commandName, guild.getName());
        return customCommandRepository.findByGuildIdAndCommandName(guild.getIdLong(), commandName.trim().toUpperCase()).isPresent();
    }

    @Override
    public CustomCommand getCommand(Guild guild, String commandName) throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to get Command={} for Guild={}", guild.getName(), commandName);

        Optional<CustomCommand> command = customCommandRepository.findByGuildIdAndCommandName(guild.getIdLong(), commandName.trim().toUpperCase());

        if ( !command.isPresent() )
            throw new CustomCommandException.NoCommandExistsException("No Custom Command found for `" + commandName + "`");
        return command.get();
    }

    @Override
    public CustomCommand addNewCommand(Sia sia, Guild guild, String commandName, List<Role> roles,
                                       String message, DiscordGuild discordGuild)
        throws CustomCommandException.CommandExistsException, CustomCommandException.CommandInvalidParamException {
        log.debug("Request to create new Command={} for Guild={}", commandName, guild);

        if ( commandExists(guild, commandName) )
            throw new CustomCommandException.CommandExistsException("A command named `" + commandName + "` already exists");

        // validate length of command name and message
        if ( commandName.length() < 2 || commandName.length() > 10 )
            throw new CustomCommandException.CommandInvalidParamException("A custom command's code must be `2` " +
                "characters and no more than `10` characters long");
        if ( message.length() > 2000 )
            throw new CustomCommandException.CommandInvalidParamException("A custom command's message must not be longer" +
                " than 2000 characters. As this is the max Discord Allows. Current number of characters: `" + message.length() + "`");

        CustomCommand customCommand = new CustomCommand(guild.getIdLong(), commandName.trim().toUpperCase(), message, discordGuild);

        // add the roles
        for ( Role guildRole : roles ) {
            GuildRoles newRole = sia.getServiceManagers().guildRolesService().addNewCommandRole(guildRole, customCommand);
            customCommand.addGuildroles(newRole);
        }

        save(customCommand);
        return customCommand;
    }

    @Override
    public void removeCommand(Guild guild, String commandName) throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to remove Command={} from Guild={}", commandName, guild.getName());

        Optional<CustomCommand> command = customCommandRepository.findByGuildIdAndCommandName(guild.getIdLong(), commandName.trim().toUpperCase());
        if ( !command.isPresent() ) throw new CustomCommandException.NoCommandExistsException("No command named: `" + commandName + "` exists");

        delete(command.get().getId());
    }

    @Override
    public void addRoles(Guild guild, String commandName, GuildRoles... guildRoles) throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to add roles to Command={} for Guild={}", commandName, guild.getName());
        Optional<CustomCommand> command = customCommandRepository.findByGuildIdAndCommandName(guild.getIdLong(), commandName.trim().toUpperCase());
        if ( !command.isPresent() ) throw new CustomCommandException.NoCommandExistsException("No command named: `" + commandName + "` exists");

        for ( GuildRoles roles : guildRoles )
        {
            if ( !command.get().getGuildroles().contains(roles) )
            {
                command.get().addGuildroles(roles);
            }
        }

        save(command.get());
    }

    @Override
    public void removeRoles(Guild guild, String commandName, GuildRoles... guildRoles) throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to remove roles from Command={} for Guild={}", commandName, guild.getName());
        Optional<CustomCommand> command = customCommandRepository.findByGuildIdAndCommandName(guild.getIdLong(), commandName.trim().toUpperCase());
        if ( !command.isPresent() ) throw new CustomCommandException.NoCommandExistsException("No command named: `" + commandName + "` exists");

        for ( GuildRoles roles : guildRoles )
        {
            if ( command.get().getGuildroles().contains(roles) )
            {
                command.get().removeGuildroles(roles);
            }
        }

        save(command.get());
    }

    @Override
    public CustomCommand changeCommandName(Guild guild, String oldCommandName, String newCommandName)
        throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to change CustomCommand Name");

        CustomCommand command = getCommand(guild, oldCommandName.trim().toUpperCase());
        command.setCommandName(newCommandName.trim().toUpperCase());
        save(command);
        return command;
    }

    @Override
    public CustomCommand addCommandRoles(Sia sia, Guild guild, String commandName, List<Role> roles)
        throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to add Roles to CustomCommand");
        CustomCommand command = getCommand(guild, commandName.trim().toUpperCase());

        for ( Role role : roles )
        {
            if ( !sia.getServiceManagers().guildRolesService().roleExistsForCommand(guild.getIdLong(), role.getIdLong(), command))
            {
                GuildRoles guildRoles = sia.getServiceManagers().guildRolesService().addNewCommandRole(role, command);
                command.addGuildroles(guildRoles);
            }
        }

        save(command);
        return command;
    }

    @Override
    public CustomCommand removeCommandRoles(Sia sia, Guild guild, String commandName, List<Role> roles)
        throws CustomCommandException.NoCommandExistsException {
        log.debug("Request to remove Roles from Command");
        CustomCommand command = getCommand(guild, commandName.trim().toUpperCase());

        for ( Role role : roles )
        {
            if ( sia.getServiceManagers().guildRolesService().roleExistsForCommand(guild.getIdLong(), role.getIdLong(), command))
            {
                try {
                    GuildRoles guildRoles = sia.getServiceManagers().guildRolesService().getCommandRole(guild, role, command);
                    command.removeGuildroles(guildRoles);
                } catch (CustomCommandException.NoRoleFoundException e) {
                    log.warn("No GuildRole={} found for Command={}", role.getName(), command.getCommandName());
                }
            }
        }
        save(command);
        return command;
    }

    @Override
    public CustomCommand changeCommandMessage(Guild guild, String commandName, String message)
        throws CustomCommandException.CommandInvalidParamException, CustomCommandException.NoCommandExistsException {
        log.debug("Request to change CustomCommand message");

        // validate length of command name and message
        if ( commandName.length() < 2 || commandName.length() > 10 )
            throw new CustomCommandException.CommandInvalidParamException("A custom command's code must be `2` " +
                "characters and no more than `10` characters long");
        if ( message.length() > 2000 )
            throw new CustomCommandException.CommandInvalidParamException("A custom command's message must not be longer" +
                " than 2000 characters. As this is the max Discord Allows. Current number of characters: `" + message.length() + "`");

        CustomCommand command = getCommand(guild, commandName.trim().toUpperCase());

        command.setMessage(message);
        save(command);
        return command;
    }

    @Override
    public Message checkCustomCommand(Sia sia, List<Role> roles, Guild guild, DiscordGuild discordGuild, Message m)
        throws CustomCommandException.NoCommandExistsException, CustomCommandException.NoPrefixSetException,
        CustomCommandException.InvalidRolePermissionException
    {
        String msgPrefix = m.getContentStripped().substring(0, discordGuild.getGuildSettings().getPrefix().length() );
        if ( !msgPrefix.equalsIgnoreCase(discordGuild.getGuildSettings().getPrefix()))
            throw new CustomCommandException.NoPrefixSetException("This guild has no prefix set. A guild must set a prefix to use" +
                " custom commands. To set your prefix type: `/prefix <prefix>`");

        String commandName = m.getContentStripped().replace(msgPrefix, "").toUpperCase();
        CustomCommand command = getCommand(guild, commandName);

        for ( GuildRoles guildRole : command.getGuildroles() )
        {
            if ( guildRole.getRoleName().equalsIgnoreCase("@everyone"))
                return FormatUtil.formatCustomCommand(command);
        }
        //check if user can use command
        boolean found = false;
        for ( Role role : roles)
        {
            GuildRoles guildRoles = sia.getServiceManagers().guildRolesService().convertCommandRole(guild, role, command);
            if (command.getGuildroles().contains(guildRoles)) {
                found = true;
                break;
            }
        }

        if ( !found )
            throw new CustomCommandException.InvalidRolePermissionException("You do not have the required permissions to use this command");

        return FormatUtil.formatCustomCommand(command);
    }
}
