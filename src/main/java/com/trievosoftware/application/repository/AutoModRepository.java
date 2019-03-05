package com.trievosoftware.application.repository;

import com.trievosoftware.application.domain.AutoMod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the AutoMod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoModRepository extends JpaRepository<AutoMod, Long> {
    public Optional<AutoMod> findByGuildId(Long guildId);
}
