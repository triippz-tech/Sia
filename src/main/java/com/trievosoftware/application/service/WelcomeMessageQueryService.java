package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.GuildSettings_;
import com.trievosoftware.application.domain.WelcomeMessage;
import com.trievosoftware.application.domain.WelcomeMessage_;
import com.trievosoftware.application.repository.WelcomeMessageRepository;
import com.trievosoftware.application.service.dto.WelcomeMessageCriteria;
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
 * Service for executing complex queries for WelcomeMessage entities in the database.
 * The main input is a {@link WelcomeMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WelcomeMessage} or a {@link Page} of {@link WelcomeMessage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WelcomeMessageQueryService extends QueryService<WelcomeMessage> {

    private final Logger log = LoggerFactory.getLogger(WelcomeMessageQueryService.class);

    private final WelcomeMessageRepository welcomeMessageRepository;

    public WelcomeMessageQueryService(WelcomeMessageRepository welcomeMessageRepository) {
        this.welcomeMessageRepository = welcomeMessageRepository;
    }

    /**
     * Return a {@link List} of {@link WelcomeMessage} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WelcomeMessage> findByCriteria(WelcomeMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WelcomeMessage> specification = createSpecification(criteria);
        return welcomeMessageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WelcomeMessage} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WelcomeMessage> findByCriteria(WelcomeMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WelcomeMessage> specification = createSpecification(criteria);
        return welcomeMessageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WelcomeMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WelcomeMessage> specification = createSpecification(criteria);
        return welcomeMessageRepository.count(specification);
    }

    /**
     * Function to convert WelcomeMessageCriteria to a {@link Specification}
     */
    private Specification<WelcomeMessage> createSpecification(WelcomeMessageCriteria criteria) {
        Specification<WelcomeMessage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WelcomeMessage_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), WelcomeMessage_.name));
            }
            if (criteria.getMessageTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessageTitle(), WelcomeMessage_.messageTitle));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), WelcomeMessage_.body));
            }
            if (criteria.getFooter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFooter(), WelcomeMessage_.footer));
            }
            if (criteria.getWebsiteUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsiteUrl(), WelcomeMessage_.websiteUrl));
            }
            if (criteria.getLogoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogoUrl(), WelcomeMessage_.logoUrl));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), WelcomeMessage_.active));
            }
            if (criteria.getGuildsettingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildsettingsId(),
                    root -> root.join(WelcomeMessage_.guildsettings, JoinType.LEFT).get(GuildSettings_.id)));
            }
        }
        return specification;
    }
}
