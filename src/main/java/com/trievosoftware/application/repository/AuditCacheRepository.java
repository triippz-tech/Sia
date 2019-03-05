package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.AuditCache;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the AuditCache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditCacheRepository extends JpaRepository<AuditCache, Long> {
    List<AuditCache> findByGuildId(long guildId);
}
