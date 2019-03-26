package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.service.PollItemsService;
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
 * REST controller for managing PollItems.
 */
@RestController
@RequestMapping("/api")
public class PollItemsResource {

    private final Logger log = LoggerFactory.getLogger(PollItemsResource.class);

    private static final String ENTITY_NAME = "pollItems";

    private final PollItemsService pollItemsService;

    public PollItemsResource(PollItemsService pollItemsService) {
        this.pollItemsService = pollItemsService;
    }

    /**
     * POST  /poll-items : Create a new pollItems.
     *
     * @param pollItems the pollItems to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pollItems, or with status 400 (Bad Request) if the pollItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/poll-items")
    public ResponseEntity<PollItems> createPollItems(@Valid @RequestBody PollItems pollItems) throws URISyntaxException {
        log.debug("REST request to save PollItems : {}", pollItems);
        if (pollItems.getId() != null) {
            throw new BadRequestAlertException("A new pollItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PollItems result = pollItemsService.save(pollItems);
        return ResponseEntity.created(new URI("/api/poll-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /poll-items : Updates an existing pollItems.
     *
     * @param pollItems the pollItems to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pollItems,
     * or with status 400 (Bad Request) if the pollItems is not valid,
     * or with status 500 (Internal Server Error) if the pollItems couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/poll-items")
    public ResponseEntity<PollItems> updatePollItems(@Valid @RequestBody PollItems pollItems) throws URISyntaxException {
        log.debug("REST request to update PollItems : {}", pollItems);
        if (pollItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PollItems result = pollItemsService.save(pollItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pollItems.getId().toString()))
            .body(result);
    }

    /**
     * GET  /poll-items : get all the pollItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of pollItems in body
     */
    @GetMapping("/poll-items")
    public List<PollItems> getAllPollItems(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PollItems");
        return pollItemsService.findAll();
    }

    /**
     * GET  /poll-items/:id : get the "id" pollItems.
     *
     * @param id the id of the pollItems to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pollItems, or with status 404 (Not Found)
     */
    @GetMapping("/poll-items/{id}")
    public ResponseEntity<PollItems> getPollItems(@PathVariable Long id) {
        log.debug("REST request to get PollItems : {}", id);
        Optional<PollItems> pollItems = pollItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pollItems);
    }

    /**
     * DELETE  /poll-items/:id : delete the "id" pollItems.
     *
     * @param id the id of the pollItems to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/poll-items/{id}")
    public ResponseEntity<Void> deletePollItems(@PathVariable Long id) {
        log.debug("REST request to delete PollItems : {}", id);
        pollItemsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
