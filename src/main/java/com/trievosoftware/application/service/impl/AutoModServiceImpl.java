package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.service.AutoModService;
import com.trievosoftware.application.domain.AutoMod;
import com.trievosoftware.application.repository.AutoModRepository;
import com.trievosoftware.discord.Action;
import com.trievosoftware.discord.utils.FixedCache;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing AutoMod.
 */
@Service
@Transactional
public class AutoModServiceImpl implements AutoModService {

    private final Logger log = LoggerFactory.getLogger(AutoModServiceImpl.class);

    private final AutoModRepository autoModRepository;

    public final static int MAX_STRIKES = 100;
    public final static int MENTION_MINIMUM = 4;
    public final static int ROLE_MENTION_MINIMUM = 2;
    private static final String SETTINGS_TITLE = "\uD83D\uDEE1 Automod Settings"; // 🛡

    private final FixedCache<Long, AutoMod> autoModCache = new FixedCache<>(1000);


    public AutoModServiceImpl(AutoModRepository autoModRepository) {
        this.autoModRepository = autoModRepository;
    }

    /**
     * Save a autoMod.
     *
     * @param autoMod the entity to save
     * @return the persisted entity
     */
    @Override
    public AutoMod save(AutoMod autoMod) {
        log.debug("Request to save AutoMod : {}", autoMod);
        return autoModRepository.save(autoMod);
    }

    /**
     * Get all the autoMods.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AutoMod> findAll() {
        log.debug("Request to get all AutoMods");
        return autoModRepository.findAll();
    }


    /**
     * Get one autoMod by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AutoMod> findOne(Long id) {
        log.debug("Request to get AutoMod : {}", id);
        return autoModRepository.findById(id);
    }

    /**
     * Delete the autoMod by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AutoMod : {}", id);
        autoModRepository.deleteById(id);
    }


    /**
     * Get one autoMod by guildId.
     *
     * @param guildId the of the entity
     * @return the entity
     */
    @Override
    public Optional<AutoMod> findByGuildId(Long guildId) {
        log.debug("Request to get AutoMod : {}", guildId);
        return autoModRepository.findByGuildId(guildId);
    }

    /**
     * Get the guild's settings by their guild
     *
     * @param guild Guild objeect
     * @return AutoMod settings
     */
    @Override
    public AutoMod getSettings(Guild guild)
    {
        if(autoModCache.contains(guild.getIdLong()))
            return autoModCache.get(guild.getIdLong());

        Optional<AutoMod> tempSettings = findByGuildId(guild.getIdLong());
        if ( tempSettings.isPresent() ) {
            autoModCache.put(guild.getIdLong(), tempSettings.get());
            return tempSettings.get();
        } else {
            AutoMod settings = new AutoMod();
            settings.setDefaults(guild.getIdLong());
            autoModCache.put(guild.getIdLong(), settings);
            save(settings);
            return settings;
        }
    }

    private void invalidateCache(Guild guild)
    {
        invalidateCache(guild.getIdLong());
    }

    private void invalidateCache(long guildId)
    {
        autoModCache.pull(guildId);
    }

    @Override
    public boolean hasSettings(Guild guild)
    {
        return findByGuildId(guild.getIdLong()).isPresent();
    }

