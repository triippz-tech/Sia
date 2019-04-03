package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.domain.GuildRoles;
import com.trievosoftware.application.exceptions.CustomCommandException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing GuildRoles.
 */
public interface GuildRolesService {

    /**
     * Save a guildRoles.
     *
     * @param guildRoles the entity to save
     * @return the persisted entity
     */
    GuildRoles save(GuildRoles guildRoles);

    /**
     * Get all the guildRoles.
     *
     * @return the list of entities
     */
    List<GuildRoles> findAll();


    /**
     * Get the "id" guildRoles.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GuildRoles> findOne(Long id);

    /**
     * Delete the "id" guildRoles.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Boolean roleExists(Long guildId, Long roleId);

    Boolean roleExistsForCommand(Long guildId, Long roleId, CustomCommand command);

    void addRole(Role role);

    GuildRoles addNewRole(Role role);

    GuildRoles addNewCommandRole(Role role, CustomCommand customCommand);

    void removeRole(Role role);

    void updateRole(Role role);

    List<GuildRoles> getGuildRoles(Guild guild);

    void addAllRoles(List<Role> roles);

    List<GuildRoles> roleToGuildRoles(Guild guild, List<Role> roles);

    GuildRoles convertRole(Guild guild, Role role);

    GuildRoles convertCommandRole(Guild guild, Role role, CustomCommand command);

    GuildRoles getCommandRole(Guild guild, Role role, CustomCommand command) throws CustomCommandException.NoRoleFoundException;
}
