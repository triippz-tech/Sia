package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.domain.WelcomeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the WelcomeMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WelcomeMessageRepository extends JpaRepository<WelcomeMessage, Long>, JpaSpecificationExecutor<WelcomeMessage> {
    List<WelcomeMessage> findAllByGuildsettings(GuildSettings guildSettings);
    Optional<WelcomeMessage> findByGuildsettingsAndActive(GuildSettings guildSettings, Boolean active);
    Optional<WelcomeMessage> findByGuildsettingsAndName(GuildSettings guildSettings, String name);
}
