package com.trievosoftware.application.service.impl;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.trievosoftware.application.service.GuildSettingsService;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.repository.GuildSettingsRepository;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.utils.FixedCache;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing GuildSettings.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class GuildSettingsServiceImpl implements GuildSettingsService, GuildSettingsManager {

    private final Logger log = LoggerFactory.getLogger(GuildSettingsServiceImpl.class);

    private final GuildSettingsRepository guildSettingsRepository;

    public final static int PREFIX_MAX_LENGTH = 40;
    private static final String SETTINGS_TITLE = "\uD83D\uDCCA Server Settings"; // 📊
    private static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("GMT-4");
    private final FixedCache<Long, GuildSettings> cache = new FixedCache<>(1000);

    public GuildSettingsServiceImpl(GuildSettingsRepository guildSettingsRepository) {
        this.guildSettingsRepository = guildSettingsRepository;
    }

    /**
     * Save a guildSettings.
     *
     * @param guildSettings the entity to save
     * @return the persisted entity
     */
    @Override
    public GuildSettings save(GuildSettings guildSettings) {
        log.debug("Request to save GuildSettings : {}", guildSettings);
        return guildSettingsRepository.save(guildSettings);
    }

    /**
     * Get all the guildSettings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GuildSettings> findAll() {
        log.debug("Request to get all GuildSettings");
        return guildSettingsRepository.findAll();
    }


    /**
     * Get one guildSettings by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GuildSettings> findOne(Long id) {
        log.debug("Request to get GuildSettings : {}", id);
        return guildSettingsRepository.findById(id);
    }

    /**
     * Delete the guildSettings by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GuildSettings : {}", id);
        guildSettingsRepository.deleteById(id);
    }

    /**
     * Get one guildSettings by id.
     *
     * @param guildId the id of the entity
     * @return the entity
     */
    @Override
    public Optional<GuildSettings> findByGuildId(Long guildId) {
        log.debug("Request to get GuildSettings for Guild: {}", guildId);
        return guildSettingsRepository.findByGuildId(guildId);
    }

    @Override
    public GuildSettings getSettings(Guild guild)
    {
        if(cache.contains(guild.getIdLong()))
            return cache.get(guild.getIdLong());
        GuildSettings settings;
        Optional<GuildSettings> tempSettings = findByGuildId(guild.getIdLong());
        settings = tempSettings.orElseGet(GuildSettings::new);
        cache.put(guild.getIdLong(), settings);
        return settings;
    }

    private void invalidateCache(Guild guild)
    {
        invalidateCache(guild.getIdLong());
    }

    private void invalidateCache(long guildId)
    {
        cache.pull(guildId);
    }

    @Override
    public boolean hasSettings(Guild guild)
    {
        return findByGuildId(guild.getIdLong()).isPresent();
    }

    @Override
    public MessageEmbed.Field getSettingsDisplay(Guild guild)
    {
        GuildSettings settings = getSettings(guild);
        TextChannel modlog = settings.getModLogChannel(guild);
        TextChannel serverlog = settings.getServerLogChannel(guild);
        TextChannel messagelog = settings.getMessageLogChannel(guild);
        TextChannel voicelog = settings.getVoiceLogChannel(guild);
        TextChannel avylog = settings.getAvatarLogChannel(guild);
        Role modrole = settings.getModeratorRole(guild);
        Role muterole = settings.getMutedRole(guild);
        return new MessageEmbed.Field(SETTINGS_TITLE, "Prefix: `"+(settings.getPrefix()==null ? Constants.PREFIX : settings.getPrefix())+"`"
            + "\nMod Role: "+(modrole==null ? "None" : modrole.getAsMention())
            + "\nMuted Role: "+(muterole==null ? "None" : muterole.getAsMention())
            + "\nMod Log: "+(modlog==null ? "None" : modlog.getAsMention())
            + "\nMessage Log: "+(messagelog==null ? "None" : messagelog.getAsMention())
            + "\nVoice Log: "+(voicelog==null ? "None" : voicelog.getAsMention())
            + "\nAvatar Log: "+(avylog==null ? "None" : avylog.getAsMention())
            + "\nServer Log: "+(serverlog==null ? "None" : serverlog.getAsMention())
            + "\nTZone: **"+settings.getTimezone()+"**\n\u200B", true);
    }

    @Override
    public void setModLogChannel(Guild guild, TextChannel tc)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setModLogId(tc==null ? 0L : tc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set mod log channel={} for guild = {}", tc.getName(), guild.getIdLong());
        }
    }

    @Override
    public void setServerLogChannel(Guild guild, TextChannel tc)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setServerLogId(tc==null ? 0L : tc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set server log channel={} for guild = {}", tc.getName(), guild.getIdLong());
        }
    }

    @Override
    public void setMessageLogChannel(Guild guild, TextChannel tc)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setMessageLogId(tc==null ? 0L : tc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set message log channel={} for guild = {}", tc.getName(), guild.getIdLong());
        }
    }

    @Override
    public void setVoiceLogChannel(Guild guild, TextChannel tc)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setVoiceLogId(tc==null ? 0L : tc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set voice log channel={} for guild = {}", tc.getName(), guild.getIdLong());
        }
    }

    @Override
    public void setAvatarLogChannel(Guild guild, TextChannel tc)
    {
        setAvatarLogChannel(guild.getIdLong(), tc);
    }

    @Override
    public void setAvatarLogChannel(long guildId, TextChannel tc)
    {
        invalidateCache(guildId);
        Optional<GuildSettings> settings = findByGuildId(guildId);
        if ( settings.isPresent() ) {
            settings.get().setAvatarLogId(tc==null ? 0L : tc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set avatar log channel={} for guild = {}", tc.getName(), guildId);
        }
    }

    @Override
    public void setModeratorRole(Guild guild, Role role)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setModRoleId(role==null ? 0L : role.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set moderator role={} for guild = {}", role.getName(), guild.getIdLong());
        }
    }

    @Override
    public void setPrefix(Guild guild, String prefix)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setPrefix(prefix);
            save(settings.get());
        } else {
            log.error("Unable to set prefix={} for guild = {}", prefix, guild.getIdLong());
        }
    }

    @Override
    public void setTimezone(Guild guild, ZoneId zone)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setTimezone(zone.getId());
            save(settings.get());
        } else {
            log.error("Unable to set timezone={} for guild = {}", zone.getId(), guild.getIdLong());
        }
    }

    @Override
    public void enableRaidMode(Guild guild)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setRaidMode(guild.getVerificationLevel().getKey());
            save(settings.get());
        } else {
            log.error("Unable to enable raid mode={} for guild = {}", guild.getVerificationLevel().getKey(), guild.getIdLong());
        }
    }

    @Override
    public Guild.VerificationLevel disableRaidMode(Guild guild)
    {
        invalidateCache(guild);
        Optional<GuildSettings> settings = findByGuildId(guild.getIdLong());
        Guild.VerificationLevel old = null;
        if ( settings.isPresent() ) {
            old = Guild.VerificationLevel.fromKey(settings.get().getRaidMode());
            settings.get().setRaidMode(-2);
            save(settings.get());
        } else {
            log.error("Unable to disable raid mode={} for guild = {}", guild.getVerificationLevel().getKey(), guild.getIdLong());
        }
        return old;
    }
}
