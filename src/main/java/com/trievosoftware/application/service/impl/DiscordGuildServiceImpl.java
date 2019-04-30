package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.service.DiscordGuildService;
import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.repository.DiscordGuildRepository;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.Pair;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DiscordGuild.
 */
@Service
@Transactional
public class DiscordGuildServiceImpl implements DiscordGuildService {

    private final Logger log = LoggerFactory.getLogger(DiscordGuildServiceImpl.class);

    private final DiscordGuildRepository discordGuildRepository;

    public DiscordGuildServiceImpl(DiscordGuildRepository discordGuildRepository) {
        this.discordGuildRepository = discordGuildRepository;
    }

    /**
     * Save a discordGuild.
     *
     * @param discordGuild the entity to save
     * @return the persisted entity
     */
    @Override
    public DiscordGuild save(DiscordGuild discordGuild) {
        log.debug("Request to save DiscordGuild : {}", discordGuild);
        return discordGuildRepository.save(discordGuild);
    }

    /**
     * Get all the discordGuilds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DiscordGuild> findAll(Pageable pageable) {
        log.debug("Request to get all DiscordGuilds");
        return discordGuildRepository.findAll(pageable);
    }


    /**
     * Get one discordGuild by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DiscordGuild> findOne(Long id) {
        log.debug("Request to get DiscordGuild : {}", id);
        return discordGuildRepository.findById(id);
    }

    /**
     * Delete the discordGuild by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DiscordGuild : {}", id);
        discordGuildRepository.deleteById(id);
    }


    @Override
    public Pair<Boolean, DiscordGuild> discordGuildExists(Guild guild)
    {
        Optional<DiscordGuild> discordGuild = discordGuildRepository.findByGuildId(guild.getIdLong());
        if( discordGuild.isPresent()) return new Pair<>(true, discordGuild.get());

        DiscordGuild discordGuildNew = getDiscordGuild(guild);
        return new Pair<>(false, discordGuildNew);
    }

    @Override
    public DiscordGuild getDiscordGuild(Guild guild)
    {
        log.debug("Request to get DiscordGuild for Guild={}", guild.getName());

        Optional<DiscordGuild> discordGuild = discordGuildRepository.findByGuildId(guild.getIdLong());

        if ( discordGuild.isPresent() ) return discordGuild.get();

        String inviteLink;
        try {
            inviteLink = guild.getDefaultChannel().createInvite().setTemporary(false).complete().getURL();
        } catch (NullPointerException e)
        {
            inviteLink = "";
        }
        DiscordGuild discordGuildNew = new DiscordGuild(
            guild.getIdLong(),
            guild.getName(),
            inviteLink);

        save(discordGuildNew);
        return discordGuildNew;
    }

    @Override
    public void updateDiscordGuilds(Sia sia)
    {
        log.debug("Request to update all Discord Guilds for startup");

        Integer updated = 0;
        for ( Guild guild : sia.getShardManager().getGuilds() )
        {
            // check if the guild has settings
            DiscordGuild discordGuild = getDiscordGuild(guild);

            if ( discordGuild.getGuildSettings() == null ) {
                discordGuild.setGuildSettings(sia.getServiceManagers().getGuildSettingsService()
                    .getGuildSettings(discordGuild));
                save(discordGuild);
            }

            // make sure the server name is still the same
            if ( !guild.getName().equalsIgnoreCase(discordGuild.getGuildName()) )
            {
                discordGuild.setGuildName(guild.getName());
                updated++;
            }

            log.info("Updated {} Guild(s)", updated);
        }
    }
}
