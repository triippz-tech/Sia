package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A GuildMusicSettings.
 */
@Entity
@Table(name = "guild_music_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GuildMusicSettings implements Serializable, GuildSettingsProvider {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false, unique = true)
    private Long guildId;

    @NotNull
    @Column(name = "text_channel_id", nullable = false)
    private Long textChannelId;

    @NotNull
    @Column(name = "voice_channel_id", nullable = false)
    private Long voiceChannelId;

    @NotNull
    @Column(name = "dj_role_id", nullable = false)
    private Long djRoleId;

    @NotNull
    @Column(name = "volume", nullable = false)
    private Integer volume;

    @NotNull
    @Column(name = "jhi_repeat", nullable = false)
    private Boolean repeat;

    @NotNull
    @Column(name = "stay_in_channel", nullable = false)
    private Boolean stayInChannel;

    @NotNull
    @Column(name = "song_in_game", nullable = false)
    private Boolean songInGame;

    @NotNull
    @Column(name = "now_playing_images", nullable = false)
    private Boolean nowPlayingImages;

    @NotNull
    @Column(name = "use_eval", nullable = false)
    private Boolean use_eval;

    @NotNull
    @Column(name = "max_seconds", nullable = false)
    private Long maxSeconds;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Playlist playlist;

    @OneToOne(mappedBy = "guildMusicSettings")
    @JsonIgnore
    private DiscordGuild discordGuild;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GuildMusicSettings guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getTextChannelId() {
        return textChannelId;
    }

    public GuildMusicSettings textChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
        return this;
    }

    public void setTextChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
    }

    public Long getVoiceChannelId() {
        return voiceChannelId;
    }

    public GuildMusicSettings voiceChannelId(Long voiceChannelId) {
        this.voiceChannelId = voiceChannelId;
        return this;
    }

    public TextChannel getTextChannel(Guild guild) {
        return guild == null ? null : guild.getTextChannelById(textChannelId);
    }

    public VoiceChannel getVoiceChannel(Guild guild) {
        return guild == null ? null : guild.getVoiceChannelById(voiceChannelId);
    }

    public void setVoiceChannelId(Long voiceChannelId) {
        this.voiceChannelId = voiceChannelId;
    }

    public Long getDjRoleId() {
        return djRoleId;
    }

    public GuildMusicSettings djRoleId(Long djRoleId) {
        this.djRoleId = djRoleId;
        return this;
    }

    public void setDjRoleId(Long djRoleId) {
        this.djRoleId = djRoleId;
    }

    public Integer getVolume() {
        return volume;
    }

    public GuildMusicSettings volume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Boolean isRepeat() {
        return repeat;
    }

    public GuildMusicSettings repeat(Boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public GuildMusicSettings playlist(Playlist playlist) {
        this.playlist = playlist;
        return this;
    }

    public GuildMusicSettings maxSeconds(Long maxSeconds) {
        this.maxSeconds = maxSeconds;
        return this;
    }

    public GuildMusicSettings stayInChannel(Boolean stayInChannel) {
        this.stayInChannel = stayInChannel;
        return this;
    }

    public GuildMusicSettings nowPlayingImages(Boolean nowPlayingImages) {
        this.nowPlayingImages = nowPlayingImages;
        return this;
    }

    public GuildMusicSettings songInGame(Boolean songInGame) {
        this.songInGame = songInGame;
        return this;
    }

    public GuildMusicSettings use_eval(Boolean use_eval) {
        this.use_eval = use_eval;
        return this;
    }

    public Boolean getStayInChannel() {
        return stayInChannel;
    }

    public void setStayInChannel(Boolean stayInChannel) {
        this.stayInChannel = stayInChannel;
    }

    public Boolean getSongInGame() {
        return songInGame;
    }

    public void setSongInGame(Boolean songInGame) {
        this.songInGame = songInGame;
    }

    public Boolean getNowPlayingImages() {
        return nowPlayingImages;
    }

    public void setNowPlayingImages(Boolean nowPlayingImages) {
        this.nowPlayingImages = nowPlayingImages;
    }

    public Boolean getUse_eval() {
        return use_eval;
    }

    public void setUse_eval(Boolean use_eval) {
        this.use_eval = use_eval;
    }

    public Long getMaxSeconds() {
        return maxSeconds;
    }

    public void setMaxSeconds(Long maxSeconds) {
        this.maxSeconds = maxSeconds;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public GuildMusicSettings discordGuild(DiscordGuild discordGuild) {
        this.discordGuild = discordGuild;
        return this;
    }

    public void setDiscordGuild(DiscordGuild discordGuild) {
        this.discordGuild = discordGuild;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public void setDefaults(Long guildId) {
        this.guildId = guildId;
        this.textChannelId = 0L;
        this.voiceChannelId = 0L;
        this.djRoleId = 0L;
        this.volume = 100;
        this.playlist = null;
        this.repeat = false;
        this.stayInChannel = false;
        this.songInGame = false;
        this.nowPlayingImages = false;
        this.use_eval = false;
        this.maxSeconds = 0L;
    }

    public Role getDjRole(Guild guild)
    {
        return guild.getRoleById(djRoleId);
    }

    public TextChannel getTextChannelId(Guild guild)
    {
        return guild.getTextChannelById(textChannelId);
    }

    public VoiceChannel getVoiceChannelId(Guild guild)
    {
        return guild.getVoiceChannelById(voiceChannelId);
    }

    public boolean isTooLong(AudioTrack track)
    {
        if(maxSeconds<=0)
            return false;
        return Math.round(track.getDuration()/1000.0) > maxSeconds;
    }

    public String getMaxTime()
    {
        return FormatUtil.formatTime(maxSeconds * 1000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuildMusicSettings guildMusicSettings = (GuildMusicSettings) o;
        if (guildMusicSettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), guildMusicSettings.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GuildMusicSettings{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", textChannelId=" + getTextChannelId() +
            ", voiceChannelId=" + getVoiceChannelId() +
            ", djRoleId=" + getDjRoleId() +
            ", volume=" + getVolume() +
            ", repeat='" + isRepeat() + "'" +
            "}";
    }
}
