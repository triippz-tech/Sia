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

import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.domain.*; // for static metamodels
import com.trievosoftware.application.repository.CustomCommandRepository;
import com.trievosoftware.application.service.dto.CustomCommandCriteria;

/**
 * Service for executing complex queries for CustomCommand entities in the database.
 * The main input is a {@link CustomCommandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomCommand} or a {@link Page} of {@link CustomCommand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomCommandQueryService extends QueryService<CustomCommand> {

    private final Logger log = LoggerFactory.getLogger(CustomCommandQueryService.class);

    private final CustomCommandRepository customCommandRepository;

    public CustomCommandQueryService(CustomCommandRepository customCommandRepository) {
        this.customCommandRepository = customCommandRepository;
    }

    /**
     * Return a {@link List} of {@link CustomCommand} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomCommand> findByCriteria(CustomCommandCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomCommand> specification = createSpecification(criteria);
        return customCommandRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CustomCommand} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomCommand> findByCriteria(CustomCommandCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomCommand> specification = createSpecification(criteria);
        return customCommandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomCommandCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomCommand> specification = createSpecification(criteria);
        return customCommandRepository.count(specification);
    }

    /**
     * Function to convert CustomCommandCriteria to a {@link Specification}
     */
    private Specification<CustomCommand> createSpecification(CustomCommandCriteria criteria) {
        Specification<CustomCommand> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CustomCommand_.id));
            }
            if (criteria.getGuildId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGuildId(), CustomCommand_.guildId));
            }
            if (criteria.getCommandName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommandName(), CustomCommand_.commandName));
            }
            if (criteria.getGuildrolesId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuildrolesId(),
                    root -> root.join(CustomCommand_.guildroles, JoinType.LEFT).get(GuildRoles_.id)));
            }
        }
        return specification;
    }
}
