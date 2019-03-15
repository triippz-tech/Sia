package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Playlist.
 */
@Entity
@Table(name = "playlist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "guild_id")
    private Long guildId;

    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "playlist_name", nullable = false)
    private String playlistName;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Songs> songs = new HashSet<>();
    @OneToOne(mappedBy = "playlist", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIgnore
    private GuildMusicSettings guildmusicsettings;

    public Playlist(Guild guild, @NotNull String playlistName) {
        this.guildId = guild.getIdLong();
        this.playlistName = playlistName;
    }

    public Playlist(User user, @NotNull String playlistName) {
        this.userId = user.getIdLong();
        this.playlistName = playlistName;
    }

    public Playlist() {
    }

    public Playlist(@NotNull String playlistName) {
        this.playlistName = playlistName;
    }

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

    public Playlist guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getUserId() {
        return userId;
    }

    public Playlist userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public Playlist playlistName(String playlistName) {
        this.playlistName = playlistName;
        return this;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Set<Songs> getSongs() {
        return songs;
    }

    public Playlist songs(Set<Songs> songs) {
        this.songs = songs;
        return this;
    }

    public Playlist addSong(Songs songs) {
        this.songs.add(songs);
//        songs.setPlaylist(this);
        return this;
    }

    public Playlist removeSong(Songs songs) {
        this.songs.remove(songs);
//        songs.setPlaylist(null);
        return this;
    }

    public void setSongs(Set<Songs> songs) {
        this.songs = songs;
    }

    public GuildMusicSettings getGuildmusicsettings() {
        return guildmusicsettings;
    }

    public Playlist guildmusicsettings(GuildMusicSettings guildMusicSettings) {
        this.guildmusicsettings = guildMusicSettings;
        return this;
    }

    public void setGuildmusicsettings(GuildMusicSettings guildMusicSettings) {
        this.guildmusicsettings = guildMusicSettings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        if (playlist.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playlist.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Playlist{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", userId=" + getUserId() +
            ", playlistName='" + getPlaylistName() + "'" +
            "}";
    }
}
