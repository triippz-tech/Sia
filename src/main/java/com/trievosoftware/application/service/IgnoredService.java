package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.Ignored;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Ignored.
 */
public interface IgnoredService {

    /**
     * Save a ignored.
     *
     * @param ignored the entity to save
     * @return the persisted entity
     */
    Ignored save(Ignored ignored);

    /**
     * Get all the ignoreds.
     *
     * @return the list of entities
     */
    List<Ignored> findAll();


    /**
     * Get the "id" ignored.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Ignored> findOne(Long id);

    /**
     * Delete the "id" ignored.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Optional<Ignored> findByEntityId(Long entityId);

    List<Ignored> findByGuildIdAndType(Long guildId, int type);

    boolean isIgnored(TextChannel tc);

    boolean isIgnored(Member member);

    boolean ignore(TextChannel tc);

    boolean ignore(Role role);

    List<TextChannel> getIgnoredChannels(Guild guild);

    List<Role> getIgnoredRoles(Guild guild);

    boolean unignore(TextChannel tc);

    boolean unignore(Role role);
}
