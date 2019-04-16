package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.DiscordUser_;
import com.trievosoftware.application.domain.GiveAway;
import com.trievosoftware.application.domain.GiveAway_;
import com.trievosoftware.application.domain.GuildSettings_;
import com.trievosoftware.application.repository.GiveAwayRepository;
import com.trievosoftware.application.service.dto.GiveAwayCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for GiveAway entities in the database.
 * The main input is a {@link GiveAwayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GiveAway} or a {@link Page} of {@link GiveAway} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GiveAwayQueryService extends QueryService<GiveAway> {

    private final Logger log = LoggerFactory.getLogger(GiveAwayQueryService.class);

    private final GiveAwayRepository giveAwayRepository;

    public GiveAwayQueryService(GiveAwayRepository giveAwayRepository) {
        this.giveAwayRepository = giveAwayRepository;
    }

    /**
     * Return a {@link List} of {@link GiveAway} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GiveAway> findByCriteria(GiveAwayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GiveAway> specification = createSpecification(criteria);
        return giveAwayRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GiveAway} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GiveAway> findByCriteria(GiveAwayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GiveAway> specification = createSpecification(criteria);
        return giveAwayRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GiveAwayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GiveAway> specification = createSpecification(criteria);
        return giveAwayRepository.count(specification);
    }

    /**
     * Function to convert GiveAwayCriteria to a {@link Specification}
     */
    private Specification<GiveAway> createSpecification(GiveAwayCriteria criteria) {
        Specification<GiveAway> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GiveAway_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), GiveAway_.name));
            }
            if (criteria.getMessageId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMessageId(), GiveAway_.messageId));
            }
            if (criteria.getTextChannelId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTextChannelId(), GiveAway_.textChannelId));
            }
            if (criteria.getFinish() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFinish(), GiveAway_.finish));
            }
            if (criteria.getWinner() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWinner(), GiveAway_.winner));
            }
            if (criteria.getExpired() != null) {
                specification = specification.and(buildSpecification(criteria.getExpired(), GiveAway_.expired));
            }
            if (criteria.getDiscordUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscordUserId(),
                    root -> root.join(GiveAway_.discordUsers, JoinType.LEFT).get(DiscordUser_.id)));
            }
            if (criteria.getGuildsettingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildsettingsId(),
                    root -> root.join(GiveAway_.guildsettings, JoinType.LEFT).get(GuildSettings_.id)));
            }
        }
        return specification;
    }
}
