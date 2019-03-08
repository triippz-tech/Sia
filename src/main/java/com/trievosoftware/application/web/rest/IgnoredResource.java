package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.Ignored;
import com.trievosoftware.application.service.IgnoredService;
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
 * REST controller for managing Ignored.
 */
@RestController
@RequestMapping("/api")
public class IgnoredResource {

    private final Logger log = LoggerFactory.getLogger(IgnoredResource.class);

    private static final String ENTITY_NAME = "ignored";

    private final IgnoredService ignoredService;

    public IgnoredResource(IgnoredService ignoredService) {
        this.ignoredService = ignoredService;
    }

    /**
     * POST  /ignoreds : Create a new ignored.
     *
     * @param ignored the ignored to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ignored, or with status 400 (Bad Request) if the ignored has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ignoreds")
    public ResponseEntity<Ignored> createIgnored(@Valid @RequestBody Ignored ignored) throws URISyntaxException {
        log.debug("REST request to save Ignored : {}", ignored);
        if (ignored.getId() != null) {
            throw new BadRequestAlertException("A new ignored cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ignored result = ignoredService.save(ignored);
        return ResponseEntity.created(new URI("/api/ignoreds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ignoreds : Updates an existing ignored.
     *
     * @param ignored the ignored to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ignored,
     * or with status 400 (Bad Request) if the ignored is not valid,
     * or with status 500 (Internal Server Error) if the ignored couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ignoreds")
    public ResponseEntity<Ignored> updateIgnored(@Valid @RequestBody Ignored ignored) throws URISyntaxException {
        log.debug("REST request to update Ignored : {}", ignored);
        if (ignored.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ignored result = ignoredService.save(ignored);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ignored.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ignoreds : get all the ignoreds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ignoreds in body
     */
    @GetMapping("/ignoreds")
    public List<Ignored> getAllIgnoreds() {
        log.debug("REST request to get all Ignoreds");
        return ignoredService.findAll();
    }

    /**
     * GET  /ignoreds/:id : get the "id" ignored.
     *
     * @param id the id of the ignored to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ignored, or with status 404 (Not Found)
     */
    @GetMapping("/ignoreds/{id}")
    public ResponseEntity<Ignored> getIgnored(@PathVariable Long id) {
        log.debug("REST request to get Ignored : {}", id);
        Optional<Ignored> ignored = ignoredService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ignored);
    }

    /**
     * DELETE  /ignoreds/:id : delete the "id" ignored.
     *
     * @param id the id of the ignored to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ignoreds/{id}")
    public ResponseEntity<Void> deleteIgnored(@PathVariable Long id) {
        log.debug("REST request to delete Ignored : {}", id);
        ignoredService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
