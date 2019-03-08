package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.exceptions.UserNotMutedException;
import com.trievosoftware.application.service.GuildSettingsService;
import com.trievosoftware.application.service.TempMutesService;
import com.trievosoftware.application.domain.TempMutes;
import com.trievosoftware.application.repository.TempMutesRepository;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing TempMutes.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class TempMutesServiceImpl implements TempMutesService {

    private final Logger log = LoggerFactory.getLogger(TempMutesServiceImpl.class);

    private final TempMutesRepository tempMutesRepository;

    public TempMutesServiceImpl(TempMutesRepository tempMutesRepository) {
        this.tempMutesRepository = tempMutesRepository;
    }

    /**
     * Save a tempMutes.
     *
     * @param tempMutes the entity to save
     * @return the persisted entity
     */
    @Override
    public TempMutes save(TempMutes tempMutes) {
        log.debug("Request to save TempMutes : {}", tempMutes);
        return tempMutesRepository.save(tempMutes);
    }

    /**
     * Get all the tempMutes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TempMutes> findAll() {
        log.debug("Request to get all TempMutes");
        return tempMutesRepository.findAll();
    }


    /**
     * Get one tempMutes by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TempMutes> findOne(Long id) {
        log.debug("Request to get TempMutes : {}", id);
        return tempMutesRepository.findById(id);
    }

    /**
     * Delete the tempMutes by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TempMutes : {}", id);
        tempMutesRepository.deleteById(id);
    }

    /**
     *
     * @param guildId
     * @param userId
     * @return
     */
    @Override
    public Optional<TempMutes> findFirstByGuildIdAndUserId(Long guildId, Long userId) {
        log.debug("Request to get TempMutes for User={} in Guild={}", userId, guildId);
        return tempMutesRepository.findFirstByGuildIdAndUserId(guildId, userId);
    }

    /**
     *
     * @param guildId
     * @param userId
     * @return
     */
    @Override
    public Optional<TempMutes> findFirstByGuildIdAndUserIdAndFinishGreaterThan(Long guildId,
                                                                                    Long userId,
                                                                                    Instant now) {
        log.debug("Request to get TempMutes for User={} in Guild={}", userId, guildId);
        return tempMutesRepository.findFirstByGuildIdAndUserIdAndFinishGreaterThan(guildId, userId, now);
    }

    /**
     *
     * @param now The current time
     * @return
     */
    @Override
    public List<TempMutes> findAllByFinishIsLessThan(Instant now) {
        log.debug("Request to get all TempMutes before Current Time");
        return tempMutesRepository.findAllByFinishIsLessThan(now);
    }

    /**
     *
     * @param member
     * @return
     */
    @Override
    public boolean isMuted(Member member)
    {
        Optional<TempMutes> tempMutesOptional = findFirstByGuildIdAndUserIdAndFinishGreaterThan(
            member.getGuild().getIdLong(), member.getUser().getIdLong(), Instant.now()
        );
        return tempMutesOptional.isPresent();
    }

    /**
     *
     * @param guild
     * @param userId
     * @param finish
     */
    @Override
    public void setMute(Guild guild, long userId, Instant finish)
    {
        log.debug("Request to set mute for User={} in Guild={} with a Finish={}", userId, guild.getName(), finish.toString());
        Optional<TempMutes> tempMutesOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempMutesOptional.isPresent() )
        {
            tempMutesOptional.get().setFinish(finish);
            save(tempMutesOptional.get());
            log.info("TempMute updated for User={} in Guild={}, new Finish={}", userId, guild.getName(), finish);
        } else {
            TempMutes tempMutes = new TempMutes();
            tempMutes.setGuildId(guild.getIdLong());
            tempMutes.setUserId(userId);
            tempMutes.setFinish(finish);
            save(tempMutes);
            log.info("New TempMute set for User={} in Guild={}, until {}", userId, guild.getName(), finish.toString());
        }
    }

    /**
     *
     * @param guild
     * @param userId
     * @param finish
     */
    @Override
    public void overrideMute(Guild guild, long userId, Instant finish)
    {
        log.debug("Request to Override mute for User={} in Guild={} with a Finish={}", userId, guild.getName(), finish.toString());
        Optional<TempMutes> tempMutesOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempMutesOptional.isPresent() )
        {
            tempMutesOptional.get().setFinish(finish);
            save(tempMutesOptional.get());
            log.info("TempMute Overriden for User={} in Guild={}, new Finish={}", userId, guild.getName(), finish.toString());
        } else {
            TempMutes tempMutes = new TempMutes();
            tempMutes.setGuildId(guild.getIdLong());
            tempMutes.setUserId(userId);
            tempMutes.setFinish(finish);
            save(tempMutes);
            log.info("Override TempMute set for User={} in Guild={}, until {}", userId, guild.getName(), finish.toString());
        }
    }

    /**
     *
     *
     * @param guild
     * @param userId
     */
    @Override
    public void removeMute(Guild guild, long userId) throws UserNotMutedException {
        log.debug("Request to Remove mute for User={} in Guild={}", userId, guild.getName());
        Optional<TempMutes> tempMutesOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempMutesOptional.isPresent() )
        {
            delete(tempMutesOptional.get().getId());
            log.info("TempMute removed from User={} in Guild={}", userId, guild.getName());
        } else {
            throw new UserNotMutedException(String.format("User=%d in Guild=%s is not muted", userId, guild.getName()));
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
    public int timeUntilUnmute(Guild guild, long userId)
    {
        log.debug("Request to view timeleft on mute for User={} in Guild={}", userId, guild.getName());
        Optional<TempMutes> tempMutesOptional = findFirstByGuildIdAndUserId(guild.getIdLong(), userId);
        if ( tempMutesOptional.isPresent() )
        {
            Instant end = tempMutesOptional.get().getFinish();
            if(end.getEpochSecond() == Instant.MAX.getEpochSecond())
                return Integer.MAX_VALUE;
            else
                return (int)(Instant.now().until(end, ChronoUnit.MINUTES));
        } else {
            log.warn("User={} in Guild={} is not muted", userId, guild.getName());
            return 0;
        }
    }

    /**
     *
     * @param jda
     * @param guildSettingsService
     */
    @Override
    public void checkUnmutes(JDA jda, GuildSettingsService guildSettingsService)
    {
        log.debug("Request to check unmutes");
        List<TempMutes> tempMutesList = findAllByFinishIsLessThan(Instant.now());
        for ( TempMutes tempMutes:  tempMutesList )
        {
            Guild g = jda.getGuildById(tempMutes.getGuildId());
            if(g==null || !g.isAvailable() || !g.getSelfMember().hasPermission(Permission.MANAGE_ROLES))
                continue;
            Role mRole = guildSettingsService.getSettings(g).getMutedRole(g);
            if(mRole==null || !g.getSelfMember().canInteract(mRole))
            {
                delete(tempMutes.getId());
                continue;
            }
            Member m = g.getMemberById(tempMutes.getUserId());
            if(m!=null && m.getRoles().contains(mRole))
                g.getController().removeSingleRoleFromMember(m, mRole).reason("Temporary Mute Completed").queue();
            delete(tempMutes.getId());
        }
    }
}
