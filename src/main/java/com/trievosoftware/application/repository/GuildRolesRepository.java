package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.domain.GuildRoles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the GuildRoles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildRolesRepository extends JpaRepository<GuildRoles, Long> {
    List<GuildRoles> findAllByGuildId(Long guildId);
    Optional<GuildRoles> findByGuildIdAndRoleId(Long guildId, Long roleId);
    Optional<GuildRoles> findByGuildIdAndRoleIdAndCustomcommand(Long guildId, Long roleId, CustomCommand customCommand);
}
