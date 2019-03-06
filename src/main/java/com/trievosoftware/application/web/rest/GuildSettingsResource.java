package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.service.GuildSettingsService;
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
 * REST controller for managing GuildSettings.
 */
@RestController
@RequestMapping("/api")
public class GuildSettingsResource {

    private final Logger log = LoggerFactory.getLogger(GuildSettingsResource.class);

    private static final String ENTITY_NAME = "guildSettings";

    private final GuildSettingsService guildSettingsService;

    public GuildSettingsResource(GuildSettingsService guildSettingsService) {
        this.guildSettingsService = guildSettingsService;
    }

    /**
     * POST  /guild-settings : Create a new guildSettings.
     *
     * @param guildSettings the guildSettings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new guildSettings, or with status 400 (Bad Request) if the guildSettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/guild-settings")
    public ResponseEntity<GuildSettings> createGuildSettings(@Valid @RequestBody GuildSettings guildSettings) throws URISyntaxException {
        log.debug("REST request to save GuildSettings : {}", guildSettings);
        if (guildSettings.getId() != null) {
            throw new BadRequestAlertException("A new guildSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildSettings result = guildSettingsService.save(guildSettings);
        return ResponseEntity.created(new URI("/api/guild-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /guild-settings : Updates an existing guildSettings.
     *
     * @param guildSettings the guildSettings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated guildSettings,
     * or with status 400 (Bad Request) if the guildSettings is not valid,
     * or with status 500 (Internal Server Error) if the guildSettings couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/guild-settings")
    public ResponseEntity<GuildSettings> updateGuildSettings(@Valid @RequestBody GuildSettings guildSettings) throws URISyntaxException {
        log.debug("REST request to update GuildSettings : {}", guildSettings);
        if (guildSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildSettings result = guildSettingsService.save(guildSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, guildSettings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /guild-settings : get all the guildSettings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of guildSettings in body
     */
    @GetMapping("/guild-settings")
    public List<GuildSettings> getAllGuildSettings() {
        log.debug("REST request to get all GuildSettings");
        return guildSettingsService.findAll();
    }

    /**
     * GET  /guild-settings/:id : get the "id" guildSettings.
     *
     * @param id the id of the guildSettings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the guildSettings, or with status 404 (Not Found)
     */
    @GetMapping("/guild-settings/{id}")
    public ResponseEntity<GuildSettings> getGuildSettings(@PathVariable Long id) {
        log.debug("REST request to get GuildSettings : {}", id);
        Optional<GuildSettings> guildSettings = guildSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildSettings);
    }

    /**
     * DELETE  /guild-settings/:id : delete the "id" guildSettings.
     *
     * @param id the id of the guildSettings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/guild-settings/{id}")
    public ResponseEntity<Void> deleteGuildSettings(@PathVariable Long id) {
        log.debug("REST request to delete GuildSettings : {}", id);
        guildSettingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
