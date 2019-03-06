package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.exceptions.NoBanFoundExcetion;
import com.trievosoftware.application.service.TempBansService;
import com.trievosoftware.application.domain.TempBans;
import com.trievosoftware.application.repository.TempBansRepository;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing TempBans.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class TempBansServiceImpl implements TempBansService {

    private final Logger log = LoggerFactory.getLogger(TempBansServiceImpl.class);

    private final TempBansRepository tempBansRepository;

    public TempBansServiceImpl(TempBansRepository tempBansRepository) {
        this.tempBansRepository = tempBansRepository;
    }

    /**
     * Save a tempBans.
     *
     * @param tempBans the entity to save
     * @return the persisted entity
     */
    @Override
    public TempBans save(TempBans tempBans) {
        log.debug("Request to save TempBans : {}", tempBans);
        return tempBansRepository.save(tempBans);
    }

    /**
     * Get all the tempBans.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TempBans> findAll() {
        log.debug("Request to get all TempBans");
        return tempBansRepository.findAll();
    }


    /**
     * Get one tempBans by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TempBans> findOne(Long id) {
        log.debug("Request to get TempBans : {}", id);
        return tempBansRepository.findById(id);
    }

    /**
     * Delete the tempBans by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TempBans : {}", id);
        tempBansRepository.deleteById(id);
    }

    /**
     *
     *
     * @param guildId
     * @param userId
     * @return
     */
    @Override
    public Optional<TempBans> findFirstByGuildIdAndUserId(Long guildId, Long userId) {
        log.debug("Request to get TempBans for User={} in Guild={}", guildId, userId);
        return tempBansRepository.findFirstByGuildIdAndUserId(guildId, userId);
    }

    /**
     *
     *
     * @param epochSeconds
     * @return
     */
    @Override
    public List<TempBans> findAllByFinishIsLessThan(Long epochSeconds) {
        log.debug("Request to get All Expired TempBans");
        return tempBansRepository.findAllByFinishIsLessThan(epochSeconds);
    }

    /**
     *
     *
     * @param guild
     * @param userId
     * @param finish
     */
    @Override
    public void setBan(Guild guild, long userId, Instant finish)
    {
        log.debug("Request to set Ban for Useer={} in Guild={}, Finished={}", userId, guild.getName(), finish.toString());
        Optional<TempBans> tempBansOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempBansOptional.isPresent() )
        {
            tempBansOptional.get().setFinish(finish);
            save(tempBansOptional.get());
            log.info("Updated TempBan for User={} in Guild={}, Finished={}", userId, guild.getName(), finish.toString());
        } else {
            TempBans tempBans = new TempBans();
            tempBans.setGuildId(guild.getIdLong());
            tempBans.userId(userId);
            tempBans.setFinish(finish);
            save(tempBans);
            log.info("New TempBan for User={} in Guild={}", userId, guild.getName());
        }
    }

    /**
     *
     *
     * @param guild
     * @param userId
     */
    @Override
    public void clearBan(Guild guild, long userId) throws NoBanFoundExcetion {
        log.debug("Request to clear Ban for Useer={} in Guild={}", userId, guild.getName());
        Optional<TempBans> tempBansOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempBansOptional.isPresent() )
        {
            delete(tempBansOptional.get().getId());
            log.info("Removed ban for User={} in Guild={}", userId, guild.getName());
        } else {
            throw new NoBanFoundExcetion(String.format("User={} in Guild={}, is not currently banned.", userId, guild.getName()));
        }
    }

    /**
     *
     *
     * @param guild
     * @param userId
     * @return
     */
    @Override
    public int timeUntilUnban(Guild guild, long userId)
    {
        log.debug("Request to view time until Ban expires for Useer={} in Guild={}", userId, guild.getName());
        Optional<TempBans> tempBansOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempBansOptional.isPresent() )
        {
            Instant end = tempBansOptional.get().getFinish();
            if(end==Instant.MAX)
                return Integer.MAX_VALUE;
            else
                return (int)(Instant.now().until(end, ChronoUnit.MINUTES));
        } else {
            log.info("No bans present for User={} in Guild={}", userId, guild.getName());
            return 0;
        }
    }

    /**
     *
     * @param jda
     */
    @Override
    public void checkUnbans(JDA jda)
    {
        log.debug("Request to check for expired bans for");
        List<TempBans> tempBansOptional = findAllByFinishIsLessThan(Instant.now().getEpochSecond());
        for ( TempBans tempBans : tempBansOptional )
        {
            Guild g = jda.getGuildById(tempBans.getGuildId());
            if(g==null || !g.isAvailable() || !g.getSelfMember().hasPermission(Permission.BAN_MEMBERS))
                continue;
            g.getController().unban(Long.toString(tempBans.getUserId())).reason("Temporary Ban Completed").queue(s->{}, f->{});
            delete(tempBans.getId());
        }
    }
}
