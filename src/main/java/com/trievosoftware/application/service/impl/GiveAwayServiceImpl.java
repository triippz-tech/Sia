package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.DiscordUser;
import com.trievosoftware.application.domain.GiveAway;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.repository.GiveAwayRepository;
import com.trievosoftware.application.service.GiveAwayService;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.FormatUtil;
import com.trievosoftware.discord.utils.OtherUtil;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.webhook.WebhookClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing GiveAway.
 */
@Service
@Transactional
public class GiveAwayServiceImpl implements GiveAwayService {

    private final Logger log = LoggerFactory.getLogger(GiveAwayServiceImpl.class);

    private final GiveAwayRepository giveAwayRepository;

    public GiveAwayServiceImpl(GiveAwayRepository giveAwayRepository) {
        this.giveAwayRepository = giveAwayRepository;
    }

    /**
     * Save a giveAway.
     *
     * @param giveAway the entity to save
     * @return the persisted entity
     */
    @Override
    public GiveAway save(GiveAway giveAway) {
        log.debug("Request to save GiveAway : {}", giveAway);
        return giveAwayRepository.save(giveAway);
    }

    /**
     * Get all the giveAways.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GiveAway> findAll(Pageable pageable) {
        log.debug("Request to get all GiveAways");
        return giveAwayRepository.findAll(pageable);
    }

    /**
     * Get all the GiveAway with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<GiveAway> findAllWithEagerRelationships(Pageable pageable) {
        return giveAwayRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one giveAway by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GiveAway> findOne(Long id) {
        log.debug("Request to get GiveAway : {}", id);
        return giveAwayRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the giveAway by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GiveAway : {}", id);
        giveAwayRepository.deleteById(id);
    }

    @Override
    public List<GiveAway> findAllByGuildsettingsAndExpired(GuildSettings guildSettings, Boolean isExpired)
    {
        log.debug("Request to get all active Give Aways for Guild={}", guildSettings.getGuildId());
        return giveAwayRepository.findAllByGuildsettingsAndExpired(guildSettings, isExpired);
    }

    @Override
    public GiveAway getGiveAway(GuildSettings guildSettings, Long messageId)
    {
        log.debug("Request to find Give Away for Guild");
        Optional<GiveAway> giveAway = giveAwayRepository.findByGuildsettingsAndMessageId(guildSettings, messageId);
        return giveAway.orElse(null);
    }

    @Override
    public GiveAway createGiveAway(String name, String message, Instant finish, GuildSettings guildSettings)
    {
        log.debug("Request to create non-persisted GiveAway");
        return new GiveAway(name, message, finish, guildSettings);
    }

    @Override
    public void enterVote(Sia sia, GiveAway giveAway, User user)
    {
        log.debug("Request to add entry to GiveAway={}", giveAway.getName());

        DiscordUser discordUser = sia.getServiceManagers().getDiscordUserService().getDiscordUser(user);
        if ( giveAway.getDiscordusers().contains(discordUser))
        {
            log.debug("User={} has already voted", user.getName());
            sia.getJDA(giveAway.getGuildsettings().getGuildId()).getUserById(user.getIdLong()).
                openPrivateChannel().complete().sendMessage("You have already voted on `" + giveAway.getName() + "`").queue();
            return;
        }

        giveAway.addDiscorduser(discordUser);
        save(giveAway);
        log.debug("User={} added to GiveAway={}", user.getName(), giveAway.getName());
    }

    @Override
    public void checkForExpiredGiveAways(JDA jda)
    {
        log.debug("Request to check for expired giveaways");

        List<GiveAway> giveAways = giveAwayRepository.findAllByFinishLessThan(Instant.now());

        for ( GiveAway giveAway : giveAways )
        {
            if ( !giveAway.isExpired() )
            {
                Guild guild = jda.getGuildById(giveAway.getGuildsettings().getGuildId());
                DiscordUser winner = OtherUtil.randomUser(giveAway.getDiscordusers());

                if ( winner == null ) {
                    Message message =
                        guild.getTextChannelById(giveAway.getTextChannelId()).getMessageById(giveAway.getMessageId()).complete();
                    message.delete().queue();

                    Message newMessage = FormatUtil.formatNoGiveAwayWinner(giveAway);
                    guild.getTextChannelById(giveAway.getTextChannelId()).sendMessage(newMessage).queue();

                    giveAway.setTextChannelId(0L);
                    giveAway.setExpired(true);
                    save(giveAway);
                }
                else
                {
                    User user = jda.getUserById(winner.getUserId());
                    Message message =
                        guild.getTextChannelById(giveAway.getTextChannelId()).getMessageById(giveAway.getMessageId()).complete();

                    Message newMessage = FormatUtil.formatGiveAwayWinner(giveAway, user);
                    guild.getTextChannelById(giveAway.getTextChannelId()).sendMessage(newMessage).queue();

                    message.delete().queue();
                    giveAway.setWinner(winner.getUserId());
                    giveAway.setTextChannelId(0L);
                    giveAway.setExpired(true);
                    save(giveAway);
                }
            }
        }
    }

    @Override
    public void cleanExpiredGiveAways(Sia sia)
    {
        log.debug("Request to clean expired Polls older than 30 days");

        Instant pastDate = Instant.now().minus(Duration.ofHours(1));
        List<GiveAway> giveAways = giveAwayRepository.findAllByFinishLessThan(pastDate);

        if ( giveAways.isEmpty() )
        {
            log.info("No expired GiveAways older than 30 days.");
            sia.getLogWebhook().send("No expired GiveAways older than 30 days.");
            return;
        }

        for ( GiveAway giveAway : giveAways )
        {
            if ( giveAway.isExpired() )
            {
                delete(giveAway.getId());
                Guild guild =
                    sia.getJDA(giveAway.getGuildsettings().getGuildId()).getGuildById(giveAway.getGuildsettings().getGuildId());
                GuildSettings guildSettings =
                    sia.getServiceManagers().getGuildSettingsService().getSettings(guild);
                guildSettings.removeGiveaway(giveAway);
                sia.getServiceManagers().getGuildSettingsService().save(guildSettings);
            }
        }

        log.info("GiveAways cleanup complete, removed: {} GiveAways older than 30 days", giveAways.size());
        sia.getLogWebhook().send("Removed `" + giveAways.size() + "` GiveAways older than 30 days.");
    }

}
