package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.service.StrikesService;
import com.trievosoftware.application.domain.Strikes;
import com.trievosoftware.application.repository.StrikesRepository;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Strikes.
 */
@Service
@Transactional
public class StrikesServiceImpl implements StrikesService {

    private final Logger log = LoggerFactory.getLogger(StrikesServiceImpl.class);

    private final StrikesRepository strikesRepository;

    public StrikesServiceImpl(StrikesRepository strikesRepository) {
        this.strikesRepository = strikesRepository;
    }

    /**
     * Save a strikes.
     *
     * @param strikes the entity to save
     * @return the persisted entity
     */
    @Override
    public Strikes save(Strikes strikes) {
        log.debug("Request to save Strikes : {}", strikes);
        return strikesRepository.save(strikes);
    }

    /**
     * Get all the strikes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Strikes> findAll() {
        log.debug("Request to get all Strikes");
        return strikesRepository.findAll();
    }


    /**
     * Get one strikes by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Strikes> findOne(Long id) {
        log.debug("Request to get Strikes : {}", id);
        return strikesRepository.findById(id);
    }

    /**
     * Delete the strikes by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Strikes : {}", id);
        strikesRepository.deleteById(id);
    }

    /**
     * Get all the strikes for a user in a specific guild.
     *
     * @return the list of entities
     */
    @Override
    public Optional<Strikes> findAllByGuildIdAndUserId(Long guildId, Long userId) {
        log.debug("Request to get all Strikes for User={} in Guild={}", userId, guildId);
        return strikesRepository.findByGuildIdAndUserId(guildId, userId);
    }

    /**
     * Adds strikes to a particular user
     *
     * @param guild Guild where the user is receiving strikes
     * @param targetId the user's ID
     * @param strikes number of strikes
     * @return
     */
    @Override
    public int[] addStrikes(Guild guild, long targetId, int strikes)
    {
        log.debug("Request to add Strike(s)={} to User={} in Guild={}", strikes, targetId, guild.getName());
        Optional<Strikes> strikesOptional = findAllByGuildIdAndUserId(guild.getIdLong(), targetId);
        if ( strikesOptional.isPresent() )
        {
            int currentStrikes = strikesOptional.get().getStrikes();
            int i = currentStrikes + strikes < 0 ? 0 : currentStrikes + strikes;
            strikesOptional.get().setStrikes(i);
            save(strikesOptional.get());
            log.info("Strike(s)={} addeed to User={} in Guild={}", strikes, targetId, guild.getName());
            return new int[]{currentStrikes, i};
        } else {
            Strikes tempStrikes = new Strikes();
            tempStrikes.setGuildId(guild.getIdLong());
            tempStrikes.setUserId(targetId);
            tempStrikes.setStrikes(strikes<0 ? 0 : strikes);
            save(tempStrikes);
            log.info("Strike(s)={} addeed to User={} in Guild={}", strikes, targetId, guild.getName());
            return new int[]{0, strikes<0 ? 0 : strikes};
        }
    }

    /**
     *
     * @param target
     * @param strikes
     * @return
     */
    @Override
    public int[] removeStrikes(Member target, int strikes)
    {
        return removeStrikes(target.getGuild(), target.getUser().getIdLong(), strikes);
    }

    /**
     *
     * @param guild
     * @param targetId
     * @param strikes
     * @return
     */
    @Override
    public int[] removeStrikes(Guild guild, long targetId, int strikes)
    {
        log.debug("Request to remove Strike(s)={} for User={} in Guild={}", strikes, targetId, guild.getName());
        return addStrikes(guild, targetId, -1*strikes);
    }

    /**
     *
     * @param target
     * @return
     */
    @Override
    public int getStrikes(Member target)
    {
        return getStrikes(target.getGuild(), target.getUser().getIdLong());
    }

    /**
     *
     * @param guild
     * @param targetId
     * @return
     */
    @Override
    public int getStrikes(Guild guild, long targetId)
    {
        Optional<Strikes> strikesOptional = findAllByGuildIdAndUserId(guild.getIdLong(), targetId);
        if ( strikesOptional.isPresent() )
        {
            return strikesOptional.get().getStrikes();
        } else {
            return 0;
        }
    }
}