    /**
     * Get the settings embed display of a guild
     *
     * @param guild guild object
     * @return Field object for embeds
     */
    @Override
    @SuppressWarnings("Duplicates")
    public MessageEmbed.Field getSettingsDisplay(Guild guild)
    {
        AutoMod settings = getSettings(guild);
        return new MessageEmbed.Field(SETTINGS_TITLE,
            "__Anti-Advertisement__\n" + (settings.getInviteStrikes()==0 && settings.getRefStrikes()==0
                    ? "Disabled\n\n"
                    : "Invite Links: `" + settings.getInviteStrikes() + " " + Action.STRIKE.getEmoji() + "`\n" +
                "Referral Links: `" + settings.getRefStrikes() + " " + Action.STRIKE.getEmoji() + "`\n" +
                "Resolve Links: `" + (settings.isResolveUrls()
                    ? "ON"
                    : "OFF") + "`\n\n")
                + "__Anti-Duplicate__\n" + (settings.useAntiDuplicate()
                    ? "Strike Threshold: `" + settings.getDupeStrikesThresh() + "`\n" +
                        "Delete Threshold: `" + settings.getDupeDeleteThresh() + "`\n" +
                        "Strikes: `" + settings.getDupeStrikes() + " " + Action.STRIKE.getEmoji() + "`\n\n"
                    : "Disabled\n\n")
                + "__Maximum Mentions__\n" + (settings.getMaxMentions()==0 && settings.getMaxRoleMentions()==0
                    ? "Disabled\n\n"
                    : "User Mentions: " + (settings.getMaxMentions()==0 ? "None\n" : "`" + settings.getMaxMentions() + "`\n") +
                "Role Mentions: " + (settings.getMaxRoleMentions()==0 ? "None\n\n" : "`" + settings.getMaxRoleMentions() + "`\n\n"))
                + "__Misc Msg Settings__\n" + (settings.getMaxLines()==0 && settings.getCopyPastaStrikes()==0 && settings.getEveryoneStrikes()==0
                    ? "Disabled\n\n"
                    : "Max Lines / Msg: "+(settings.getMaxLines()==0 ? "Disabled\n" : "`"+settings.getMaxLines()+"`\n") +
                "Copypasta: `" + settings.getCopyPastaStrikes() + " " + Action.STRIKE.getEmoji() + "`\n" +
                "@\u0435very1 Attempt: `" + settings.getEveryoneStrikes() + " " + Action.STRIKE.getEmoji() + "`\n\n") // cyrillic e
                + "__Miscellaneous__\n"
                + "Auto AntiRaid: " + (settings.useAutoRaidMode()
                    ? "`" + settings.getRaidModeNumber() + "` joins/`" + settings.getRaidModeTime() + "`s\n"
                    : "Disabled\n")
                + "Auto Dehoist: " + (settings.getDehoistChar()==(char)0
                    ? "Disabled"
                    : "`"+settings.getDehoistChar()+"` and above")
            /*+ "\u200B"*/, true);
    }

