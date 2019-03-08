package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.TempMutes;
import com.trievosoftware.application.service.TempMutesService;
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
 * REST controller for managing TempMutes.
 */
@RestController
@RequestMapping("/api")
public class TempMutesResource {

    private final Logger log = LoggerFactory.getLogger(TempMutesResource.class);

    private static final String ENTITY_NAME = "tempMutes";

    private final TempMutesService tempMutesService;

    public TempMutesResource(TempMutesService tempMutesService) {
        this.tempMutesService = tempMutesService;
    }

    /**
     * POST  /temp-mutes : Create a new tempMutes.
     *
     * @param tempMutes the tempMutes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tempMutes, or with status 400 (Bad Request) if the tempMutes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/temp-mutes")
    public ResponseEntity<TempMutes> createTempMutes(@Valid @RequestBody TempMutes tempMutes) throws URISyntaxException {
        log.debug("REST request to save TempMutes : {}", tempMutes);
        if (tempMutes.getId() != null) {
            throw new BadRequestAlertException("A new tempMutes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TempMutes result = tempMutesService.save(tempMutes);
        return ResponseEntity.created(new URI("/api/temp-mutes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /temp-mutes : Updates an existing tempMutes.
     *
     * @param tempMutes the tempMutes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tempMutes,
     * or with status 400 (Bad Request) if the tempMutes is not valid,
     * or with status 500 (Internal Server Error) if the tempMutes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/temp-mutes")
    public ResponseEntity<TempMutes> updateTempMutes(@Valid @RequestBody TempMutes tempMutes) throws URISyntaxException {
        log.debug("REST request to update TempMutes : {}", tempMutes);
        if (tempMutes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TempMutes result = tempMutesService.save(tempMutes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tempMutes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /temp-mutes : get all the tempMutes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tempMutes in body
     */
    @GetMapping("/temp-mutes")
    public List<TempMutes> getAllTempMutes() {
        log.debug("REST request to get all TempMutes");
        return tempMutesService.findAll();
    }

    /**
     * GET  /temp-mutes/:id : get the "id" tempMutes.
     *
     * @param id the id of the tempMutes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tempMutes, or with status 404 (Not Found)
     */
    @GetMapping("/temp-mutes/{id}")
    public ResponseEntity<TempMutes> getTempMutes(@PathVariable Long id) {
        log.debug("REST request to get TempMutes : {}", id);
        Optional<TempMutes> tempMutes = tempMutesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tempMutes);
    }

    /**
     * DELETE  /temp-mutes/:id : delete the "id" tempMutes.
     *
     * @param id the id of the tempMutes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/temp-mutes/{id}")
    public ResponseEntity<Void> deleteTempMutes(@PathVariable Long id) {
        log.debug("REST request to delete TempMutes : {}", id);
        tempMutesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
