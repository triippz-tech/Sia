package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.service.PollItemsService;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.repository.PollItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing PollItems.
 */
@Service
@Transactional
public class PollItemsServiceImpl implements PollItemsService {

    private final Logger log = LoggerFactory.getLogger(PollItemsServiceImpl.class);

    private final PollItemsRepository pollItemsRepository;

    public PollItemsServiceImpl(PollItemsRepository pollItemsRepository) {
        this.pollItemsRepository = pollItemsRepository;
    }

    /**
     * Save a pollItems.
     *
     * @param pollItems the entity to save
     * @return the persisted entity
     */
    @Override
    public PollItems save(PollItems pollItems) {
        log.debug("Request to save PollItems : {}", pollItems);
        return pollItemsRepository.save(pollItems);
    }

    /**
     * Get all the pollItems.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PollItems> findAll() {
        log.debug("Request to get all PollItems");
        return pollItemsRepository.findAll();
    }


    /**
     * Get one pollItems by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PollItems> findOne(Long id) {
        log.debug("Request to get PollItems : {}", id);
        return pollItemsRepository.findById(id);
    }

    /**
     * Delete the pollItems by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PollItems : {}", id);        pollItemsRepository.deleteById(id);
    }

    /**
     *
     * @param pollItems
     * @return
     */
    @Override
    @Transactional
    public PollItems createPollItem(PollItems pollItems)
    {
        log.debug("Reqest to create new PollItem");
        save(pollItems);
        return pollItems;
    }


    @Override
    @Transactional
    public PollItems createPollItem(String itemName, Poll poll)
    {
        log.debug("Reqest to create new PollItem");
        PollItems pollItem = new PollItems(itemName, poll);
        pollItemsRepository.saveAndFlush(pollItem);
        log.debug("New PollItem={} created." , itemName);
        return pollItem;
    }

    @Override
    @Transactional
    public void removeVote(Long itemId)
    {
        log.debug("Request to remove vote from PollItem={}", itemId);
        Optional<PollItems> pollItem = findOne(itemId);
        if ( !pollItem.isPresent() ) return;

        pollItem.get().setVotes(pollItem.get().getVotes() - 1);
        save(pollItem.get());
    }


    @Override
    @Transactional
    public void addVote(Long itemId)
    {
        log.debug("Request to add vote to PollItem={}", itemId);
        Optional<PollItems> pollItem = findOne(itemId);
        if ( !pollItem.isPresent() ) return;

        pollItem.get().setVotes(pollItem.get().getVotes() + 1);
        save(pollItem.get());
    }


    @Override
    @Transactional
    public void addVote(String itemName, Poll poll)
    {
        log.debug("Request to add vote to PollItem={} for Poll={}", itemName, poll.getTitle());
        Optional<PollItems> pollItem = pollItemsRepository.findByItemNameAndPoll(itemName, poll);
        if ( !pollItem.isPresent() ) return;

        pollItem.get().setVotes(pollItem.get().getVotes() + 1);
        save(pollItem.get());
    }
}
