package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.service.DiscordGuildService;
import com.trievosoftware.application.web.rest.errors.BadRequestAlertException;
import com.trievosoftware.application.web.rest.util.HeaderUtil;
import com.trievosoftware.application.web.rest.util.PaginationUtil;
import com.trievosoftware.application.service.dto.DiscordGuildCriteria;
import com.trievosoftware.application.service.DiscordGuildQueryService;
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
 * REST controller for managing DiscordGuild.
 */
@RestController
@RequestMapping("/api")
public class DiscordGuildResource {

    private final Logger log = LoggerFactory.getLogger(DiscordGuildResource.class);

    private static final String ENTITY_NAME = "discordGuild";

    private final DiscordGuildService discordGuildService;

    private final DiscordGuildQueryService discordGuildQueryService;

    public DiscordGuildResource(DiscordGuildService discordGuildService, DiscordGuildQueryService discordGuildQueryService) {
        this.discordGuildService = discordGuildService;
        this.discordGuildQueryService = discordGuildQueryService;
    }

    /**
     * POST  /discord-guilds : Create a new discordGuild.
     *
     * @param discordGuild the discordGuild to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discordGuild, or with status 400 (Bad Request) if the discordGuild has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discord-guilds")
    public ResponseEntity<DiscordGuild> createDiscordGuild(@Valid @RequestBody DiscordGuild discordGuild) throws URISyntaxException {
        log.debug("REST request to save DiscordGuild : {}", discordGuild);
        if (discordGuild.getId() != null) {
            throw new BadRequestAlertException("A new discordGuild cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscordGuild result = discordGuildService.save(discordGuild);
        return ResponseEntity.created(new URI("/api/discord-guilds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discord-guilds : Updates an existing discordGuild.
     *
     * @param discordGuild the discordGuild to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discordGuild,
     * or with status 400 (Bad Request) if the discordGuild is not valid,
     * or with status 500 (Internal Server Error) if the discordGuild couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discord-guilds")
    public ResponseEntity<DiscordGuild> updateDiscordGuild(@Valid @RequestBody DiscordGuild discordGuild) throws URISyntaxException {
        log.debug("REST request to update DiscordGuild : {}", discordGuild);
        if (discordGuild.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiscordGuild result = discordGuildService.save(discordGuild);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discordGuild.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discord-guilds : get all the discordGuilds.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of discordGuilds in body
     */
    @GetMapping("/discord-guilds")
    public ResponseEntity<List<DiscordGuild>> getAllDiscordGuilds(DiscordGuildCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DiscordGuilds by criteria: {}", criteria);
        Page<DiscordGuild> page = discordGuildQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/discord-guilds");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /discord-guilds/count : count all the discordGuilds.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/discord-guilds/count")
    public ResponseEntity<Long> countDiscordGuilds(DiscordGuildCriteria criteria) {
        log.debug("REST request to count DiscordGuilds by criteria: {}", criteria);
        return ResponseEntity.ok().body(discordGuildQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /discord-guilds/:id : get the "id" discordGuild.
     *
     * @param id the id of the discordGuild to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discordGuild, or with status 404 (Not Found)
     */
    @GetMapping("/discord-guilds/{id}")
    public ResponseEntity<DiscordGuild> getDiscordGuild(@PathVariable Long id) {
        log.debug("REST request to get DiscordGuild : {}", id);
        Optional<DiscordGuild> discordGuild = discordGuildService.findOne(id);
        return ResponseUtil.wrapOrNotFound(discordGuild);
    }

    /**
     * DELETE  /discord-guilds/:id : delete the "id" discordGuild.
     *
     * @param id the id of the discordGuild to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discord-guilds/{id}")
    public ResponseEntity<Void> deleteDiscordGuild(@PathVariable Long id) {
        log.debug("REST request to delete DiscordGuild : {}", id);
        discordGuildService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
