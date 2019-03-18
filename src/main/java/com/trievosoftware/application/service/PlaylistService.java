package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    Playlist save(Playlist playlist);

    @Transactional(readOnly = true)
    List<Playlist> findAll();

    @Transactional(readOnly = true)
    Optional<Playlist> findOne(Long id);

    void delete(Long id);

    @Transactional()
    List<Playlist> findAllByUserId(Long userId);

    @Transactional()
    List<Playlist> findAllByUserIdAndPlaylistName(Long userId, String playlistName);

    @Transactional()
    List<Playlist> findAllByPlaylistName(String playlistName);

    @Transactional()
    Optional<Playlist> findFirstByGuildId(Long guildId);

    @Transactional()
    Optional<Playlist> findByUserIdAndPlaylistName(Long userId, String playlistName);

    List<Playlist> getUserPlaylists(Long userId) throws NoPlaylistFoundException;

    Playlist getUserPlaylistByName(Long userId, String playlistName) throws NoPlaylistFoundException;

    @Transactional
    boolean userPlaylistExists(Long userId, String playlistName);

    Playlist getGuildPlaylist(Guild guild) throws NoPlaylistFoundException;

    MessageEmbed.Field getUserPlaylistDisplay(User user, Guild guild);

    MessageEmbed.Field getGuildPlaylistDisplay(Guild guild);

    void addSongToGuildPlaylist(Guild guild, Songs song) throws NoPlaylistFoundException;

    void removeSongFromGuildPlaylist(Guild guild, Songs song) throws NoPlaylistFoundException;

    void addSongToUserPlaylist(User user, long playlistId, Songs song) throws NoPlaylistFoundException;

    void addSongToUserPlaylist(User user, Playlist playlist, Songs song);

    void removeSongFromUserPlaylist(User user, String playlistName, Songs song) throws NoPlaylistFoundException;

    void removeSongFromUserPlaylist(User user, Long playlistId, Songs song) throws NoPlaylistFoundException;

    void deleteAllUserPlaylists(User user) throws NoPlaylistFoundException;

    void deleteUserPlaylist(User user, Long playlistId);

    void deleteUserPlaylist(User user, String playlistName) throws NoPlaylistFoundException;

    void createUserPlaylist(String name, User user);

    Playlist createGuildPlaylist(String name, Guild guild);

    List<MessageEmbed.Field> getUserPlaylistDisplays(User user);

    @Transactional()
    List<MessageEmbed.Field> getUserPlaylistDisplays(String[] playlistNames, User user);
}
