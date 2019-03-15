package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import net.dv8tion.jda.core.entities.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GuildMusicSettingsService {
    GuildMusicSettings save(GuildMusicSettings guildMusicSettings);

    GuildMusicSettings saveAndFlush(GuildMusicSettings guildMusicSettings);

    @Transactional(readOnly = true)
    List<GuildMusicSettings> findAll();

    @Transactional(readOnly = true)
    Optional<GuildMusicSettings> findOne(Long id);

    void delete(Long id);

    Optional<GuildMusicSettings> findByGuildId(Long guildId);

    GuildMusicSettings getSettings(Long guildId);

    GuildMusicSettings getSettings(Guild guild);

    boolean hasSettings(Guild guild);

    MessageEmbed.Field getSettingsDisplay(Guild guild);

    void setDjRole(Guild guild, Role role) throws NoMusicSettingsException;

    void setDefaultPlaylist(Guild guild, Playlist playlist) throws NoMusicSettingsException;

    void setRepeat(Guild guild, Boolean repeat) throws NoMusicSettingsException;

    void setVolume(Guild guild, Integer volume) throws NoMusicSettingsException;

    void setTextChannel(Guild guild, TextChannel tc) throws NoMusicSettingsException;

    void setVoiceChannel(Guild guild, VoiceChannel vc) throws NoMusicSettingsException;
}
