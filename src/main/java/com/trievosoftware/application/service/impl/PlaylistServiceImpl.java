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
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.application.repository.PlaylistRepository;
import com.trievosoftware.application.service.PlaylistService;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@SuppressWarnings("Duplicates")
public class PlaylistServiceImpl implements PlaylistService
{
    private final Logger log = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    private final PlaylistRepository playlistRepository;

    private static final String EMOJI = "\uD83C\uDFB6"; // 🎶

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    /**
     * Save a playlist.
     *
     * @param playlist the entity to save
     * @return the persisted entity
     */
    @Override
    public Playlist save(Playlist playlist) {
        log.debug("Request to save Playlist : {}", playlist);
        return playlistRepository.save(playlist);
    }

    /**
     * Get all the playlists.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Playlist> findAll() {
        log.debug("Request to get all Playlists");
        return playlistRepository.findAll();
    }


    /**
     * Get one playlist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Playlist> findOne(Long id) {
        log.debug("Request to get Playlist : {}", id);
        return playlistRepository.findById(id);
    }

    /**
     * Delete the playlist by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Playlist : {}", id);
        playlistRepository.deleteById(id);
    }

    /**
     * Get all the playlists by userId
     *
     * @return the list of entities
     */
    @Override
    @Transactional()
    public List<Playlist> findAllByUserId(Long userId) {
        log.debug("Request to get all Playlists for user={}", userId);
        return playlistRepository.findAllByUserId(userId);
    }

    /**
     * Finds all playlists of a certain name for a user
     *
     * @param userId Id of thee user (Discord)
     * @param playlistName The playlist name to find
     * @return List
     */
    @Override
    @Transactional()
    public List<Playlist> findAllByUserIdAndPlaylistName(Long userId, String playlistName)
    {
        log.debug("Request to get all Playlists={} for user={}", playlistName, userId);
        return playlistRepository.findAllByUserIdAndPlaylistName(userId, playlistName);
    }

    /**
     * Get all the playlists with specific playlist name
     *
     * @return the list of entities
     */
    @Override
    @Transactional()
    public List<Playlist> findAllByPlaylistName(String playlistName) {
        log.debug("Request to get all Playlists for with name={}", playlistName);
        return playlistRepository.findAllByPlaylistName(playlistName);
    }

    /**
     * Get one playlist by guildId.
     *
     * @param guildId the id of the Guild
     * @return the entity
     */
    @Override
    @Transactional()
    public Optional<Playlist> findFirstByGuildId(Long guildId) {
        log.debug("Request to get Playlist for guild : {}", guildId);
        return playlistRepository.findFirstByGuildId(guildId);
    }

    /**
     *
     * @param userId
     * @param playlistName
     * @return
     */
    @Override
    @Transactional()
    public Optional<Playlist> findByUserIdAndPlaylistName(Long userId, String playlistName) {
        log.debug("Request to get Playlist={} for user : {}", playlistName, userId);
        return playlistRepository.findByUserIdAndPlaylistName(userId, playlistName);
    }


