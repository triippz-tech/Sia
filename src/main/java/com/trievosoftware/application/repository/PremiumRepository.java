package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Premium;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Premium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PremiumRepository extends JpaRepository<Premium, Long> {
    Optional<Premium> findByDiscordId(Long discordId);

    List<Premium> findAllByUntilIsLessThan(Instant now);
}
