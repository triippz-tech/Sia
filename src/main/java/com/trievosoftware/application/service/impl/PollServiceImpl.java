package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.DiscordUser;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.exceptions.NoPollsFoundException;
import com.trievosoftware.application.exceptions.PollExpiredException;
import com.trievosoftware.application.service.PollService;
import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.repository.PollRepository;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.FixedCache;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.webhook.WebhookClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Poll.
 */
@Service
@Transactional
public class PollServiceImpl implements PollService {

    private final Logger log = LoggerFactory.getLogger(PollServiceImpl.class);

    // So we dont have to query the database for each request
    private final FixedCache<Long, Poll> cache = new FixedCache<>(1000);

    private final PollRepository pollRepository;

    public PollServiceImpl(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    /**
     * Save a poll.
     *
     * @param poll the entity to save
     * @return the persisted entity
     */
    @Override
    public Poll save(Poll poll) {
        log.debug("Request to save Poll : {}", poll);
        return pollRepository.save(poll);
    }

    /**
     * Get all the polls.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Poll> findAll() {
        log.debug("Request to get all Polls");
        return pollRepository.findAll();
    }


    /**
     * Get one poll by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Poll> findOne(Long id) {
        log.debug("Request to get Poll : {}", id);
        return pollRepository.findById(id);
    }

    /**
     * Delete the poll by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Poll : {}", id);
        pollRepository.deleteById(id);
    }

    @Override
    public List<Poll> findAllByGuildIdAndUserId(Long guildId, Long userId)
    {
        log.debug("Request to find all Polls for User={} in Guild={}", userId, guildId);
        return pollRepository.findAllByGuildIdAndUserId(guildId, userId);
    }


    @Override
    public List<Poll> findAllByGuildId(Long guildId)
    {
        log.debug("Request to find all Polls for Guild={}", guildId);
        return pollRepository.findAllByGuildId(guildId);
    }


    @Override
    public List<Poll> findAllByUserId(Long userId)
    {
        log.debug("Request to find all Polls for User={}", userId);
        return pollRepository.findAllByUserId(userId);
    }


    @Override
    public Optional<Poll> findByGuildIdAndUserIdAndTitle(Long guildId, Long userId, String title)
    {
        log.debug("Request to find Poll={} for User={} in Guild={}", title, userId, guildId);
        return pollRepository.findByGuildIdAndUserIdAndTitle(guildId, userId, title);
    }


    @Override
    public List<Poll> findAllByFinishTimeIsLessThan(Instant now)
    {
        log.debug("Request to find all expired polls");
        return pollRepository.findAllByFinishTimeIsLessThan(now);
    }


    @Override
    public List<Poll> findAllByGuildIdAndUserIdAndFinishTimeIsLessThan(Long guildId, Long userId, Instant now)
    {
        log.debug("Request to find expired Polls for User={} in Guild={}", userId, guildId);
        return pollRepository.findAllByGuildIdAndUserIdAndFinishTimeIsLessThan(guildId, userId, now);
    }


    @Override
    public List<Poll> findAllByGuildIdAndFinishTimeIsLessThan(Long guildId, Instant now)
    {
        log.debug("Request to find all expired Polls for Guild={}");
        return pollRepository.findAllByGuildIdAndFinishTimeIsLessThan(guildId, now);
    }


    @Override
    public List<Poll> findAllByUserIdAndFinishTimeIsLessThan(Long userId, Instant now)
    {
        log.debug("Request to find all expired Polls for User={}", userId);
        return pollRepository.findAllByUserIdAndFinishTimeIsLessThan(userId, now);
    }

    @Override
    public Poll findByGuildIdAndMessageId(Long guildId, Long messageId) throws NoPollsFoundException {
        log.debug("Request to get Poll from Message={} in Guild={}", messageId, guildId);

        Optional<Poll> poll = pollRepository.findByGuildIdAndMessageId(guildId, messageId);

        if ( !poll.isPresent() ) throw new NoPollsFoundException("Could not find Poll from Message=" + messageId);
        else return poll.get();
    }

    @Override
    public Optional<Poll> findByGuildIdAndTitle(Long guildId, String pollName)
    {
        log.debug("Request to find Poll={} for Guild={}", pollName, guildId);
        return pollRepository.findByGuildIdAndTitle(guildId, pollName);
    }

    @Override
    public Poll getPoll(Long pollId) throws NoPollsFoundException, PollExpiredException {
        log.debug("Request to get Poll={}", pollId);
        Optional<Poll> poll = findOne(pollId);

        if ( !poll.isPresent() ) throw  new NoPollsFoundException("No Poll was found");
        if ( poll.get().isExpired() ) throw new PollExpiredException("This poll has expired.");
        else return poll.get();
    }

    @Override
    public Poll getPoll(Guild guild, String pollName) throws NoPollsFoundException, PollExpiredException {
        log.debug("Request to get Poll={}", pollName);
        Optional<Poll> poll = findByGuildIdAndTitle(guild.getIdLong(), pollName);

        if ( !poll.isPresent() ) throw  new NoPollsFoundException("No Poll was found");
        if ( poll.get().isExpired() ) throw new PollExpiredException("This poll has expired.");
        else return poll.get();
    }


    @Override
    public boolean activePollExists(Guild guild, String pollTitle)
    {
        log.debug("Request to see if Poll={} exists for Guild={}", guild.getName(), pollTitle);
        Optional<Poll> poll = findByGuildIdAndTitle(guild.getIdLong(), pollTitle);
        return poll.isPresent();
    }

    @Override
    public void createPoll(Poll poll)
    {
        log.debug("Request to create new Poll={}", poll.getTitle());
        save(poll);
    }

    @Override
    public Poll createPoll(Long guildId, Long userId, String title, Instant finishTime)
    {
        log.debug("Request to create new Poll={} for Guild={}", title, guildId);
        Poll poll = new Poll(guildId, userId, title, finishTime);
        save(poll);
        log.debug("Poll={} created for Guild={}", title, guildId);
        return poll;
    }

    @Override
    public void addItemToPoll(Long pollId, PollItems item) throws NoPollsFoundException {
        log.debug("Request to add PollItem={} to Poll={}", item.getItemName(), pollId);
        Optional<Poll> poll = findOne(pollId);
        if ( !poll.isPresent() ) throw new NoPollsFoundException("I was unable to find that poll.");

        poll.get().addPollitems(item);
        save(poll.get());
        log.debug("PollItem={} added to Poll={}", item.getItemName(), poll.get().getTitle());
    }

    @Override
    public void setExpiredFlag(Long pollId)
    {
        log.debug("Request to set expired flag for Poll={}", pollId);
        Optional<Poll> poll = findOne(pollId);

        if ( poll.isPresent() )
        {
            poll.get().setExpired(true);
            pollRepository.saveAndFlush(poll.get());
        }
    }


    @Override
    public List<Poll> checkExpiredPollsForUserAndGuild(Guild guild, User user)
    {
        log.debug("Request to get all expired Polls for User={} in Guild={}", user.getName(), guild.getName());
        return findAllByGuildIdAndUserIdAndFinishTimeIsLessThan(guild.getIdLong(), user.getIdLong(), Instant.now());
    }

    @Override
    public List<Poll> checkExpiredPollsForGuild(Guild guild)
    {
        log.debug("Request to get all expired polls for Guild={}", guild.getName());
        return findAllByGuildIdAndFinishTimeIsLessThan(guild.getIdLong(), Instant.now());
    }


    @Override
    public List<Poll> checkExpiredPollsForUser(User user)
    {
        log.debug("Request to get all expired polls for User={}", user.getName());
        return pollRepository.findAllByUserIdAndFinishTimeIsLessThan(user.getIdLong(), Instant.now());
    }

    @Override
    public List<Poll> findAllByGuildIdAndFinishTimeIsGreaterThan(Long guildId, Instant now)
    {
        log.debug("Request to get all active polls for Guild={}", guildId);
        return pollRepository.findAllByGuildIdAndFinishTimeIsGreaterThan(guildId, now);
    }

    @Override
    @Transactional
    public void addUserVoted(Long pollId, DiscordUser user) throws NoPollsFoundException {
        log.debug("Request to add User={} to voted DiscordUsers", user.getId());
        Optional<Poll> poll = findOne(pollId);
        if ( !poll.isPresent() ) throw new NoPollsFoundException("Poll was not found, cannot add user");

        poll.get().addDiscorduser(user);
        save(poll.get());
    }

    @Override
    @Transactional
    public void removeUserVoted(Long pollId, DiscordUser user) throws NoPollsFoundException {
        log.debug("Request to add User={} to voted DiscordUsers", user.getId());
        Optional<Poll> poll = findOne(pollId);
        if ( !poll.isPresent() ) throw new NoPollsFoundException("Poll was not found, cannot add user");

        poll.get().removeDiscorduser(user);
        save(poll.get());
    }

    /**
     * Checks for expired Polls. If one or more are found. Sia will attempt to notify users in the follwing format:
     *      1. The text channel assigned (if one was)
     *      2. The Guild's (which the poll was created in) default channel
     *      3. The Guild's owner in a private channel, to notify the Owner to let users know
     * @param jda
     */
    @Override
    public void checkExpiredPolls(JDA jda)
    {
        log.debug("Request to check for expired Polls");
        List<Poll> polls = findAllByFinishTimeIsLessThan(Instant.now());

        for ( Poll poll : polls )
        {
            if ( !poll.isExpired() ) {
                Guild guild = jda.getGuildById(poll.getGuildId());

                // First determine we have a set Text Channel to log to
                if (poll.getTextChannelId() == 0L)
                    // Attempt to send to a default channel
                    try {
                        guild.getDefaultChannel().sendMessage(FormatUtil.formatPollComplete(jda, poll)).queue();
                    } catch (NullPointerException e) {
                        // if No default found, send message in private channel to the owner
                        guild.getOwner().getUser().openPrivateChannel().complete().sendMessage(FormatUtil.formatPollCompleteDirect(jda, poll)).queue();
                    }
                else
                    guild.getTextChannelById(poll.getTextChannelId()).sendMessage(FormatUtil.formatPollComplete(jda, poll)).queue();

                log.info("Poll={} completed, message sent", poll.getTitle());

                // now set the expired flag so we dont notify anyone again
                setExpiredFlag(poll.getId());

                // Remove it from the cache
                invalidateCache(poll.getMessageId());

                // delete the message
                jda.getGuildById(poll.getGuildId()).getTextChannelById(poll.getTextChannelId())
                    .getMessageById(poll.getMessageId()).complete().delete().complete();
            }
        }
    }


    @Override
    public void loadPollCache()
    {
        log.debug("Request to load Poll Cache");

        List<Poll> polls = pollRepository.findAllByFinishTimeIsGreaterThan(Instant.now());

        if ( polls.isEmpty() ) {
            log.info("No active Polls were found in database.");
            return;
        }

        for ( Poll poll : polls )
            cache.put(poll.getTextChannelId(), poll);

        log.info("{} Polls were loaded into PollCache", polls.size());
    }

    /**
     * Returns a Message of all active Polls for a GUild
     * @param guild the Guild object
     * @return Message
     */
    @Override
    public Message getActivePollsForGuild(Guild guild) {
        log.debug("Request to check for active Polls for Guild={}", guild.getName());
        List<Poll> polls = findAllByGuildIdAndFinishTimeIsGreaterThan(guild.getIdLong(), Instant.now());

        MessageBuilder builder = new MessageBuilder();
        builder.setContent("**Active Polls for `" + guild.getName() + "`**");
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (polls.isEmpty())
        {
            embedBuilder.addField(
                "",
                "** No Active Polls for this Guild**",
                false
            );
        }
        else {
            for (Poll poll : polls) {
                embedBuilder.addField(
                    "**" + poll.getTitle() + "**",
                    FormatUtil.formatPoll(poll),
                    false
                );
            }
        }

        builder.setEmbed(embedBuilder.build());
        return builder.build();
    }

    @Override
    public Message getPollByName(Guild guild, String pollTitle) throws NoPollsFoundException {
        log.debug("Request to get Poll={} for Guild={}", pollTitle, guild.getName());

        Optional<Poll> poll = findByGuildIdAndTitle(guild.getIdLong(), pollTitle);

        if ( !poll.isPresent() ) throw new NoPollsFoundException("No poll exists with that name.");

        MessageBuilder builder = new MessageBuilder();

        if ( poll.get().isPollActive() ) {
            builder.setContent("`**" + poll.get().getTitle() + "`** (Active)");
            builder.setEmbed(new EmbedBuilder()
                .addField(
                    "",
                    FormatUtil.formatPoll(poll.get()),
                    false)
                .build());
        }
        else {
            return FormatUtil.formatPollComplete(guild, poll.get());
        }
        return builder.build();
    }

    @Override
    public Message getPollByName(Poll poll) {
        log.debug("Request to get Poll={} for Guild={}", poll.getTitle(), poll.getGuildId());

        MessageBuilder builder = new MessageBuilder();

        if ( poll.isPollActive() ) {
            builder.setContent("`**" + poll.getTitle() + "`** (Active)");
            builder.setEmbed(new EmbedBuilder()
                .addField(
                    "",
                    FormatUtil.formatPoll(poll),
                    false)
                .build());
        }
        else {
            return FormatUtil.formatPollComplete(poll);
        }
        return builder.build();
    }

    @Override
    public Message buildPollMessage(Poll poll, TextChannel textChannel, Sia sia)
    {
        log.debug("Request to build Message for Poll={}", poll.getTitle());

        if ( !poll.isPollActive() ) return getPollByName(poll);

        MessageBuilder builder = new MessageBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for ( PollItems item : poll.getPollitems() )
        {
            item.setReaction(Constants.NUMBERS[i]);
            stringBuilder.append(" ").append(Constants.NUMBERS[i]).append(" ").append(item.getItemName())
                .append(": `[").append(item.getVotes()).append("]`").append("\n");
            i++;
        }

        embedBuilder.addField(
            Constants.POLL_EMOJI + " `" + poll.getTitle() + "` " + Constants.POLL_EMOJI,
            stringBuilder.toString(),
            false);
        embedBuilder.setFooter("To vote from a different channel type: `/poll vote add " + poll.getTitle()
            + " | <choice number>`", textChannel.getGuild().getIconUrl());
        builder.setEmbed(embedBuilder.build());

        Message message = textChannel.sendMessage(builder.build()).complete();

//        for ( int x = 0; x < i; x++ )
//        {
//            message.addReaction(Constants.NUMBERS[x]).submit();
//        }

        poll.setMessageId(message.getIdLong());
        poll.setTextChannelId(textChannel.getIdLong());
        save(poll);
        for ( PollItems item: poll.getPollitems() )
        {
            sia.getServiceManagers().getPollItemsService().save(item);
        }
        cache.put(message.getIdLong(), poll);

        return message;
    }

    @Override
    public void updatePollMessage(Poll poll, Message message, Sia sia)
    {
        log.debug("Request to update Poll Message={}", message.getIdLong());

        if ( !poll.isPollActive() ) return;

        invalidateCache(message.getIdLong());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        MessageBuilder builder = new MessageBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        for ( PollItems item : poll.getPollitems() )
        {
            stringBuilder.append(" ").append(item.getReaction()).append(" ").append(item.getItemName())
                .append(": `[").append(item.getVotes()).append("]`").append("\n");
        }

        embedBuilder.addField(
            Constants.POLL_EMOJI + " `" + poll.getTitle() + "` " + Constants.POLL_EMOJI,
            stringBuilder.toString(),
            false);
        embedBuilder.setFooter("To vote from a different channel type: `/poll vote add " + poll.getTitle()
            + " | <choice number>`", message.getGuild().getIconUrl());
        message.editMessage(builder.setEmbed(embedBuilder.build()).build()).complete();

        for ( PollItems item : poll.getPollitems() )
//            message.addReaction(item.getReaction()).submit();

        save(poll);
        cache.put(message.getIdLong(), poll);
        message.pin().submit();
    }


    @Override
    public void cleanExpiredPolls(WebhookClient webhookClient)
    {
        log.debug("Request to clean expired Polls older than 30 days");

        Instant pastDate = Instant.now().minus(Duration.ofHours(1));
        List<Poll> polls = findAllByFinishTimeIsLessThan(pastDate);

        if ( polls.isEmpty() )
        {
            log.info("No expired polls older than 30 days.");
            webhookClient.send("No expired Polls older than 30 days.");
            return;
        }

        for ( Poll poll : polls )
        {
            if ( poll.isExpired() )
            {
                delete(poll.getId());

                // just ensure cache is cleared too
                if (cache.contains(poll.getMessageId())) invalidateCache(poll.getMessageId());
            }
        }

        log.info("Poll cleanup complete, removed: {} Polls older than 30 days", polls.size());
        webhookClient.send("Removed `" + polls.size() + "` Polls older than 30 days.");
    }

    private void invalidateCache(Message message)
    {
        invalidateCache(message.getIdLong());
    }

    private void invalidateCache(long messageId)
    {
        log.debug("Request to pull Poll={} from cache", messageId);
        cache.pull(messageId);
    }
}
