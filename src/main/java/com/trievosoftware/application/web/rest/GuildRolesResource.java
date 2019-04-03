package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.GuildRoles;
import com.trievosoftware.application.service.GuildRolesService;
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
 * REST controller for managing GuildRoles.
 */
@RestController
@RequestMapping("/api")
public class GuildRolesResource {

    private final Logger log = LoggerFactory.getLogger(GuildRolesResource.class);

    private static final String ENTITY_NAME = "guildRoles";

    private final GuildRolesService guildRolesService;

    public GuildRolesResource(GuildRolesService guildRolesService) {
        this.guildRolesService = guildRolesService;
    }

    /**
     * POST  /guild-roles : Create a new guildRoles.
     *
     * @param guildRoles the guildRoles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new guildRoles, or with status 400 (Bad Request) if the guildRoles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/guild-roles")
    public ResponseEntity<GuildRoles> createGuildRoles(@Valid @RequestBody GuildRoles guildRoles) throws URISyntaxException {
        log.debug("REST request to save GuildRoles : {}", guildRoles);
        if (guildRoles.getId() != null) {
            throw new BadRequestAlertException("A new guildRoles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuildRoles result = guildRolesService.save(guildRoles);
        return ResponseEntity.created(new URI("/api/guild-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /guild-roles : Updates an existing guildRoles.
     *
     * @param guildRoles the guildRoles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated guildRoles,
     * or with status 400 (Bad Request) if the guildRoles is not valid,
     * or with status 500 (Internal Server Error) if the guildRoles couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/guild-roles")
    public ResponseEntity<GuildRoles> updateGuildRoles(@Valid @RequestBody GuildRoles guildRoles) throws URISyntaxException {
        log.debug("REST request to update GuildRoles : {}", guildRoles);
        if (guildRoles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GuildRoles result = guildRolesService.save(guildRoles);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, guildRoles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /guild-roles : get all the guildRoles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of guildRoles in body
     */
    @GetMapping("/guild-roles")
    public List<GuildRoles> getAllGuildRoles() {
        log.debug("REST request to get all GuildRoles");
        return guildRolesService.findAll();
    }

    /**
     * GET  /guild-roles/:id : get the "id" guildRoles.
     *
     * @param id the id of the guildRoles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the guildRoles, or with status 404 (Not Found)
     */
    @GetMapping("/guild-roles/{id}")
    public ResponseEntity<GuildRoles> getGuildRoles(@PathVariable Long id) {
        log.debug("REST request to get GuildRoles : {}", id);
        Optional<GuildRoles> guildRoles = guildRolesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guildRoles);
    }

    /**
     * DELETE  /guild-roles/:id : delete the "id" guildRoles.
     *
     * @param id the id of the guildRoles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/guild-roles/{id}")
    public ResponseEntity<Void> deleteGuildRoles(@PathVariable Long id) {
        log.debug("REST request to delete GuildRoles : {}", id);
        guildRolesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
