package com.trievosoftware.application.web.rest;
import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.service.PollService;
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
 * REST controller for managing Poll.
 */
@RestController
@RequestMapping("/api")
public class PollResource {

    private final Logger log = LoggerFactory.getLogger(PollResource.class);

    private static final String ENTITY_NAME = "poll";

    private final PollService pollService;

    public PollResource(PollService pollService) {
        this.pollService = pollService;
    }

    /**
     * POST  /polls : Create a new poll.
     *
     * @param poll the poll to create
     * @return the ResponseEntity with status 201 (Created) and with body the new poll, or with status 400 (Bad Request) if the poll has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/polls")
    public ResponseEntity<Poll> createPoll(@Valid @RequestBody Poll poll) throws URISyntaxException {
        log.debug("REST request to save Poll : {}", poll);
        if (poll.getId() != null) {
            throw new BadRequestAlertException("A new poll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Poll result = pollService.save(poll);
        return ResponseEntity.created(new URI("/api/polls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polls : Updates an existing poll.
     *
     * @param poll the poll to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated poll,
     * or with status 400 (Bad Request) if the poll is not valid,
     * or with status 500 (Internal Server Error) if the poll couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/polls")
    public ResponseEntity<Poll> updatePoll(@Valid @RequestBody Poll poll) throws URISyntaxException {
        log.debug("REST request to update Poll : {}", poll);
        if (poll.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Poll result = pollService.save(poll);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, poll.getId().toString()))
            .body(result);
    }

    /**
     * GET  /polls : get all the polls.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of polls in body
     */
    @GetMapping("/polls")
    public List<Poll> getAllPolls() {
        log.debug("REST request to get all Polls");
        return pollService.findAll();
    }

    /**
     * GET  /polls/:id : get the "id" poll.
     *
     * @param id the id of the poll to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the poll, or with status 404 (Not Found)
     */
    @GetMapping("/polls/{id}")
    public ResponseEntity<Poll> getPoll(@PathVariable Long id) {
        log.debug("REST request to get Poll : {}", id);
        Optional<Poll> poll = pollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(poll);
    }

    /**
     * DELETE  /polls/:id : delete the "id" poll.
     *
     * @param id the id of the poll to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/polls/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        log.debug("REST request to delete Poll : {}", id);
        pollService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
