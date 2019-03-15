/*
 *    Copyright 2019 Mark Tripoli
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import com.trievosoftware.application.repository.GuildMusicSettingsRepository;
import com.trievosoftware.application.service.GuildMusicSettingsService;
import com.trievosoftware.discord.utils.FixedCache;
import net.dv8tion.jda.core.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing GuildMusicSettings.
 */
@Service
@Transactional
public class GuildMusicSettingsServiceImpl implements GuildMusicSettingsService {

    private final Logger log = LoggerFactory.getLogger(GuildMusicSettingsServiceImpl.class);

    private final GuildMusicSettingsRepository guildMusicSettingsRepository;

    private static final String SETTINGS_TITLE = "\uD83C\uDFB5 Server Settings \uD83C\uDFB5"; // 🎵
    private final FixedCache<Long, GuildMusicSettings> cache = new FixedCache<>(1000);

    public GuildMusicSettingsServiceImpl(GuildMusicSettingsRepository guildMusicSettingsRepository) {
        this.guildMusicSettingsRepository = guildMusicSettingsRepository;
    }

    /**
     * Save a guildMusicSettings.
     *
     * @param guildMusicSettings the entity to save
     * @return the persisted entity
     */
    @Override
    public GuildMusicSettings save(GuildMusicSettings guildMusicSettings) {
        log.debug("Request to save GuildMusicSettings : {}", guildMusicSettings);
        return guildMusicSettingsRepository.save(guildMusicSettings);
    }

    /**
     * Save a guildMusicSettings.
     *
     * @param guildMusicSettings the entity to save
     * @return the persisted entity
     */
    @Override
    public GuildMusicSettings saveAndFlush(GuildMusicSettings guildMusicSettings) {
        log.debug("Request to save GuildMusicSettings : {}", guildMusicSettings);
        return guildMusicSettingsRepository.saveAndFlush(guildMusicSettings);
    }

    /**
     * Get all the guildMusicSettings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GuildMusicSettings> findAll() {
        log.debug("Request to get all GuildMusicSettings");
        return guildMusicSettingsRepository.findAll();
    }


    /**
     * Get one guildMusicSettings by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GuildMusicSettings> findOne(Long id) {
        log.debug("Request to get GuildMusicSettings : {}", id);
        return guildMusicSettingsRepository.findById(id);
    }

    /**
     * Delete the guildMusicSettings by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GuildMusicSettings : {}", id);
        guildMusicSettingsRepository.deleteById(id);
    }

    /**
     * Get one guildMusicSettings by guildId.
     *
     * @param guildId the id of the entity
     * @return the entity
     */
    @Override
    public Optional<GuildMusicSettings> findByGuildId(Long guildId) {
        log.debug("Request to get GuildMusicSettings for Guild: {}", guildId);
        return guildMusicSettingsRepository.findByGuildId(guildId);
    }

    @Override
    public GuildMusicSettings getSettings(Long guildId)
    {
        log.info("Request to get music settings for Guild={}", guildId);
        if(cache.contains(guildId))
            return cache.get(guildId);
        Optional<GuildMusicSettings> tempSettings = findByGuildId(guildId);
        if ( tempSettings.isPresent() )
        {
            log.info("Music Settings found for Guild={}", guildId);
            cache.put(guildId, tempSettings.get());
            return tempSettings.get();
        } else {
            log.info("No music settings found for Guild={}, creating new. . .", guildId);
            GuildMusicSettings settings = new GuildMusicSettings();
            settings.setDefaults(guildId);
            save(settings);
            cache.put(guildId, settings);
            return settings;

        }
    }

    @Override
    public GuildMusicSettings getSettings(Guild guild)
    {
        return getSettings(guild.getIdLong());
    }

    private void invalidateCache(Guild guild)
    {
        invalidateCache(guild.getIdLong());
    }

    private void invalidateCache(long guildId)
    {
        log.debug("Request to pull Guild={} from cache", guildId);
        cache.pull(guildId);
    }

    @Override
    public boolean hasSettings(Guild guild)
    {
        log.debug("Request to see if music settings exist for Guild={}", guild.getName());
        return findByGuildId(guild.getIdLong()).isPresent();
    }

