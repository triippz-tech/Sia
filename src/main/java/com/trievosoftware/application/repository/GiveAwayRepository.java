package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.GiveAway;
import com.trievosoftware.application.domain.GuildSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the GiveAway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiveAwayRepository extends JpaRepository<GiveAway, Long>, JpaSpecificationExecutor<GiveAway> {

    @Query(value = "select distinct give_away from GiveAway give_away left join fetch give_away.discordusers",
        countQuery = "select count(distinct give_away) from GiveAway give_away")
    Page<GiveAway> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct give_away from GiveAway give_away left join fetch give_away.discordusers")
    List<GiveAway> findAllWithEagerRelationships();

    @Query("select give_away from GiveAway give_away left join fetch give_away.discordusers where give_away.id =:id")
    Optional<GiveAway> findOneWithEagerRelationships(@Param("id") Long id);

    List<GiveAway> findAllByGuildsettings(GuildSettings guildSettings);
    List<GiveAway> findAllByGuildsettingsAndExpired(GuildSettings guildSettings, Boolean isExpired);
    List<GiveAway> findAllByFinishLessThan(Instant now);
    List<GiveAway> findAllByGuildsettingsAndFinishLessThan(GuildSettings guildSettings, Instant now);
    List<GiveAway> findAllByExpired(Boolean isExpired);
    Optional<GiveAway> findByGuildsettingsAndMessageId(GuildSettings guildSettings, Long messageId);
}
