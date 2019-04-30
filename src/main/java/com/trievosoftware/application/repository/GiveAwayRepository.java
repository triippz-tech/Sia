package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.domain.GiveAway;
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

    List<GiveAway> findAllByDiscordGuild(DiscordGuild discordGuild);
    List<GiveAway> findAllByDiscordGuildAndExpired(DiscordGuild discordGuild, Boolean isExpired);
    List<GiveAway> findAllByFinishLessThan(Instant now);
    List<GiveAway> findAllByDiscordGuildAndFinishLessThan(DiscordGuild discordGuild, Instant now);
    List<GiveAway> findAllByExpired(Boolean isExpired);
    Optional<GiveAway> findByDiscordGuildAndMessageId(DiscordGuild discordGuild, Long messageId);
}