    /**
     * Updates the AutoMod Settings of a given guild, by updating the disableMaxMentions,
     * Also updates the cache
     *
     * @param guild the guild object
     */
    @Override
    public void disableMaxMentions(Guild guild)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setMaxMentions(0);
            settings.get().setMaxRoleMentions(0);
            save(settings.get());
        } else {
            log.error("Unable to disable max mentions for guild = {}", guild.getIdLong());
        }
    }

    /**
     *
     * @param guild
     * @param value
     */
    @Override
    public void setResolveUrls(Guild guild, boolean value)
    {
        setResolveUrls(guild.getIdLong(), value);
    }

    /**
     * Sets the resolve urls flag for a guild. If no AutoMod object found, log error, throw exception.
     *
     * @param guildId the discord ID of the guild
     * @param value flag value to set
     */
    @Override
    public void setResolveUrls(long guildId, boolean value)
    {
        invalidateCache(guildId);
        Optional<AutoMod> settings = findByGuildId(guildId);
        if ( settings.isPresent() ) {
            settings.get().setResolveUrls(value);
            save(settings.get());
        } else {
            log.error("Unable to set resolve urls VALUE={} for guild = {}. AutoMod settings not found.", value, guildId);
        }
    }

    /**
     * Sets the max mentions of a guild. If no AutoMod found, log error throw exception
     * @param guild the discord guild object
     * @param max value to set
     */
    @Override
    public void setMaxMentions(Guild guild, int max)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setMaxMentions(max);
            save(settings.get());
        } else {
            log.error("Unable to set max mentions VALUE={} for guild = {}. AutoMod settings not found.", max, guild.getName());
        }
    }

    /**
     * Sets the max role mentions of a guild. If no AutoMod found, log error, and throw exception
     * @param guild the discord guild object
     * @param max value to set
     */
    @Override
    public void setMaxRoleMentions(Guild guild, int max)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setMaxRoleMentions(max);
            save(settings.get());
        } else {
            log.error("Unable to set max role mentions VALUE={} for guild = {}. AutoMod settings not found.", max, guild.getName());
        }
    }

    /**
     * Sets the max lines of a guild. If no AutoMod found, log error, and throw exception
     * @param guild the discord guild object
     * @param max value to set
     */
    @Override
    public void setMaxLines(Guild guild, int max)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setMaxLines(max);
            save(settings.get());
        } else {
            log.error("Unable to set max lines VALUE={} for guild = {}. AutoMod settings not found.", max, guild.getName());
        }
    }

    /**
     * Sets the auto raid mode of a guild. If no AutoMod found, log error, and throw exception
     * @param guild the discord guild object
     * @param number
     * @param time
     */
    @Override
    public void setAutoRaidMode(Guild guild, int number, int time)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setRaidModeNumber(number);
            settings.get().setRaidModeTime(time);
            save(settings.get());
        } else {
            log.error("Unable to set auto raid mode number={} time={} for guild = {}. AutoMod settings not found.",
                number, time, guild.getName());
        }
    }

    /**
     * Sets the invite strikes for a guild. If no AutoMod found, log error, throw exception
     * @param guild
     * @param strikes
     */
    @Override
    public void setInviteStrikes(Guild guild, int strikes)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setInviteStrikes(strikes);
            save(settings.get());
        } else {
            log.error("Unable to set invite strikes VALUE={} for guild = {}. AutoMod settings not found.", strikes, guild.getName());
        }
    }

    /**
     *
     * @param guild
     * @param strikes
     */
    @Override
    public void setRefStrikes(Guild guild, int strikes)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setRefStrikes(strikes);
            save(settings.get());
        } else {
            log.error("Unable to set ref strikes VALUE={} for guild = {}. AutoMod settings not found.", strikes, guild.getName());
        }
    }

    /**
     *
     * @param guild
     * @param strikes
     */
    @Override
    public void setCopypastaStrikes(Guild guild, int strikes)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setCopyPastaStrikes(strikes);
            save(settings.get());
        } else {
            log.error("Unable to set copypasta strikes VALUE={} for guild = {}. AutoMod settings not found.", strikes, guild.getName());
        }
    }

    /**
     *
     * @param guild
     * @param strikes
     */
    @Override
    public void setEveryoneStrikes(Guild guild, int strikes)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setEveryoneStrikes(strikes);
            save(settings.get());
        } else {
            log.error("Unable to set everyone strikes VALUE={} for guild = {}. AutoMod settings not found.", strikes, guild.getName());
        }
    }

    /**
     *
     * @param guild
     * @param strikes
     * @param deleteThresh
     * @param strikeThresh
     */
    @Override
    public void setDupeSettings(Guild guild, int strikes, int deleteThresh, int strikeThresh)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setDupeStrikes(strikes);
            settings.get().setDupeDeleteThresh(deleteThresh);
            settings.get().setDupeStrikesThresh(strikeThresh);
            save(settings.get());
        } else {
            log.error("Unable to set dupe settings strikes={} deleteThresh={} strikeThresh={} for guild={}. AutoMod settings not found.",
                strikes, deleteThresh, strikeThresh, guild.getName());
        }
    }

    /**
     *
     * @param guild
     * @param dehoistChar
     */
    @Override
    public void setDehoistChar(Guild guild, char dehoistChar)
    {
        invalidateCache(guild);
        Optional<AutoMod> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setDehoistChar((int) dehoistChar);
            save(settings.get());
        } else {
            log.error("Unable to set everyone strikes VALUE={} for guild = {}. AutoMod settings not found.", dehoistChar, guild.getName());
        }
    }
}
