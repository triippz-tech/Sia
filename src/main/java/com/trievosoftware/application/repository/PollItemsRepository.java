package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.domain.PollItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the PollItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollItemsRepository extends JpaRepository<PollItems, Long> {
    List<PollItems> findAllByItemNameAndPoll(String itemName, Poll poll);
    Optional<PollItems> findByItemNameAndPoll(String itemName, Poll poll);
}
