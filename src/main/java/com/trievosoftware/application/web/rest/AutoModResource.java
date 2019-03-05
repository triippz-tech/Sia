package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.AutoMod;
import com.trievosoftware.application.service.AutoModService;
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
 * REST controller for managing AutoMod.
 */
@RestController
@RequestMapping("/api")
public class AutoModResource {

    private final Logger log = LoggerFactory.getLogger(AutoModResource.class);

    private static final String ENTITY_NAME = "autoMod";

    private final AutoModService autoModService;

    public AutoModResource(AutoModService autoModService) {
        this.autoModService = autoModService;
    }

    /**
     * POST  /auto-mods : Create a new autoMod.
     *
     * @param autoMod the autoMod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new autoMod, or with status 400 (Bad Request) if the autoMod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/auto-mods")
    public ResponseEntity<AutoMod> createAutoMod(@Valid @RequestBody AutoMod autoMod) throws URISyntaxException {
        log.debug("REST request to save AutoMod : {}", autoMod);
        if (autoMod.getId() != null) {
            throw new BadRequestAlertException("A new autoMod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutoMod result = autoModService.save(autoMod);
        return ResponseEntity.created(new URI("/api/auto-mods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auto-mods : Updates an existing autoMod.
     *
     * @param autoMod the autoMod to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated autoMod,
     * or with status 400 (Bad Request) if the autoMod is not valid,
     * or with status 500 (Internal Server Error) if the autoMod couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/auto-mods")
    public ResponseEntity<AutoMod> updateAutoMod(@Valid @RequestBody AutoMod autoMod) throws URISyntaxException {
        log.debug("REST request to update AutoMod : {}", autoMod);
        if (autoMod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutoMod result = autoModService.save(autoMod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, autoMod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auto-mods : get all the autoMods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of autoMods in body
     */
    @GetMapping("/auto-mods")
    public List<AutoMod> getAllAutoMods() {
        log.debug("REST request to get all AutoMods");
        return autoModService.findAll();
    }

    /**
     * GET  /auto-mods/:id : get the "id" autoMod.
     *
     * @param id the id of the autoMod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the autoMod, or with status 404 (Not Found)
     */
    @GetMapping("/auto-mods/{id}")
    public ResponseEntity<AutoMod> getAutoMod(@PathVariable Long id) {
        log.debug("REST request to get AutoMod : {}", id);
        Optional<AutoMod> autoMod = autoModService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoMod);
    }

    /**
     * DELETE  /auto-mods/:id : delete the "id" autoMod.
     *
     * @param id the id of the autoMod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/auto-mods/{id}")
    public ResponseEntity<Void> deleteAutoMod(@PathVariable Long id) {
        log.debug("REST request to delete AutoMod : {}", id);
        autoModService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
