package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.GuildMusicSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the GuildMusicSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildMusicSettingsRepository extends JpaRepository<GuildMusicSettings, Long> {

    Optional<GuildMusicSettings> findByGuildId(Long guildId);
}
