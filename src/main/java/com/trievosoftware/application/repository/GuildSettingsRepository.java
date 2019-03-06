package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.GuildSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the GuildSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildSettingsRepository extends JpaRepository<GuildSettings, Long> {
    Optional<GuildSettings> findByGuildId(Long guildId);
}
