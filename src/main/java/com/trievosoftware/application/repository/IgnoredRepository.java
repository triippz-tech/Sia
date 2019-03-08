package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Ignored;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Ignored entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IgnoredRepository extends JpaRepository<Ignored, Long> {
    Optional<Ignored> findByEntityId(Long entityId);
    List<Ignored> findByGuildIdAndType(Long guildId, Integer type);
}
