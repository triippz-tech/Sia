package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.DiscordUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
