package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.domain.GiveAway;
import com.trievosoftware.application.service.GiveAwayQueryService;
import com.trievosoftware.application.service.GiveAwayService;
import com.trievosoftware.application.service.dto.GiveAwayCriteria;
import com.trievosoftware.application.web.rest.errors.BadRequestAlertException;
import com.trievosoftware.application.web.rest.util.HeaderUtil;
import com.trievosoftware.application.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GiveAway.
 */
@RestController
@RequestMapping("/api")
public class GiveAwayResource {

    private final Logger log = LoggerFactory.getLogger(GiveAwayResource.class);

    private static final String ENTITY_NAME = "giveAway";

    private final GiveAwayService giveAwayService;

    private final GiveAwayQueryService giveAwayQueryService;

    public GiveAwayResource(GiveAwayService giveAwayService, GiveAwayQueryService giveAwayQueryService) {
        this.giveAwayService = giveAwayService;
        this.giveAwayQueryService = giveAwayQueryService;
    }

    /**
     * POST  /give-aways : Create a new giveAway.
     *
     * @param giveAway the giveAway to create
     * @return the ResponseEntity with status 201 (Created) and with body the new giveAway, or with status 400 (Bad Request) if the giveAway has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/give-aways")
    public ResponseEntity<GiveAway> createGiveAway(@Valid @RequestBody GiveAway giveAway) throws URISyntaxException {
        log.debug("REST request to save GiveAway : {}", giveAway);
        if (giveAway.getId() != null) {
            throw new BadRequestAlertException("A new giveAway cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GiveAway result = giveAwayService.save(giveAway);
        return ResponseEntity.created(new URI("/api/give-aways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /give-aways : Updates an existing giveAway.
     *
     * @param giveAway the giveAway to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated giveAway,
     * or with status 400 (Bad Request) if the giveAway is not valid,
     * or with status 500 (Internal Server Error) if the giveAway couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/give-aways")
    public ResponseEntity<GiveAway> updateGiveAway(@Valid @RequestBody GiveAway giveAway) throws URISyntaxException {
        log.debug("REST request to update GiveAway : {}", giveAway);
        if (giveAway.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GiveAway result = giveAwayService.save(giveAway);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, giveAway.getId().toString()))
            .body(result);
    }

    /**
     * GET  /give-aways : get all the giveAways.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of giveAways in body
     */
    @GetMapping("/give-aways")
    public ResponseEntity<List<GiveAway>> getAllGiveAways(GiveAwayCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GiveAways by criteria: {}", criteria);
        Page<GiveAway> page = giveAwayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/give-aways");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /give-aways/count : count all the giveAways.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/give-aways/count")
    public ResponseEntity<Long> countGiveAways(GiveAwayCriteria criteria) {
        log.debug("REST request to count GiveAways by criteria: {}", criteria);
        return ResponseEntity.ok().body(giveAwayQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /give-aways/:id : get the "id" giveAway.
     *
     * @param id the id of the giveAway to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the giveAway, or with status 404 (Not Found)
     */
    @GetMapping("/give-aways/{id}")
    public ResponseEntity<GiveAway> getGiveAway(@PathVariable Long id) {
        log.debug("REST request to get GiveAway : {}", id);
        Optional<GiveAway> giveAway = giveAwayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giveAway);
    }

    /**
     * DELETE  /give-aways/:id : delete the "id" giveAway.
     *
     * @param id the id of the giveAway to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/give-aways/{id}")
    public ResponseEntity<Void> deleteGiveAway(@PathVariable Long id) {
        log.debug("REST request to delete GiveAway : {}", id);
        giveAwayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
