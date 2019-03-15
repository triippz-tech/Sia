package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Songs.
 */
@Entity
@Table(name = "songs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Songs implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "song_name")
    private String songName;

    @NotNull
    @Column(name = "song_query", nullable = false)
    private String songQuery;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("songs")
    private Playlist playlist;

    public Songs(@NotNull String songQuery, @NotNull Playlist playlist) {
        this.songQuery = songQuery;
        this.playlist = playlist;
    }

    public Songs() {
    }

    public Songs(String songName, @NotNull String songQuery, @NotNull Playlist playlist) {
        this.songName = songName;
        this.songQuery = songQuery;
        this.playlist = playlist;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public Songs songName(String songName) {
        this.songName = songName;
        return this;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongQuery() {
        return songQuery;
    }

    public Songs songQuery(String songQuery) {
        this.songQuery = songQuery;
        return this;
    }

    public void setSongQuery(String songQuery) {
        this.songQuery = songQuery;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Songs playlist(Playlist playlist) {
        this.playlist = playlist;
        return this;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
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
        Songs songs = (Songs) o;
        if (songs.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), songs.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Songs{" +
            "id=" + getId() +
            ", songName='" + getSongName() + "'" +
            ", songQuery='" + getSongQuery() + "'" +
            "}";
    }
}
