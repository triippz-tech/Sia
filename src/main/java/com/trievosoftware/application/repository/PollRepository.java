package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Poll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Poll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByGuildIdAndUserId(Long guildId, Long userId);
    List<Poll> findAllByGuildId(Long guildId);
    List<Poll> findAllByUserId(Long userId);
    Optional<Poll> findByGuildIdAndUserIdAndTitle(Long guildId, Long userId, String title);
    Optional<Poll> findByGuildIdAndTitle(Long guildId, String pollName);
    List<Poll> findAllByFinishTimeIsLessThan(Instant now);
    List<Poll> findAllByGuildIdAndUserIdAndFinishTimeIsLessThan(Long guildId, Long userId, Instant now);
    List<Poll> findAllByGuildIdAndFinishTimeIsLessThan(Long guildId, Instant now);
    List<Poll> findAllByUserIdAndFinishTimeIsLessThan(Long userId, Instant now);
    List<Poll> findAllByGuildIdAndFinishTimeIsGreaterThan(Long guildId, Instant now);
    Optional<Poll> findByGuildIdAndMessageId(Long guildId, Long messageId);
    List<Poll> findAllByFinishTimeIsGreaterThan(Instant now);
}
