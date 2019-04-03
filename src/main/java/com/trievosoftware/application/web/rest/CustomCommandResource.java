package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.service.CustomCommandService;
import com.trievosoftware.application.web.rest.errors.BadRequestAlertException;
import com.trievosoftware.application.web.rest.util.HeaderUtil;
import com.trievosoftware.application.web.rest.util.PaginationUtil;
import com.trievosoftware.application.service.dto.CustomCommandCriteria;
import com.trievosoftware.application.service.CustomCommandQueryService;
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
 * REST controller for managing CustomCommand.
 */
@RestController
@RequestMapping("/api")
public class CustomCommandResource {

    private final Logger log = LoggerFactory.getLogger(CustomCommandResource.class);

    private static final String ENTITY_NAME = "customCommand";

    private final CustomCommandService customCommandService;

    private final CustomCommandQueryService customCommandQueryService;

    public CustomCommandResource(CustomCommandService customCommandService, CustomCommandQueryService customCommandQueryService) {
        this.customCommandService = customCommandService;
        this.customCommandQueryService = customCommandQueryService;
    }

    /**
     * POST  /custom-commands : Create a new customCommand.
     *
     * @param customCommand the customCommand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customCommand, or with status 400 (Bad Request) if the customCommand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/custom-commands")
    public ResponseEntity<CustomCommand> createCustomCommand(@Valid @RequestBody CustomCommand customCommand) throws URISyntaxException {
        log.debug("REST request to save CustomCommand : {}", customCommand);
        if (customCommand.getId() != null) {
            throw new BadRequestAlertException("A new customCommand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomCommand result = customCommandService.save(customCommand);
        return ResponseEntity.created(new URI("/api/custom-commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /custom-commands : Updates an existing customCommand.
     *
     * @param customCommand the customCommand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customCommand,
     * or with status 400 (Bad Request) if the customCommand is not valid,
     * or with status 500 (Internal Server Error) if the customCommand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/custom-commands")
    public ResponseEntity<CustomCommand> updateCustomCommand(@Valid @RequestBody CustomCommand customCommand) throws URISyntaxException {
        log.debug("REST request to update CustomCommand : {}", customCommand);
        if (customCommand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomCommand result = customCommandService.save(customCommand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customCommand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /custom-commands : get all the customCommands.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of customCommands in body
     */
    @GetMapping("/custom-commands")
    public ResponseEntity<List<CustomCommand>> getAllCustomCommands(CustomCommandCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CustomCommands by criteria: {}", criteria);
        Page<CustomCommand> page = customCommandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/custom-commands");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /custom-commands/count : count all the customCommands.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/custom-commands/count")
    public ResponseEntity<Long> countCustomCommands(CustomCommandCriteria criteria) {
        log.debug("REST request to count CustomCommands by criteria: {}", criteria);
        return ResponseEntity.ok().body(customCommandQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /custom-commands/:id : get the "id" customCommand.
     *
     * @param id the id of the customCommand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customCommand, or with status 404 (Not Found)
     */
    @GetMapping("/custom-commands/{id}")
    public ResponseEntity<CustomCommand> getCustomCommand(@PathVariable Long id) {
        log.debug("REST request to get CustomCommand : {}", id);
        Optional<CustomCommand> customCommand = customCommandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customCommand);
    }

    /**
     * DELETE  /custom-commands/:id : delete the "id" customCommand.
     *
     * @param id the id of the customCommand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/custom-commands/{id}")
    public ResponseEntity<Void> deleteCustomCommand(@PathVariable Long id) {
        log.debug("REST request to delete CustomCommand : {}", id);
        customCommandService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
