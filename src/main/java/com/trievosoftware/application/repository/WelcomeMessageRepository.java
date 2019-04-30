package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.DiscordGuild;
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
    List<WelcomeMessage> findAllByDiscordGuild(DiscordGuild discordGuild);
    Optional<WelcomeMessage> findByDiscordGuildAndActive(DiscordGuild discordGuild, Boolean active);
    Optional<WelcomeMessage> findByDiscordGuildAndName(DiscordGuild discordGuild, String name);
}
