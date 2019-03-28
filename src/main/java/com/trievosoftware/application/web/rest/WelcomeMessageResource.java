package com.trievosoftware.application.web.rest;

import com.trievosoftware.application.domain.WelcomeMessage;
import com.trievosoftware.application.service.WelcomeMessageQueryService;
import com.trievosoftware.application.service.WelcomeMessageService;
import com.trievosoftware.application.service.dto.WelcomeMessageCriteria;
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
 * REST controller for managing WelcomeMessage.
 */
@RestController
@RequestMapping("/api")
public class WelcomeMessageResource {

    private final Logger log = LoggerFactory.getLogger(com.trievosoftware.application.web.rest.WelcomeMessageResource.class);

    private static final String ENTITY_NAME = "welcomeMessage";

    private final WelcomeMessageService welcomeMessageService;

    private final WelcomeMessageQueryService welcomeMessageQueryService;

    public WelcomeMessageResource(WelcomeMessageService welcomeMessageService, WelcomeMessageQueryService welcomeMessageQueryService) {
        this.welcomeMessageService = welcomeMessageService;
        this.welcomeMessageQueryService = welcomeMessageQueryService;
    }

    /**
     * POST  /welcome-messages : Create a new welcomeMessage.
     *
     * @param welcomeMessage the welcomeMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new welcomeMessage, or with status 400 (Bad Request) if the welcomeMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/welcome-messages")
    public ResponseEntity<WelcomeMessage> createWelcomeMessage(@Valid @RequestBody WelcomeMessage welcomeMessage) throws URISyntaxException {
        log.debug("REST request to save WelcomeMessage : {}", welcomeMessage);
        if (welcomeMessage.getId() != null) {
            throw new BadRequestAlertException("A new welcomeMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WelcomeMessage result = welcomeMessageService.save(welcomeMessage);
        return ResponseEntity.created(new URI("/api/welcome-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /welcome-messages : Updates an existing welcomeMessage.
     *
     * @param welcomeMessage the welcomeMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated welcomeMessage,
     * or with status 400 (Bad Request) if the welcomeMessage is not valid,
     * or with status 500 (Internal Server Error) if the welcomeMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/welcome-messages")
    public ResponseEntity<WelcomeMessage> updateWelcomeMessage(@Valid @RequestBody WelcomeMessage welcomeMessage) throws URISyntaxException {
        log.debug("REST request to update WelcomeMessage : {}", welcomeMessage);
        if (welcomeMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WelcomeMessage result = welcomeMessageService.save(welcomeMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, welcomeMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /welcome-messages : get all the welcomeMessages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of welcomeMessages in body
     */
    @GetMapping("/welcome-messages")
    public ResponseEntity<List<WelcomeMessage>> getAllWelcomeMessages(WelcomeMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WelcomeMessages by criteria: {}", criteria);
        Page<WelcomeMessage> page = welcomeMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/welcome-messages");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /welcome-messages/count : count all the welcomeMessages.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/welcome-messages/count")
    public ResponseEntity<Long> countWelcomeMessages(WelcomeMessageCriteria criteria) {
        log.debug("REST request to count WelcomeMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(welcomeMessageQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /welcome-messages/:id : get the "id" welcomeMessage.
     *
     * @param id the id of the welcomeMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the welcomeMessage, or with status 404 (Not Found)
     */
    @GetMapping("/welcome-messages/{id}")
    public ResponseEntity<WelcomeMessage> getWelcomeMessage(@PathVariable Long id) {
        log.debug("REST request to get WelcomeMessage : {}", id);
        Optional<WelcomeMessage> welcomeMessage = welcomeMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(welcomeMessage);
    }

    /**
     * DELETE  /welcome-messages/:id : delete the "id" welcomeMessage.
     *
     * @param id the id of the welcomeMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/welcome-messages/{id}")
    public ResponseEntity<Void> deleteWelcomeMessage(@PathVariable Long id) {
        log.debug("REST request to delete WelcomeMessage : {}", id);
        welcomeMessageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
