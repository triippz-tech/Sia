package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.Songs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Songs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SongsRepository extends JpaRepository<Songs, Long> {
    List<Songs> findBySongNameAndPlaylist(String songName, Playlist playlist);
}
