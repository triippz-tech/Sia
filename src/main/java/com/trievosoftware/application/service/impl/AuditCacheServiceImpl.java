package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.service.AuditCacheService;
import com.trievosoftware.application.domain.AuditCache;
import com.trievosoftware.application.repository.AuditCacheRepository;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing AuditCache.
 */
@Service
@Transactional
public class AuditCacheServiceImpl implements AuditCacheService {

    private final Logger log = LoggerFactory.getLogger(AuditCacheServiceImpl.class);

    private final AuditCacheRepository auditCacheRepository;

    public AuditCacheServiceImpl(AuditCacheRepository auditCacheRepository) {
        this.auditCacheRepository = auditCacheRepository;
    }

    /**
     * Save a auditCache.
     *
     * @param auditCache the entity to save
     * @return the persisted entity
     */
    @Override
    public AuditCache save(AuditCache auditCache) {
        log.debug("Request to save AuditCache : {}", auditCache);
        return auditCacheRepository.save(auditCache);
    }

    /**
     * Get all the auditCaches.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditCache> findAll() {
        log.debug("Request to get all AuditCaches");
        return auditCacheRepository.findAll();
    }


    /**
     * Get one auditCache by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AuditCache> findOne(Long id) {
        log.debug("Request to get AuditCache : {}", id);
        return auditCacheRepository.findById(id);
    }

    /**
     * Delete the auditCache by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AuditCache : {}", id);
        auditCacheRepository.deleteById(id);
    }

    /**
     * Get all the auditCaches for a guild from the guildId.
     *
     * @param guildId The id of the guilds to find
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditCache> findByGuildId(Long guildId) {
        log.debug("Request to get all AuditCaches for guild: {}", guildId);
        return auditCacheRepository.findByGuildId(guildId);
    }

    @Override
    public List<AuditLogEntry> filterUncheckedEntries(List<AuditLogEntry> list) {
        log.debug("Request to filter AuditLogEntry");
        if(list.isEmpty())
            return list;
        long gid = list.get(0).getGuild().getIdLong();

        List<AuditCache> auditCaches = findByGuildId(gid);
        LinkedList<AuditLogEntry> filtered = new LinkedList<>();

        if (!auditCaches.isEmpty()){
            for ( AuditCache cache : auditCaches ){
                long old = cache.getOld();
                long older = cache.getOlder();
                long oldest = cache.getOldest();


                for(AuditLogEntry entry: list) {
                    if (entry.getIdLong() > oldest && entry.getIdLong() != older && entry.getIdLong() != old)
                        filtered.add(0, entry);
                }

                cache.setOld(list.get(0).getIdLong());
                if(list.size()>=3)
                {
                    cache.setOlder(list.get(1).getIdLong());
                    cache.setOldest(list.get(2).getIdLong());
                }
                else if(list.size()==2)
                {
                    cache.setOlder(list.get(1).getIdLong());
                    cache.setOldest(list.get(1).getIdLong());
                }
                else
                {
                    cache.setOlder(list.get(0).getIdLong());
                    cache.setOldest(list.get(0).getIdLong());
                }

                save(cache);
            }
        } else {
            AuditCache cache = new AuditCache();
            cache.setGuildId(gid);
            cache.setOld(0L);
            cache.setOlder(0L);
            cache.setOldest(0L);
            save(cache);
        }
        return filtered;
    }
}
