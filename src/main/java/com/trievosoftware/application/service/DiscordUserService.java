package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.DiscordUser;

import com.trievosoftware.application.exceptions.NoDiscordUserFoundException;
import net.dv8tion.jda.core.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Interface for managing DiscordUser.
 */
public interface DiscordUserService {

    /**
     * Save a discordUser.
     *
     * @param discordUser the entity to save
     * @return the persisted entity
     */
    DiscordUser save(DiscordUser discordUser);

    /**
     * Get all the discordUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DiscordUser> findAll(Pageable pageable);


    /**
     * Get the "id" discordUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DiscordUser> findOne(Long id);

    /**
     * Delete the "id" discordUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    @Transactional(readOnly = true)
    Optional<DiscordUser> findByUserId(Long userId);

    boolean userExists(Long userId);

    void addNewUser(Long userId);

    DiscordUser addNewUser(User user);

    void blacklistUser(User user);

    void removeBlacklist(User user) throws NoDiscordUserFoundException;

    void addCommand(User user);

    boolean isUserBlacklisted(Long userid);
}
