package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.repository.SongsRepository;
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
 * REST controller for managing Songs.
 */
@RestController
@RequestMapping("/api")
public class SongsResource {

    private final Logger log = LoggerFactory.getLogger(SongsResource.class);

    private static final String ENTITY_NAME = "songs";

    private final SongsRepository songsRepository;

    public SongsResource(SongsRepository songsRepository) {
        this.songsRepository = songsRepository;
    }

    /**
     * POST  /songs : Create a new songs.
     *
     * @param songs the songs to create
     * @return the ResponseEntity with status 201 (Created) and with body the new songs, or with status 400 (Bad Request) if the songs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/songs")
    public ResponseEntity<Songs> createSongs(@Valid @RequestBody Songs songs) throws URISyntaxException {
        log.debug("REST request to save Songs : {}", songs);
        if (songs.getId() != null) {
            throw new BadRequestAlertException("A new songs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Songs result = songsRepository.save(songs);
        return ResponseEntity.created(new URI("/api/songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /songs : Updates an existing songs.
     *
     * @param songs the songs to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated songs,
     * or with status 400 (Bad Request) if the songs is not valid,
     * or with status 500 (Internal Server Error) if the songs couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/songs")
    public ResponseEntity<Songs> updateSongs(@Valid @RequestBody Songs songs) throws URISyntaxException {
        log.debug("REST request to update Songs : {}", songs);
        if (songs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Songs result = songsRepository.save(songs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, songs.getId().toString()))
            .body(result);
    }

    /**
     * GET  /songs : get all the songs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of songs in body
     */
    @GetMapping("/songs")
    public List<Songs> getAllSongs() {
        log.debug("REST request to get all Songs");
        return songsRepository.findAll();
    }

    /**
     * GET  /songs/:id : get the "id" songs.
     *
     * @param id the id of the songs to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the songs, or with status 404 (Not Found)
     */
    @GetMapping("/songs/{id}")
    public ResponseEntity<Songs> getSongs(@PathVariable Long id) {
        log.debug("REST request to get Songs : {}", id);
        Optional<Songs> songs = songsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(songs);
    }

    /**
     * DELETE  /songs/:id : delete the "id" songs.
     *
     * @param id the id of the songs to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSongs(@PathVariable Long id) {
        log.debug("REST request to delete Songs : {}", id);
        songsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
