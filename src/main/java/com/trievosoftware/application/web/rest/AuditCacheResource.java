package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.AuditCache;
import com.trievosoftware.application.service.AuditCacheService;
import com.trievosoftware.application.web.rest.errors.BadRequestAlertException;
import com.trievosoftware.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AuditCache.
 */
@RestController
@RequestMapping("/api")
public class AuditCacheResource {

    private final Logger log = LoggerFactory.getLogger(AuditCacheResource.class);

    private static final String ENTITY_NAME = "auditCache";

    private final AuditCacheService auditCacheService;

    public AuditCacheResource(AuditCacheService auditCacheService) {
        this.auditCacheService = auditCacheService;
    }

    /**
     * POST  /audit-caches : Create a new auditCache.
     *
     * @param auditCache the auditCache to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auditCache, or with status 400 (Bad Request) if the auditCache has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/audit-caches")
    public ResponseEntity<AuditCache> createAuditCache(@Valid @RequestBody AuditCache auditCache) throws URISyntaxException {
        log.debug("REST request to save AuditCache : {}", auditCache);
        if (auditCache.getId() != null) {
            throw new BadRequestAlertException("A new auditCache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditCache result = auditCacheService.save(auditCache);
        return ResponseEntity.created(new URI("/api/audit-caches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audit-caches : Updates an existing auditCache.
     *
     * @param auditCache the auditCache to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auditCache,
     * or with status 400 (Bad Request) if the auditCache is not valid,
     * or with status 500 (Internal Server Error) if the auditCache couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/audit-caches")
    public ResponseEntity<AuditCache> updateAuditCache(@Valid @RequestBody AuditCache auditCache) throws URISyntaxException {
        log.debug("REST request to update AuditCache : {}", auditCache);
        if (auditCache.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditCache result = auditCacheService.save(auditCache);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, auditCache.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audit-caches : get all the auditCaches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of auditCaches in body
     */
    @GetMapping("/audit-caches")
    public List<AuditCache> getAllAuditCaches() {
        log.debug("REST request to get all AuditCaches");
        return auditCacheService.findAll();
    }

    /**
     * GET  /audit-caches/:id : get the "id" auditCache.
     *
     * @param id the id of the auditCache to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auditCache, or with status 404 (Not Found)
     */
    @GetMapping("/audit-caches/{id}")
    public ResponseEntity<AuditCache> getAuditCache(@PathVariable Long id) {
        log.debug("REST request to get AuditCache : {}", id);
        Optional<AuditCache> auditCache = auditCacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditCache);
    }

    /**
     * DELETE  /audit-caches/:id : delete the "id" auditCache.
     *
     * @param id the id of the auditCache to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/audit-caches/{id}")
    public ResponseEntity<Void> deleteAuditCache(@PathVariable Long id) {
        log.debug("REST request to delete AuditCache : {}", id);
        auditCacheService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
