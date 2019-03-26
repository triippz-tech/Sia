package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.DiscordUser;
import com.trievosoftware.application.service.DiscordUserService;
import com.trievosoftware.application.web.rest.errors.BadRequestAlertException;
import com.trievosoftware.application.web.rest.util.HeaderUtil;
import com.trievosoftware.application.web.rest.util.PaginationUtil;
import com.trievosoftware.application.service.dto.DiscordUserCriteria;
import com.trievosoftware.application.service.DiscordUserQueryService;
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
 * REST controller for managing DiscordUser.
 */
@RestController
@RequestMapping("/api")
public class DiscordUserResource {

    private final Logger log = LoggerFactory.getLogger(DiscordUserResource.class);

    private static final String ENTITY_NAME = "discordUser";

    private final DiscordUserService discordUserService;

    private final DiscordUserQueryService discordUserQueryService;

    public DiscordUserResource(DiscordUserService discordUserService, DiscordUserQueryService discordUserQueryService) {
        this.discordUserService = discordUserService;
        this.discordUserQueryService = discordUserQueryService;
    }

    /**
     * POST  /discord-users : Create a new discordUser.
     *
     * @param discordUser the discordUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discordUser, or with status 400 (Bad Request) if the discordUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discord-users")
    public ResponseEntity<DiscordUser> createDiscordUser(@Valid @RequestBody DiscordUser discordUser) throws URISyntaxException {
        log.debug("REST request to save DiscordUser : {}", discordUser);
        if (discordUser.getId() != null) {
            throw new BadRequestAlertException("A new discordUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscordUser result = discordUserService.save(discordUser);
        return ResponseEntity.created(new URI("/api/discord-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discord-users : Updates an existing discordUser.
     *
     * @param discordUser the discordUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discordUser,
     * or with status 400 (Bad Request) if the discordUser is not valid,
     * or with status 500 (Internal Server Error) if the discordUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discord-users")
    public ResponseEntity<DiscordUser> updateDiscordUser(@Valid @RequestBody DiscordUser discordUser) throws URISyntaxException {
        log.debug("REST request to update DiscordUser : {}", discordUser);
        if (discordUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiscordUser result = discordUserService.save(discordUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discordUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discord-users : get all the discordUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of discordUsers in body
     */
    @GetMapping("/discord-users")
    public ResponseEntity<List<DiscordUser>> getAllDiscordUsers(DiscordUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DiscordUsers by criteria: {}", criteria);
        Page<DiscordUser> page = discordUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/discord-users");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /discord-users/count : count all the discordUsers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/discord-users/count")
    public ResponseEntity<Long> countDiscordUsers(DiscordUserCriteria criteria) {
        log.debug("REST request to count DiscordUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(discordUserQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /discord-users/:id : get the "id" discordUser.
     *
     * @param id the id of the discordUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discordUser, or with status 404 (Not Found)
     */
    @GetMapping("/discord-users/{id}")
    public ResponseEntity<DiscordUser> getDiscordUser(@PathVariable Long id) {
        log.debug("REST request to get DiscordUser : {}", id);
        Optional<DiscordUser> discordUser = discordUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(discordUser);
    }

    /**
     * DELETE  /discord-users/:id : delete the "id" discordUser.
     *
     * @param id the id of the discordUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discord-users/{id}")
    public ResponseEntity<Void> deleteDiscordUser(@PathVariable Long id) {
        log.debug("REST request to delete DiscordUser : {}", id);
        discordUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
