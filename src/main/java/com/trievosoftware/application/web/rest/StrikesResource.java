package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.Strikes;
import com.trievosoftware.application.service.StrikesService;
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
 * REST controller for managing Strikes.
 */
@RestController
@RequestMapping("/api")
public class StrikesResource {

    private final Logger log = LoggerFactory.getLogger(StrikesResource.class);

    private static final String ENTITY_NAME = "strikes";

    private final StrikesService strikesService;

    public StrikesResource(StrikesService strikesService) {
        this.strikesService = strikesService;
    }

    /**
     * POST  /strikes : Create a new strikes.
     *
     * @param strikes the strikes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new strikes, or with status 400 (Bad Request) if the strikes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/strikes")
    public ResponseEntity<Strikes> createStrikes(@Valid @RequestBody Strikes strikes) throws URISyntaxException {
        log.debug("REST request to save Strikes : {}", strikes);
        if (strikes.getId() != null) {
            throw new BadRequestAlertException("A new strikes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Strikes result = strikesService.save(strikes);
        return ResponseEntity.created(new URI("/api/strikes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /strikes : Updates an existing strikes.
     *
     * @param strikes the strikes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated strikes,
     * or with status 400 (Bad Request) if the strikes is not valid,
     * or with status 500 (Internal Server Error) if the strikes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/strikes")
    public ResponseEntity<Strikes> updateStrikes(@Valid @RequestBody Strikes strikes) throws URISyntaxException {
        log.debug("REST request to update Strikes : {}", strikes);
        if (strikes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Strikes result = strikesService.save(strikes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, strikes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /strikes : get all the strikes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of strikes in body
     */
    @GetMapping("/strikes")
    public List<Strikes> getAllStrikes() {
        log.debug("REST request to get all Strikes");
        return strikesService.findAll();
    }

    /**
     * GET  /strikes/:id : get the "id" strikes.
     *
     * @param id the id of the strikes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the strikes, or with status 404 (Not Found)
     */
    @GetMapping("/strikes/{id}")
    public ResponseEntity<Strikes> getStrikes(@PathVariable Long id) {
        log.debug("REST request to get Strikes : {}", id);
        Optional<Strikes> strikes = strikesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strikes);
    }

    /**
     * DELETE  /strikes/:id : delete the "id" strikes.
     *
     * @param id the id of the strikes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/strikes/{id}")
    public ResponseEntity<Void> deleteStrikes(@PathVariable Long id) {
        log.debug("REST request to delete Strikes : {}", id);
        strikesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
