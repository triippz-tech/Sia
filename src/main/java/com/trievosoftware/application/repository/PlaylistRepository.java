package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Playlist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Playlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByUserIdAndPlaylistName(Long userId, String playlistName);

    Optional<Playlist> findFirstByGuildId(Long guildId);

    List<Playlist> findAllByPlaylistName(String playlistName);

    List<Playlist> findAllByUserId(Long userId);

    List<Playlist> findAllByUserIdAndPlaylistName(Long userId, String playlistName);
}
