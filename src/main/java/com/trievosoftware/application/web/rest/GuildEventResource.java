package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.GuildEvent;
import com.trievosoftware.application.service.GuildEventService;
import com.trievosoftware.application.web.rest.errors.BadRequestAlertException;
import com.trievosoftware.application.web.rest.util.HeaderUtil;
import com.trievosoftware.application.web.rest.util.PaginationUtil;
import com.trievosoftware.application.service.dto.GuildEventCriteria;
import com.trievosoftware.application.service.GuildEventQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GuildEvent.
 */
@RestController
@RequestMapping("/api")
public class GuildEventResource {

    private final Logger log = LoggerFactory.getLogger(GuildEventResource.class);

    private static final String ENTITY_NAME = "guildEvent";

    private final GuildEventService guildEventService;

    private final GuildEventQueryService guildEventQueryService;

    public GuildEventResource(GuildEventService guildEventService, GuildEventQueryService guildEventQueryService) {
        this.guildEventService = guildEventService;
        this.guildEventQueryService = guildEventQueryService;
    }

    /**
     * POST  /guild-events : Create a new guildEvent.
     *
     * @param guildEvent the guildEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new guildEvent, or with status 400 (Bad Request) if the guildEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/guild-events")
    public ResponseEntity<GuildEvent> createGuildEvent(@Valid @RequestBody GuildEvent guildEvent) throws URISyntaxException {
        log.debug("REST request to save GuildEvent : {}", guildEvent);
        if (guildEvent.getId() != null) {
            throw new BadRequestAlertException("A new guildEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildEvent result = guildEventService.save(guildEvent);
        return ResponseEntity.created(new URI("/api/guild-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /guild-events : Updates an existing guildEvent.
     *
     * @param guildEvent the guildEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated guildEvent,
     * or with status 400 (Bad Request) if the guildEvent is not valid,
     * or with status 500 (Internal Server Error) if the guildEvent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/guild-events")
    public ResponseEntity<GuildEvent> updateGuildEvent(@Valid @RequestBody GuildEvent guildEvent) throws URISyntaxException {
        log.debug("REST request to update GuildEvent : {}", guildEvent);
        if (guildEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildEvent result = guildEventService.save(guildEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, guildEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /guild-events : get all the guildEvents.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of guildEvents in body
     */
    @GetMapping("/guild-events")
    public ResponseEntity<List<GuildEvent>> getAllGuildEvents(GuildEventCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GuildEvents by criteria: {}", criteria);
        Page<GuildEvent> page = guildEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/guild-events");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /guild-events/count : count all the guildEvents.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/guild-events/count")
    public ResponseEntity<Long> countGuildEvents(GuildEventCriteria criteria) {
        log.debug("REST request to count GuildEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(guildEventQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /guild-events/:id : get the "id" guildEvent.
     *
     * @param id the id of the guildEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the guildEvent, or with status 404 (Not Found)
     */
    @GetMapping("/guild-events/{id}")
    public ResponseEntity<GuildEvent> getGuildEvent(@PathVariable Long id) {
        log.debug("REST request to get GuildEvent : {}", id);
        Optional<GuildEvent> guildEvent = guildEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildEvent);
    }

    /**
     * DELETE  /guild-events/:id : delete the "id" guildEvent.
     *
     * @param id the id of the guildEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/guild-events/{id}")
    public ResponseEntity<Void> deleteGuildEvent(@PathVariable Long id) {
        log.debug("REST request to delete GuildEvent : {}", id);
        guildEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
