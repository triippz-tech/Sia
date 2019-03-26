package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.DiscordUser;
import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.exceptions.UserHasNoVoteException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing PollItems.
 */
public interface PollItemsService {

    /**
     * Save a pollItems.
     *
     * @param pollItems the entity to save
     * @return the persisted entity
     */
    PollItems save(PollItems pollItems);

    /**
     * Get all the pollItems.
     *
     * @return the list of entities
     */
    List<PollItems> findAll();


    /**
     * Get the "id" pollItems.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PollItems> findOne(Long id);

    /**
     * Delete the "id" pollItems.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    @Transactional
    PollItems createPollItem(PollItems pollItems);

    @Transactional
    PollItems createPollItem(String itemName, Poll poll);


    @Transactional
    void removeVote(Long itemId);

    @Transactional
    void addVote(Long itemId);

    @Transactional
    void addVote(String itemName, Poll poll);

    @Transactional
    void addVoteToUser(DiscordUser user, PollItems item);

    @Transactional
    void removeVoteFromUser(DiscordUser user, PollItems item);

    @Transactional
    boolean userVotedThis(DiscordUser user, PollItems item);
}
