package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.service.IgnoredService;
import com.trievosoftware.application.domain.Ignored;
import com.trievosoftware.application.repository.IgnoredRepository;
import com.trievosoftware.discord.utils.FixedCache;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Ignored.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class IgnoredServiceImpl implements IgnoredService {

    private final Logger log = LoggerFactory.getLogger(IgnoredServiceImpl.class);

    private final IgnoredRepository ignoredRepository;

    private final FixedCache<Long, Set<Long>> cache = new FixedCache<>(1000);

    public IgnoredServiceImpl(IgnoredRepository ignoredRepository) {
        this.ignoredRepository = ignoredRepository;
    }

    /**
     * Save a ignored.
     *
     * @param ignored the entity to save
     * @return the persisted entity
     */
    @Override
    public Ignored save(Ignored ignored) {
        log.debug("Request to save Ignored : {}", ignored);
        return ignoredRepository.save(ignored);
    }

    /**
     * Get all the ignoreds.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ignored> findAll() {
        log.debug("Request to get all Ignoreds");
        return ignoredRepository.findAll();
    }


    /**
     * Get one ignored by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ignored> findOne(Long id) {
        log.debug("Request to get Ignored : {}", id);
        return ignoredRepository.findById(id);
    }

    /**
     * Delete the ignored by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ignored : {}", id);
        ignoredRepository.deleteById(id);
    }

    /**
     * Get one ignored by entityId.
     *
     * @param entityId the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Ignored> findByEntityId(Long entityId) {
        log.debug("Request to get Ignored for entity: {}", entityId);
        return ignoredRepository.findByEntityId(entityId);
    }

    /**
     * Get one ignored by entityId.
     *
     * @param guildId the id of the entity
     * @param type the type of the entity
     * @return the entity
     */
    @Override
    public List<Ignored> findByGuildIdAndType(Long guildId, int type) {
        log.debug("Request to get Ignored for guild: {} with type={}", guildId, type);
        return ignoredRepository.findByGuildIdAndType(guildId, type);
    }

    /**
     * Checks to see if the text channel is ignored.
     * @param tc the text channel
     * @return ignored or not
     */
    @Override
    public boolean isIgnored(TextChannel tc)
    {
        return findByEntityId(tc.getIdLong()).isPresent();
    }

    /**
     *
     * @param member
     * @return
     */
    @Override
    public boolean isIgnored(Member member)
    {
        if(cache.contains(member.getGuild().getIdLong()))
        {
            for(long rid: cache.get(member.getGuild().getIdLong()))
                if(member.getRoles().stream().anyMatch(r -> r.getIdLong()==rid))
                    return true;
            return false;
        }
        List<Ignored> ignored = findByGuildIdAndType( member.getGuild().getIdLong(), Type.ROLE.ordinal() );
        return !ignored.isEmpty();
    }

    /**
     *
     * @param tc
     * @return
     */
    @Override
    public boolean ignore(TextChannel tc)
    {
        Optional<Ignored> ignore = findByEntityId(tc.getIdLong());
        if ( ignore.isPresent() ) {
            return false;
        } else {
            log.info("No Ignored present for TextChannel={} for Guild={}, creating new", tc.getName(), tc.getGuild().getName());
            Ignored ignored = new Ignored();
            ignored.setGuildId(tc.getGuild().getIdLong());
            ignored.setEntityId(tc.getIdLong());
            ignored.setType(Type.TEXT_CHANNEL.ordinal());
            save(ignored);
            return true;
        }
    }

    /**
     *
     * @param role
     * @return
     */
    @Override
    public boolean ignore(Role role)
    {
        invalidateCache(role.getGuild());
        Optional<Ignored> ignoredOptional = findByEntityId(role.getIdLong());
        if ( ignoredOptional.isPresent() ) {
            return false;
        } else {
            Ignored ignored = new Ignored();
            ignored.setGuildId(role.getGuild().getIdLong());
            ignored.setEntityId(role.getIdLong());
            ignored.setType(Type.ROLE.ordinal());
            save(ignored);
            return true;
        }
    }

    /**
     *
     * @param guild
     * @return
     */
    @Override
    public List<TextChannel> getIgnoredChannels(Guild guild)
    {
        List<Ignored> ignoredOptional = findByGuildIdAndType(guild.getIdLong(), Type.TEXT_CHANNEL.ordinal());
        List<TextChannel> list = new LinkedList<>();

        for (Ignored ignored: ignoredOptional) {
            TextChannel tc = guild.getTextChannelById(ignored.getEntityId());
            if ( tc!= null )
                list.add(tc);
        }
        return list;
    }

    /**
     *
     * @param guild
     * @return
     */
    @Override
    public List<Role> getIgnoredRoles(Guild guild)
    {
        if(cache.contains(guild.getIdLong()))
        {
            List<Role> list = new LinkedList<>();
            for(long rid: cache.get(guild.getIdLong()))
            {
                Role role = guild.getRoleById(rid);
                if(role!=null)
                    list.add(role);
            }
            return list;
        }

        List<Ignored> ignoredOptional = findByGuildIdAndType(guild.getIdLong(), Type.ROLE.ordinal());
        List<Role> list = new LinkedList<>();

        for ( Ignored ignored: ignoredOptional ){
            Role role = guild.getRoleById(ignored.getEntityId());
            if(role!=null)
                list.add(role);
        }
        return list;
    }

    /**
     *
     * @param tc
     * @return
     */
    @Override
    public boolean unignore(TextChannel tc)
    {
        Optional<Ignored> ignoredOptional = findByEntityId(tc.getIdLong());
        if (ignoredOptional.isPresent()) {
            delete(ignoredOptional.get().getId());
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param role
     * @return
     */
    @Override
    public boolean unignore(Role role)
    {
        invalidateCache(role.getGuild());
        Optional<Ignored> ignoredOptional = findByEntityId(role.getIdLong());
        if (ignoredOptional.isPresent()) {
            delete(ignoredOptional.get().getId());
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    private enum Type
    {
        TEXT_CHANNEL, ROLE
    }

    /**
     * 
     * @param guild
     */
    private void invalidateCache(Guild guild)
    {
        cache.pull(guild.getIdLong());
    }
}
