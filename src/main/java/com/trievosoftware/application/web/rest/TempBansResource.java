package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.TempBans;
import com.trievosoftware.application.service.TempBansService;
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
 * REST controller for managing TempBans.
 */
@RestController
@RequestMapping("/api")
public class TempBansResource {

    private final Logger log = LoggerFactory.getLogger(TempBansResource.class);

    private static final String ENTITY_NAME = "tempBans";

    private final TempBansService tempBansService;

    public TempBansResource(TempBansService tempBansService) {
        this.tempBansService = tempBansService;
    }

    /**
     * POST  /temp-bans : Create a new tempBans.
     *
     * @param tempBans the tempBans to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tempBans, or with status 400 (Bad Request) if the tempBans has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/temp-bans")
    public ResponseEntity<TempBans> createTempBans(@Valid @RequestBody TempBans tempBans) throws URISyntaxException {
        log.debug("REST request to save TempBans : {}", tempBans);
        if (tempBans.getId() != null) {
            throw new BadRequestAlertException("A new tempBans cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TempBans result = tempBansService.save(tempBans);
        return ResponseEntity.created(new URI("/api/temp-bans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /temp-bans : Updates an existing tempBans.
     *
     * @param tempBans the tempBans to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tempBans,
     * or with status 400 (Bad Request) if the tempBans is not valid,
     * or with status 500 (Internal Server Error) if the tempBans couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/temp-bans")
    public ResponseEntity<TempBans> updateTempBans(@Valid @RequestBody TempBans tempBans) throws URISyntaxException {
        log.debug("REST request to update TempBans : {}", tempBans);
        if (tempBans.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TempBans result = tempBansService.save(tempBans);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tempBans.getId().toString()))
            .body(result);
    }

    /**
     * GET  /temp-bans : get all the tempBans.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tempBans in body
     */
    @GetMapping("/temp-bans")
    public List<TempBans> getAllTempBans() {
        log.debug("REST request to get all TempBans");
        return tempBansService.findAll();
    }

    /**
     * GET  /temp-bans/:id : get the "id" tempBans.
     *
     * @param id the id of the tempBans to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tempBans, or with status 404 (Not Found)
     */
    @GetMapping("/temp-bans/{id}")
    public ResponseEntity<TempBans> getTempBans(@PathVariable Long id) {
        log.debug("REST request to get TempBans : {}", id);
        Optional<TempBans> tempBans = tempBansService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tempBans);
    }

    /**
     * DELETE  /temp-bans/:id : delete the "id" tempBans.
     *
     * @param id the id of the tempBans to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/temp-bans/{id}")
    public ResponseEntity<Void> deleteTempBans(@PathVariable Long id) {
        log.debug("REST request to delete TempBans : {}", id);
        tempBansService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
