package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.GuildEvent;
import com.trievosoftware.application.domain.GuildSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the GuildEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuildEventRepository extends JpaRepository<GuildEvent, Long>, JpaSpecificationExecutor<GuildEvent> {
    List<GuildEvent> findAllByGuildsettings(GuildSettings guildSettings);
    List<GuildEvent> findAllByEventStartLessThan(Instant now);
    List<GuildEvent> findAllByGuildsettingsAndEventStartLessThan(GuildSettings guildSettings, Instant now);
    List<GuildEvent> findAllByGuildsettingsAndExpired(GuildSettings guildSettings, Boolean expired);
    List<GuildEvent> findAllByExpired(Boolean isExpired);
    Optional<GuildEvent> findByGuildsettingsAndEventName(GuildSettings guildSettings, String eventName);
}
