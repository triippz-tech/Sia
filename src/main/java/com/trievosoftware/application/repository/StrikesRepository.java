package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Strikes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Strikes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrikesRepository extends JpaRepository<Strikes, Long> {
    Optional<Strikes> findByGuildIdAndUserId(Long guildId, Long userId);
}
