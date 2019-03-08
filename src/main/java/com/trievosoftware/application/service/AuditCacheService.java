package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.AuditCache;
import net.dv8tion.jda.core.audit.AuditLogEntry;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing AuditCache.
 */
public interface AuditCacheService {

    /**
     * Save a auditCache.
     *
     * @param auditCache the entity to save
     * @return the persisted entity
     */
    AuditCache save(AuditCache auditCache);

    /**
     * Get all the auditCaches.
     *
     * @return the list of entities
     */
    List<AuditCache> findAll();


    /**
     * Get the "id" auditCache.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AuditCache> findOne(Long id);

    /**
     * Delete the "id" auditCache.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get all the auditCaches from the guildId.
     *
     * @param guildId
     * @return the list of entities
     */
    List<AuditCache> findByGuildId(Long guildId);

    List<AuditLogEntry> filterUncheckedEntries(List<AuditLogEntry> list);
}
