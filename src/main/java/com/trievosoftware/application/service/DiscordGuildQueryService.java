package com.trievosoftware.application.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.domain.*; // for static metamodels
import com.trievosoftware.application.repository.DiscordGuildRepository;
import com.trievosoftware.application.service.dto.DiscordGuildCriteria;

/**
 * Service for executing complex queries for DiscordGuild entities in the database.
 * The main input is a {@link DiscordGuildCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscordGuild} or a {@link Page} of {@link DiscordGuild} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscordGuildQueryService extends QueryService<DiscordGuild> {

    private final Logger log = LoggerFactory.getLogger(DiscordGuildQueryService.class);

    private final DiscordGuildRepository discordGuildRepository;

    public DiscordGuildQueryService(DiscordGuildRepository discordGuildRepository) {
        this.discordGuildRepository = discordGuildRepository;
    }

    /**
     * Return a {@link List} of {@link DiscordGuild} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscordGuild> findByCriteria(DiscordGuildCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DiscordGuild> specification = createSpecification(criteria);
        return discordGuildRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DiscordGuild} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscordGuild> findByCriteria(DiscordGuildCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DiscordGuild> specification = createSpecification(criteria);
        return discordGuildRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscordGuildCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DiscordGuild> specification = createSpecification(criteria);
        return discordGuildRepository.count(specification);
    }

    /**
     * Function to convert DiscordGuildCriteria to a {@link Specification}
     */
    private Specification<DiscordGuild> createSpecification(DiscordGuildCriteria criteria) {
        Specification<DiscordGuild> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DiscordGuild_.id));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), DiscordGuild_.guildId));
            }
            if (criteria.getGuildName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuildName(), DiscordGuild_.guildName));
            }
            if (criteria.getInviteLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInviteLink(), DiscordGuild_.inviteLink));
            }
            if (criteria.getGuildSettingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildSettingsId(),
                    root -> root.join(DiscordGuild_.guildSettings, JoinType.LEFT).get(GuildSettings_.id)));
            }
            if (criteria.getAuditCacheId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuditCacheId(),
                    root -> root.join(DiscordGuild_.auditCache, JoinType.LEFT).get(AuditCache_.id)));
            }
            if (criteria.getAutoModId() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoModId(),
                    root -> root.join(DiscordGuild_.autoMod, JoinType.LEFT).get(AutoMod_.id)));
            }
            if (criteria.getGuildMusicSettingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildMusicSettingsId(),
                    root -> root.join(DiscordGuild_.guildMusicSettings, JoinType.LEFT).get(GuildMusicSettings_.id)));
            }
            if (criteria.getDiscordUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscordUserId(),
                    root -> root.join(DiscordGuild_.discordUsers, JoinType.LEFT).get(DiscordUser_.id)));
            }
            if (criteria.getIgnoredId() != null) {
                specification = specification.and(buildSpecification(criteria.getIgnoredId(),
                    root -> root.join(DiscordGuild_.ignoreds, JoinType.LEFT).get(Ignored_.id)));
            }
            if (criteria.getTempMutesId() != null) {
                specification = specification.and(buildSpecification(criteria.getTempMutesId(),
                    root -> root.join(DiscordGuild_.tempMutes, JoinType.LEFT).get(TempMutes_.id)));
            }
            if (criteria.getTempBansId() != null) {
                specification = specification.and(buildSpecification(criteria.getTempBansId(),
                    root -> root.join(DiscordGuild_.tempBans, JoinType.LEFT).get(TempBans_.id)));
            }
            if (criteria.getPollId() != null) {
                specification = specification.and(buildSpecification(criteria.getPollId(),
                    root -> root.join(DiscordGuild_.polls, JoinType.LEFT).get(Poll_.id)));
            }
            if (criteria.getGuildRolesId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildRolesId(),
                    root -> root.join(DiscordGuild_.guildRoles, JoinType.LEFT).get(GuildRoles_.id)));
            }
            if (criteria.getCustomCommandId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomCommandId(),
                    root -> root.join(DiscordGuild_.customCommands, JoinType.LEFT).get(CustomCommand_.id)));
            }
            if (criteria.getActionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getActionsId(),
                    root -> root.join(DiscordGuild_.actions, JoinType.LEFT).get(Actions_.id)));
            }
            if (criteria.getStrikesId() != null) {
                specification = specification.and(buildSpecification(criteria.getStrikesId(),
                    root -> root.join(DiscordGuild_.strikes, JoinType.LEFT).get(Strikes_.id)));
            }
            if (criteria.getWelcomeMessageId() != null) {
                specification = specification.and(buildSpecification(criteria.getWelcomeMessageId(),
                    root -> root.join(DiscordGuild_.welcomeMessages, JoinType.LEFT).get(WelcomeMessage_.id)));
            }
            if (criteria.getGuildEventId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildEventId(),
                    root -> root.join(DiscordGuild_.guildEvents, JoinType.LEFT).get(GuildEvent_.id)));
            }
            if (criteria.getGiveAwayId() != null) {
                specification = specification.and(buildSpecification(criteria.getGiveAwayId(),
                    root -> root.join(DiscordGuild_.giveAways, JoinType.LEFT).get(GiveAway_.id)));
            }
        }
        return specification;
    }
}