    @Override
    @Transactional()
    public List<Playlist> getUserPlaylists(Long userId) throws NoPlaylistFoundException {
        log.debug("Request to get playlists owned by user={}", userId);
        List<Playlist> playlists = findAllByUserId(userId);
        if ( !playlists.isEmpty() )
        {
            log.debug("Found: {} playlists for user={}", playlists.size(), userId);
            return playlists;
        } else {
            log.debug("No playlists found for user={}", userId);
            throw new NoPlaylistFoundException("No playlists found, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public Playlist getUserPlaylistByName(Long userId, String playlistName) throws NoPlaylistFoundException {
        log.debug("Request to get playlist={} owned by user={}", playlistName, userId);
        Optional<Playlist> playlist = findByUserIdAndPlaylistName(userId, playlistName);
        if ( playlist.isPresent() )
        {
            log.debug("Found: {} playlist for user={}", playlist.get().getPlaylistName(), userId);
            return playlist.get();
        } else {
            log.debug("No playlist={} found for user={}", playlistName, userId);
            throw new NoPlaylistFoundException("No playlist found, please consider creating one.");
        }
    }

    @Override
    @Transactional
    public boolean userPlaylistExists(Long userId, String playlistName)
    {
        log.debug("Request to see if playlist={} exists for user={}", playlistName, userId);
        List<Playlist> playlists = findAllByUserIdAndPlaylistName(userId, playlistName);
        return playlists.size() >= 1;
    }

    @Override
    @Transactional()
    public Playlist getGuildPlaylist(Guild guild) throws NoPlaylistFoundException {
        log.debug("Request to get playlist owned by guild={}", guild.getName());
        Optional<Playlist> playlist = findFirstByGuildId(guild.getIdLong());
        if ( playlist.isPresent() )
        {
            log.debug("Found: {} playlist for guild={}", playlist.get().getPlaylistName(), guild.getName());
            return playlist.get();
        } else {
            log.debug("No playlist found for Guild={}", guild.getName());
            throw new NoPlaylistFoundException("No playlists found, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public MessageEmbed.Field getUserPlaylistDisplay(User user, Guild guild)
    {
        log.debug("Request to get Playlist Display Message for User={}", user.getName());
        try {
            List<Playlist> playlists = getUserPlaylists(user.getIdLong());
            StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < playlists.size(); i ++ )
            {
                builder.append(i).append(") ").append(playlists.get(i).getPlaylistName())
                    .append("\t Total Songs: ").append(playlists.get(i).getSongs().size()).append("\n");
            }

            return new MessageEmbed.Field(
                EMOJI + user.getName() + "'s Playlist(s) " + EMOJI,
                builder.toString()
                , true);
        } catch (NoPlaylistFoundException e) {
            return new MessageEmbed.Field(
                EMOJI + user.getName() + "'s Playlist(s) " + EMOJI,
                "You have no playlists!"
                , true);
        }
    }

    @Override
    @Transactional()
    public MessageEmbed.Field getGuildPlaylistDisplay(Guild guild)
    {
        log.debug("Request to get Playlist Display Message for Guild={}", guild.getName());
        Optional<Playlist> playlists = findFirstByGuildId(guild.getIdLong());
        StringBuilder builder = new StringBuilder();

        if (playlists.isPresent())
        {
            for ( Songs song : playlists.get().getSongs() )
            {
                builder.append(
                    ( song.getSongName()==null
                        ? song.getSongQuery()
                        : song.getSongName().replaceAll("_", " ") )
                )
                    .append("\n");
            }

            return new MessageEmbed.Field(
                EMOJI + playlists.get().getPlaylistName() + EMOJI,
                "**Tracks:**\n" + builder.toString(),
                true
            );
        } else {
            return new MessageEmbed.Field(
                EMOJI + guild.getName() + "'s Playlist(s) " + EMOJI,
                "You have no playlists!"
                , true);
        }
    }

    @Override
    @Transactional()
    public void addSongToGuildPlaylist(Guild guild, Songs song) throws NoPlaylistFoundException {
        log.debug("Request to add Song to Guild={} Playlist with Query={}", guild.getName(), song.getSongQuery());
        Optional<Playlist> playlist = findFirstByGuildId(guild.getIdLong());
        if ( playlist.isPresent() )
        {
            playlist.get().addSong(song);
            save(playlist.get());
            log.debug("New song added to playlist={}", playlist.get().getPlaylistName());
        } else {
            log.debug("No playlist found for Guild={}", guild.getName());
            throw new NoPlaylistFoundException("Can not add Song. No playlist found for Guild, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public void removeSongFromGuildPlaylist(Guild guild, Songs song) throws NoPlaylistFoundException {
        log.debug("Request to remove Song from Guild={} playlist", guild.getName());
        Optional<Playlist> playlist = findFirstByGuildId(guild.getIdLong());
        if ( playlist.isPresent() )
        {
            playlist.get().removeSong(song);
            save(playlist.get());
            log.debug("Song={} removed from Playlist={}", song.getSongQuery(), playlist.get().getPlaylistName());
        } else {
            log.debug("No playlist found for Guild={}", guild.getName());
            throw new NoPlaylistFoundException("Can not remove Song. No playlist found for Guild, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public void addSongToUserPlaylist(User user, long playlistId, Songs song) throws NoPlaylistFoundException {
        log.debug("Request to add Song to User={} Playlist with Query={}", user.getName(), song.getSongQuery());
        Optional<Playlist> playlist = findOne(playlistId);
        if ( playlist.isPresent() )
        {
            playlist.get().addSong(song);
            save(playlist.get());
            log.debug("New song added to playlist={}", playlist.get().getPlaylistName());
        } else {
            log.debug("No playlist={} found for User={}", playlist.get().getPlaylistName(), user.getName());
            throw new NoPlaylistFoundException("Can not add Song. No playlist found for User, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public void addSongToUserPlaylist(User user, Playlist playlist, Songs song) {
        log.debug("Request to add Song to User={} Playlist={}", user.getName(), playlist.getPlaylistName());
        playlist.addSong(song);
        save(playlist);
        log.debug("New song added to playlist={}", playlist.getPlaylistName());
    }

    @Override
    @Transactional()
    public void removeSongFromUserPlaylist(User user, String playlistName, Songs song) throws NoPlaylistFoundException {
        log.debug("Request to remove Song from User={} Playlist with Query={}", user.getName(), song.getSongQuery());
        Optional<Playlist> playlist = findByUserIdAndPlaylistName(user.getIdLong(), playlistName);
        if ( playlist.isPresent() )
        {
            playlist.get().removeSong(song);
            save(playlist.get());
            log.debug("Song removed from playlist={}", playlist.get().getPlaylistName());
        } else {
            log.debug("No playlist={} found for User={}", playlistName, user.getName());
            throw new NoPlaylistFoundException("Can not remove Song. No playlist found for User, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public void removeSongFromUserPlaylist(User user, Long playlistId, Songs song) throws NoPlaylistFoundException {
        log.debug("Request to remove Song from User={} Playlist={}", user.getName(), playlistId);
        Optional<Playlist> playlist = findOne(user.getIdLong());
        if ( playlist.isPresent() )
        {
            playlist.get().removeSong(song);
            save(playlist.get());
            log.debug("Song removed from playlist={}", playlist.get().getPlaylistName());
        } else {
            log.debug("No playlist={} found for User={}", playlistId, user.getName());
            throw new NoPlaylistFoundException("Can not remove Song. No playlist found for User, please consider creating one.");
        }
    }

    @Override
    @Transactional()
    public void deleteAllUserPlaylists(User user) throws NoPlaylistFoundException {
        log.debug("Request to remove all User={}'s playlists", user.getName());
        List<Playlist> playlists = findAllByUserId(user.getIdLong());
        if ( !playlists.isEmpty() )
        {
            for ( Playlist playlist : playlists )
            {
                delete(playlist.getId());
                log.debug("Playlist={} removed.", playlist.getPlaylistName());
            }
        } else{
            log.warn("user={} has no playlists", user.getName());
            throw new NoPlaylistFoundException("Unable to remove playlists, as no playlists were found!");
        }
    }

    @Override
    @Transactional()
    public void deleteUserPlaylist(User user, Long playlistId)
    {
        log.debug("Request to remove Playlist={} for User={}", playlistId, user.getName());
        try {
            delete(playlistId);
            log.debug("Playlist={} deleted", playlistId);
        } catch (Exception e) { //until i find the catual exception type
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional()
    public void deleteUserPlaylist(User user, String playlistName) throws NoPlaylistFoundException {
        log.debug("Request to remove Playlist={} for User={}", playlistName, user.getName());
        Optional<Playlist> playlist = findByUserIdAndPlaylistName(user.getIdLong(), playlistName);
        if ( playlist.isPresent() )
        {
            delete(playlist.get().getId());
            log.debug("Playlist={} deleted", playlistName);
        } else {
            log.warn("user={} has no playlists", user.getName());
            throw new NoPlaylistFoundException("Unable to remove playlist, as no playlists were found with that name!");
        }
    }

    @Override
    @Transactional()
    public void createUserPlaylist(String name, User user) {
        log.debug("Request to create Playlist={}", name, user.getName());
        Playlist playlist = new Playlist(user, name);
        playlist.setPlaylistName(name);
        playlist.setUserId(user.getIdLong());
        save(playlist);
        log.debug("Playlist={} created for User={}", name, user.getName());
    }

    @Override
    @Transactional()
    public Playlist createGuildPlaylist(String name, Guild guild) {
        log.debug("Request to create Playlist={}", name, guild.getName());
        Playlist playlist = new Playlist(guild, name);
        playlist.setPlaylistName(name);
        playlist.setGuildId(guild.getIdLong());
        save(playlist);
        log.debug("Playlist={} created for User={}", name, guild.getName());
        return playlist;
    }

    @Override
    @Transactional()
    public List<MessageEmbed.Field> getUserPlaylistDisplays(User user)  {
        log.debug("Request to get all playlists for User={}", user.getName());
        List<Playlist> playlistList = findAllByUserId(user.getIdLong());
        if ( !playlistList.isEmpty() )
        {
            List<MessageEmbed.Field> embedFields = new ArrayList<>();
            for ( Playlist playlist : playlistList )
            {
                embedFields.add(
                    new MessageEmbed.Field(
                    "** " + playlist.getPlaylistName() + " **",
                        "Total Songs: " + playlist.getSongs().size(),
                        true
                    )
                );
            }
            return embedFields;
        }
        else {
            log.warn("No playlists found for User={}", user.getName());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional()
    public List<MessageEmbed.Field> getUserPlaylistDisplays(String[] playlistNames, User user)  {
        log.debug("Request to get all playlists for User={}", user.getName());
        List<Playlist> playlistList = findAllByUserId(user.getIdLong());
        if ( !playlistList.isEmpty() )
        {
            List<MessageEmbed.Field> embedFields = new ArrayList<>();
            int listedTracks = 0;
            for ( Playlist playlist : playlistList )
            {
                StringBuilder builder = new StringBuilder();
                for ( Songs song : playlist.getSongs() ) {
                    builder.append(song.getSongName()).append("\n");
                    if (builder.toString().length() > 800) {
                        builder.append("Plus: ").append((playlistList.size()-listedTracks)).append(" More!");
                        break;
                    }
                    listedTracks ++;
                }

                embedFields.add(
                    new MessageEmbed.Field(
                        "** " + playlist.getPlaylistName() + " **",
                        builder.toString(),
                        true
                    )
                );
            }
            return embedFields;
        }
        else {
            log.warn("No playlists found for User={}", user.getName());
            return Collections.emptyList();
        }
    }
}