    @Override
    public MessageEmbed.Field getSettingsDisplay(Guild guild)
    {
        log.debug("Request to get Music Settings Display Message for Guild={}", guild.getName());
        GuildMusicSettings settings = getSettings(guild);
        TextChannel textChannel = settings.getTextChannelId(guild);
        VoiceChannel voiceChannel = settings.getVoiceChannelId(guild);
        Role djRole = settings.getDjRole(guild);
        Boolean repeat = settings.isRepeat();
        Integer volume = settings.getVolume();
        String defaultPlaylist;
        try {
            defaultPlaylist = (settings.getPlaylist().getPlaylistName() == null ? "None" : settings.getPlaylist().getPlaylistName());
        } catch (NullPointerException e) {
            defaultPlaylist = "None";
        }
        return new MessageEmbed.Field(SETTINGS_TITLE,
             "DJ Role: "+(djRole==null ? "None" : djRole.getAsMention())
            + "\nText Channel: "+(textChannel==null ? "All" : textChannel.getAsMention())
            + "\nVoice Channel: "+(voiceChannel==null ? "All" : voiceChannel.getName())
            + "\nRepeat: "+(repeat)
            + "\nVolume: "+(volume)
            + "\nDefault Playlist: "+ defaultPlaylist
            , true);
    }

    @Override
    public void setDjRole(Guild guild, Role role) throws NoMusicSettingsException {
        log.debug("Request to set DJ Role={} for Guild={}", role.getName(), guild.getName());
        invalidateCache(guild);
        Optional<GuildMusicSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setDjRoleId(role==null ? 0L : role.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set DJ role={} for guild = {}", role.getName(), guild.getName());
            throw new NoMusicSettingsException("No Music Settings exist. Please ensure MusicSettings have " +
                "been setup for Guild by using command `/msettings`");
        }
    }

    @Override
    public void setDefaultPlaylist(Guild guild, Playlist playlist) throws NoMusicSettingsException {
        log.debug("Request to set DefaultPlaylist={} for Guild={}", playlist.getPlaylistName(), guild.getName());
        invalidateCache(guild);
        Optional<GuildMusicSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setPlaylist( playlist );
            saveAndFlush(settings.get());
        } else {
            log.error("Unable to set DefaultPlaylist={} for guild = {}", playlist.getPlaylistName(), guild.getName());
            throw new NoMusicSettingsException("No Music Settings exist. Please ensure MusicSettings have " +
                "been setup for Guild by using command `/msettings`");
        }
    }

    @Override
    public void setRepeat(Guild guild, Boolean repeat) throws NoMusicSettingsException {
        log.debug("Request to set Repeat={} for Guild={}", repeat, guild.getName());
        invalidateCache(guild);
        Optional<GuildMusicSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setRepeat(repeat==null ? false : repeat);
            save(settings.get());
        } else {
            log.error("Unable to set Repeat={} for guild = {}", repeat, guild.getName());
            throw new NoMusicSettingsException("No Music Settings exist. Please ensure MusicSettings have " +
                "been setup for Guild by using command `/msettings`");
        }
    }

    @Override
    public void setVolume(Guild guild, Integer volume) throws NoMusicSettingsException {
        log.debug("Request to set Volume={} for Guild={}", volume, guild.getName());
        invalidateCache(guild);
        Optional<GuildMusicSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setVolume(volume==null ? 100 : volume);
            save(settings.get());
        } else {
            log.error("Unable to set Volume={} for guild = {}", volume, guild.getName());
            throw new NoMusicSettingsException("No Music Settings exist. Please ensure MusicSettings have " +
                "been setup for Guild by using command `/msettings`");
        }
    }

    @Override
    public void setTextChannel(Guild guild, TextChannel tc) throws NoMusicSettingsException {
        log.debug("Request to set Music Text Channel={} for Guild={}", tc.getName(), guild.getName());
        invalidateCache(guild);
        Optional<GuildMusicSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setTextChannelId(tc==null ? 0L : tc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set Music Text channel={} for guild = {}", tc.getName(), guild.getIdLong());
            throw new NoMusicSettingsException("No Music Settings exist. Please ensure MusicSettings have " +
                "been setup for Guild by using command `/msettings`");
        }
    }

    @Override
    public void setVoiceChannel(Guild guild, VoiceChannel vc) throws NoMusicSettingsException {
        log.debug("Request to set Music Voice Channel={} for Guild={}", vc.getName(), guild.getName());
        invalidateCache(guild);
        Optional<GuildMusicSettings> settings = findByGuildId(guild.getIdLong());
        if ( settings.isPresent() ) {
            settings.get().setVoiceChannelId(vc==null ? 0L : vc.getIdLong());
            save(settings.get());
        } else {
            log.error("Unable to set Music Voice channel={} for guild = {}", vc.getName(), guild.getIdLong());
            throw new NoMusicSettingsException("No Music Settings exist. Please ensure MusicSettings have " +
                "been setup for Guild by using command `/msettings`");
        }
    }
}
