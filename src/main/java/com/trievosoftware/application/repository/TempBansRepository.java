package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.TempBans;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the TempBans entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TempBansRepository extends JpaRepository<TempBans, Long> {
    Optional<TempBans> findFirstByGuildIdAndUserId(Long guildId, Long userId);
    List<TempBans> findAllByFinishIsLessThan(Long epochSeconds);
}
