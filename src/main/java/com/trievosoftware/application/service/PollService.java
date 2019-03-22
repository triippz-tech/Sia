package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.exceptions.NoPollsFoundException;
import com.trievosoftware.application.exceptions.PollExpiredException;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.webhook.WebhookClient;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Poll.
 */
public interface PollService {

    /**
     * Save a poll.
     *
     * @param poll the entity to save
     * @return the persisted entity
     */
    Poll save(Poll poll);

    /**
     * Get all the polls.
     *
     * @return the list of entities
     */
    List<Poll> findAll();


    /**
     * Get the "id" poll.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Poll> findOne(Long id);

    /**
     * Delete the "id" poll.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<Poll> findAllByGuildIdAndUserId(Long guildId, Long userId);

    List<Poll> findAllByGuildId(Long guildId);

    List<Poll> findAllByUserId(Long userId);

    Optional<Poll> findByGuildIdAndUserIdAndTitle(Long guildId, Long userId, String title);

    List<Poll> findAllByFinishTimeIsLessThan(Instant now);

    List<Poll> findAllByGuildIdAndUserIdAndFinishTimeIsLessThan(Long guildId, Long userId, Instant now);

    List<Poll> findAllByGuildIdAndFinishTimeIsLessThan(Long guildId, Instant now);

    List<Poll> findAllByUserIdAndFinishTimeIsLessThan(Long userId, Instant now);

    Poll findByGuildIdAndMessageId(Long guildId, Long messageId) throws NoPollsFoundException;

    Optional<Poll> findByGuildIdAndTitle(Long guildId, String pollName);

    Poll getPoll(Long pollId) throws NoPollsFoundException, PollExpiredException;

    Poll getPoll(Guild guild, String pollName) throws NoPollsFoundException, PollExpiredException;

    boolean activePollExists(Guild guild, String pollTitle);

    void createPoll(Poll poll);

    Poll createPoll(Long guildId, Long userId, String title, Instant finishTime);

    void addItemToPoll(Long pollId, PollItems item) throws NoPollsFoundException;

    void setExpiredFlag(Long pollId);

    List<Poll> checkExpiredPollsForUserAndGuild(Guild guild, User user);

    List<Poll> checkExpiredPollsForGuild(Guild guild);

    List<Poll> checkExpiredPollsForUser(User user);

    List<Poll> findAllByGuildIdAndFinishTimeIsGreaterThan(Long guildId, Instant now);

    void checkExpiredPolls(JDA jda);

    void loadPollCache();

    Message getActivePollsForGuild(Guild guild);

    Message getPollByName(Guild guild, String pollTitle) throws NoPollsFoundException;

    Message getPollByName(Poll poll) throws NoPollsFoundException;

    Message buildPollMessage(Poll poll, TextChannel textChannel, Sia sia);

    void updatePollMessage(Poll poll, Message message, Sia sia);

    void cleanExpiredPolls (WebhookClient webhookClient);
}
