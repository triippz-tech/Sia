package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.TempMutes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the TempMutes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TempMutesRepository extends JpaRepository<TempMutes, Long> {
    Optional<TempMutes> findFirstByGuildIdAndUserId(Long guildId, Long userId);
    List<TempMutes> findAllByFinishIsLessThan(Instant now);
    Optional<TempMutes> findFirstByGuildIdAndUserIdAndFinishGreaterThan(Long guildId, Long userId, Instant now);
}
