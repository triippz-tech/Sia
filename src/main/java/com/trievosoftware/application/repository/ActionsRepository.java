package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.Actions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Actions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionsRepository extends JpaRepository<Actions, Long> {
    List<Actions> findAllByGuildId(Long guildId);
    List<Actions> findAllByGuildIdAndNumStrikesIs(Long guildId, Integer numStrikes);
}
