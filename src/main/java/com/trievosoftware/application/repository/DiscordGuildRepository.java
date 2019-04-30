package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.DiscordGuild;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the DiscordGuild entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscordGuildRepository extends JpaRepository<DiscordGuild, Long>, JpaSpecificationExecutor<DiscordGuild> {
    Optional<DiscordGuild> findByGuildId(Long guildId);
}
