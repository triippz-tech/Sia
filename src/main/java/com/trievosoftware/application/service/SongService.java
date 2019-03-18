package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoSongException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SongService {
    Songs save(Songs song);

    @Transactional(readOnly = true)
    List<Songs> findAll();

    @Transactional(readOnly = true)
    Optional<Songs> findOne(Long id);

    void delete(Long id);

    @Transactional
    Songs createSong(Songs song);

    Songs createSong(String query, Playlist playlist);

    Songs createSong(String name, String query, Playlist playlist);

    Integer deleteSongByNameAndPlaylist(String songName, Playlist playlist) throws NoSongException;
}
