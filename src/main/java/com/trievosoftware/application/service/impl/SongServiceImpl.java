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

import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoSongException;
import com.trievosoftware.application.repository.SongsRepository;
import com.trievosoftware.application.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SongServiceImpl implements SongService {

    private final Logger log = LoggerFactory.getLogger(SongServiceImpl.class);

    private final SongsRepository songsRepository;

    public SongServiceImpl(SongsRepository songsRepository) {
        this.songsRepository = songsRepository;
    }

    /**
     * Save a Song.
     *
     * @param song the entity to save
     * @return the persisted entity
     */
    @Override
    public Songs save(Songs song) {
        log.debug("Request to save song : {}", song);
        return songsRepository.save(song);
    }

    /**
     * Get all the songs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Songs> findAll() {
        log.debug("Request to get all Songs");
        return songsRepository.findAll();
    }


    /**
     * Get one Song by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Songs> findOne(Long id) {
        log.debug("Request to get Songs : {}", id);
        return songsRepository.findById(id);
    }

    /**
     * Delete the Song by id.
     *
     * @param id the id of the entity
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Songs : {}", id);
        songsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Songs createSong(Songs song)
    {
        log.debug("Request to create new song={}", song.toString());
        songsRepository.saveAndFlush(song);
        return song;
    }

    @Override
    @Transactional
    public Songs createSong(String query, Playlist playlist)
    {
        log.debug("Request to create new song with query={}", query);
        Songs song = new Songs(query, playlist);
        songsRepository.saveAndFlush(song);
        log.debug("New song created with query ={}" , query);
        return song;
    }

    @Override
    @Transactional
    public Songs createSong(String name, String query, Playlist playlist)
    {
        log.debug("Request to create new song={} with query={} for Guuild={}", name, query, playlist.getGuildId());
        Songs song = new Songs(name, query, playlist);
        songsRepository.saveAndFlush(song);
        log.debug("New Song={} created with query ={}" , name, query);
        return song;
    }

    @Override
    @Transactional
    public Integer deleteSongByNameAndPlaylist(String songName, Playlist playlist) throws NoSongException {
        log.debug("Request to delete Song={} from Playlist={}", songName, playlist.getPlaylistName());
        List<Songs> songs = songsRepository.findBySongNameAndPlaylist(songName, playlist);
        if ( !songs.isEmpty() )
            for ( Songs song : songs )
                delete(song.getId());
        else
            throw new NoSongException("Could not find song named: " + songName + ". Please ensure you have spelled it correctly.");

        log.debug("Successfully deleted Song={} from Playlist={}", songName, playlist.getPlaylistName());
        return songs.size();
    }

}
