package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.DiscordUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the DiscordUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long>, JpaSpecificationExecutor<DiscordUser> {
    Optional<DiscordUser> findByUserId(Long userId);
}
