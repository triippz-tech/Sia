package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.CustomCommand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CustomCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomCommandRepository extends JpaRepository<CustomCommand, Long>, JpaSpecificationExecutor<CustomCommand> {
    List<CustomCommand> findAllByGuildId(Long guildId);
    Optional<CustomCommand> findByGuildIdAndCommandName(Long guildId, String commandName);
}
