package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.Actions;
import com.trievosoftware.application.service.ActionsService;
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
 * REST controller for managing Actions.
 */
@RestController
@RequestMapping("/api")
public class ActionsResource {

    private final Logger log = LoggerFactory.getLogger(ActionsResource.class);

    private static final String ENTITY_NAME = "actions";

    private final ActionsService actionsService;

    public ActionsResource(ActionsService actionsService) {
        this.actionsService = actionsService;
    }

    /**
     * POST  /actions : Create a new actions.
     *
     * @param actions the actions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actions, or with status 400 (Bad Request) if the actions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/actions")
    public ResponseEntity<Actions> createActions(@Valid @RequestBody Actions actions) throws URISyntaxException {
        log.debug("REST request to save Actions : {}", actions);
        if (actions.getId() != null) {
            throw new BadRequestAlertException("A new actions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Actions result = actionsService.save(actions);
        return ResponseEntity.created(new URI("/api/actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actions : Updates an existing actions.
     *
     * @param actions the actions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actions,
     * or with status 400 (Bad Request) if the actions is not valid,
     * or with status 500 (Internal Server Error) if the actions couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/actions")
    public ResponseEntity<Actions> updateActions(@Valid @RequestBody Actions actions) throws URISyntaxException {
        log.debug("REST request to update Actions : {}", actions);
        if (actions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Actions result = actionsService.save(actions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actions : get all the actions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actions in body
     */
    @GetMapping("/actions")
    public List<Actions> getAllActions() {
        log.debug("REST request to get all Actions");
        return actionsService.findAll();
    }

    /**
     * GET  /actions/:id : get the "id" actions.
     *
     * @param id the id of the actions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actions, or with status 404 (Not Found)
     */
    @GetMapping("/actions/{id}")
    public ResponseEntity<Actions> getActions(@PathVariable Long id) {
        log.debug("REST request to get Actions : {}", id);
        Optional<Actions> actions = actionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actions);
    }

    /**
     * DELETE  /actions/:id : delete the "id" actions.
     *
     * @param id the id of the actions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/actions/{id}")
    public ResponseEntity<Void> deleteActions(@PathVariable Long id) {
        log.debug("REST request to delete Actions : {}", id);
        actionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
