package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.repository.GuildMusicSettingsRepository;
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
 * REST controller for managing GuildMusicSettings.
 */
@RestController
@RequestMapping("/api")
public class GuildMusicSettingsResource {

    private final Logger log = LoggerFactory.getLogger(GuildMusicSettingsResource.class);

    private static final String ENTITY_NAME = "guildMusicSettings";

    private final GuildMusicSettingsRepository guildMusicSettingsRepository;

    public GuildMusicSettingsResource(GuildMusicSettingsRepository guildMusicSettingsRepository) {
        this.guildMusicSettingsRepository = guildMusicSettingsRepository;
    }

    /**
     * POST  /guild-music-settings : Create a new guildMusicSettings.
     *
     * @param guildMusicSettings the guildMusicSettings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new guildMusicSettings, or with status 400 (Bad Request) if the guildMusicSettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/guild-music-settings")
    public ResponseEntity<GuildMusicSettings> createGuildMusicSettings(@Valid @RequestBody GuildMusicSettings guildMusicSettings) throws URISyntaxException {
        log.debug("REST request to save GuildMusicSettings : {}", guildMusicSettings);
        if (guildMusicSettings.getId() != null) {
            throw new BadRequestAlertException("A new guildMusicSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildMusicSettings result = guildMusicSettingsRepository.save(guildMusicSettings);
        return ResponseEntity.created(new URI("/api/guild-music-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /guild-music-settings : Updates an existing guildMusicSettings.
     *
     * @param guildMusicSettings the guildMusicSettings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated guildMusicSettings,
     * or with status 400 (Bad Request) if the guildMusicSettings is not valid,
     * or with status 500 (Internal Server Error) if the guildMusicSettings couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/guild-music-settings")
    public ResponseEntity<GuildMusicSettings> updateGuildMusicSettings(@Valid @RequestBody GuildMusicSettings guildMusicSettings) throws URISyntaxException {
        log.debug("REST request to update GuildMusicSettings : {}", guildMusicSettings);
        if (guildMusicSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildMusicSettings result = guildMusicSettingsRepository.save(guildMusicSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, guildMusicSettings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /guild-music-settings : get all the guildMusicSettings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of guildMusicSettings in body
     */
    @GetMapping("/guild-music-settings")
    public List<GuildMusicSettings> getAllGuildMusicSettings() {
        log.debug("REST request to get all GuildMusicSettings");
        return guildMusicSettingsRepository.findAll();
    }

    /**
     * GET  /guild-music-settings/:id : get the "id" guildMusicSettings.
     *
     * @param id the id of the guildMusicSettings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the guildMusicSettings, or with status 404 (Not Found)
     */
    @GetMapping("/guild-music-settings/{id}")
    public ResponseEntity<GuildMusicSettings> getGuildMusicSettings(@PathVariable Long id) {
        log.debug("REST request to get GuildMusicSettings : {}", id);
        Optional<GuildMusicSettings> guildMusicSettings = guildMusicSettingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(guildMusicSettings);
    }

    /**
     * DELETE  /guild-music-settings/:id : delete the "id" guildMusicSettings.
     *
     * @param id the id of the guildMusicSettings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/guild-music-settings/{id}")
    public ResponseEntity<Void> deleteGuildMusicSettings(@PathVariable Long id) {
        log.debug("REST request to delete GuildMusicSettings : {}", id);
        guildMusicSettingsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
