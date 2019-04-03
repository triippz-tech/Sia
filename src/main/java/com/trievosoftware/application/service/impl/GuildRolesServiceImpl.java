package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.service.GuildRolesService;
import com.trievosoftware.application.domain.GuildRoles;
import com.trievosoftware.application.exceptions.CustomCommandException;
import com.trievosoftware.application.repository.GuildRolesRepository;
import com.trievosoftware.application.service.GuildRolesService;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing GuildRoles.
 */
@Service
@Transactional
public class GuildRolesServiceImpl implements GuildRolesService {

    private final Logger log = LoggerFactory.getLogger(GuildRolesServiceImpl.class);

    private final GuildRolesRepository guildRolesRepository;

    public GuildRolesServiceImpl(GuildRolesRepository guildRolesRepository) {
        this.guildRolesRepository = guildRolesRepository;
    }

    /**
     * Save a guildRoles.
     *
     * @param guildRoles the entity to save
     * @return the persisted entity
     */
    @Override
    public GuildRoles save(GuildRoles guildRoles) {
        log.debug("Request to save GuildRoles : {}", guildRoles);
        return guildRolesRepository.save(guildRoles);
    }

    /**
     * Get all the guildRoles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GuildRoles> findAll() {
        log.debug("Request to get all GuildRoles");
        return guildRolesRepository.findAll();
    }


    /**
     * Get one guildRoles by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GuildRoles> findOne(Long id) {
        log.debug("Request to get GuildRoles : {}", id);
        return guildRolesRepository.findById(id);
    }

    /**
     * Delete the guildRoles by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GuildRoles : {}", id);
        guildRolesRepository.deleteById(id);
    }

    @Override
    public Boolean roleExists(Long guildId, Long roleId)
    {
        log.debug("Request to see if role exists");
        return guildRolesRepository.findByGuildIdAndRoleId(guildId, roleId).isPresent();
    }

    @Override
    public Boolean roleExistsForCommand(Long guildId, Long roleId, CustomCommand command)
    {
        log.debug("Request to see if role exists for Command={}", command.getCommandName());
        return guildRolesRepository.findByGuildIdAndRoleIdAndCustomcommand(guildId, roleId, command).isPresent();
    }

    @Override
    public void addRole(Role role)
    {
        log.debug("Request to add Role={} to Guild={}", role.getIdLong(), role.getGuild().getName());

        if ( roleExists(role.getGuild().getIdLong(), role.getIdLong()) ) return;

        GuildRoles guildRoles = new GuildRoles(role.getGuild().getIdLong(), role.getIdLong(), role.getName());
        save(guildRoles);
    }

    @Override
    public GuildRoles addNewRole(Role role)
    {
        log.debug("Request to add Role={} to Guild={}", role.getIdLong(), role.getGuild().getName());

        GuildRoles guildRoles = new GuildRoles(role.getGuild().getIdLong(), role.getIdLong(), role.getName());
        save(guildRoles);
        return guildRoles;
    }

    @Override
    public GuildRoles addNewCommandRole(Role role, CustomCommand customCommand)
    {
        log.debug("Request to add Role={} to Guild={}", role.getIdLong(), role.getGuild().getName());

        GuildRoles guildRoles = new GuildRoles(role.getGuild().getIdLong(), role.getIdLong(), role.getName());
        guildRoles.setCustomcommand(customCommand);
        save(guildRoles);
        return guildRoles;
    }

    @Override
    public void removeRole(Role role)
    {
        log.debug("Request to remove Role={} from Guild={}", role.getName(), role.getGuild().getName());

        Optional<GuildRoles> guildRole = guildRolesRepository.findByGuildIdAndRoleId(role.getGuild().getIdLong(), role.getIdLong());
        if ( !guildRole.isPresent() ) return;

        delete(guildRole.get().getId());
    }

    @Override
    public void updateRole(Role role)
    {
        log.debug("Request to update Role={} for Guild={}", role.getName(), role.getGuild().getName());
        Optional<GuildRoles> guildRole = guildRolesRepository.findByGuildIdAndRoleId(role.getGuild().getIdLong(), role.getIdLong());
        if ( !guildRole.isPresent() ) return;

        guildRole.get().setRoleName(role.getName());
        save(guildRole.get());
    }

    @Override
    public List<GuildRoles> getGuildRoles(Guild guild)
    {
        log.debug("Request to get Roles for Guild={}", guild.getName());
        return guildRolesRepository.findAllByGuildId(guild.getIdLong());
    }

    @Override
    public void addAllRoles(List<Role> roles)
    {
        log.debug("Request to add all roles for guild");
        for ( Role role: roles )
        {
            addRole(role);
        }
    }

    @Override
    public List<GuildRoles> roleToGuildRoles(Guild guild, List<Role> roles)
    {
        log.debug("Request to match Discord Roles to GuildRoles");

        List<GuildRoles> guildRoles = new ArrayList<>();

        for ( Role role : roles )
        {
            Optional<GuildRoles> guildRole = guildRolesRepository.findByGuildIdAndRoleId(guild.getIdLong(), role.getIdLong());
            guildRole.ifPresent(guildRoles::add);
        }

        return guildRoles;
    }

    @Override
    public GuildRoles convertRole(Guild guild, Role role)
    {
        log.debug("Request to match Discord Role={} to GuildRoles", role.getName());

        Optional<GuildRoles> guildRoles = guildRolesRepository.findByGuildIdAndRoleId(guild.getIdLong(), role.getIdLong());
        return guildRoles.orElseGet(() -> addNewRole(role));

    }

    @Override
    public GuildRoles convertCommandRole(Guild guild, Role role, CustomCommand command)
    {
        log.debug("Request to match Discord Role={} to GuildRoles", role.getName());

        Optional<GuildRoles> guildRoles =
            guildRolesRepository.findByGuildIdAndRoleIdAndCustomcommand(guild.getIdLong(), role.getIdLong(), command);
        return guildRoles.orElseGet(() -> addNewRole(role));
    }

    @Override
    public GuildRoles getCommandRole(Guild guild, Role role, CustomCommand command) throws CustomCommandException.NoRoleFoundException {
        log.debug("Request to get GuildRole for Command");
        Optional<GuildRoles> guildRoles =
            guildRolesRepository.findByGuildIdAndRoleIdAndCustomcommand(guild.getIdLong(), role.getIdLong(), command);

        if ( !guildRoles.isPresent() )
            throw new CustomCommandException.NoRoleFoundException("No Role exists");

        return guildRoles.get();
    }
}
