package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.exceptions.NoDiscordUserFoundException;
import com.trievosoftware.application.service.DiscordUserService;
import com.trievosoftware.application.domain.DiscordUser;
import com.trievosoftware.application.repository.DiscordUserRepository;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DiscordUser.
 */
@Service
@Transactional
public class DiscordUserServiceImpl implements DiscordUserService {

    private final Logger log = LoggerFactory.getLogger(DiscordUserServiceImpl.class);

    private final DiscordUserRepository discordUserRepository;

    public DiscordUserServiceImpl(DiscordUserRepository discordUserRepository) {
        this.discordUserRepository = discordUserRepository;
    }

    /**
     * Save a discordUser.
     *
     * @param discordUser the entity to save
     * @return the persisted entity
     */
    @Override
    public DiscordUser save(DiscordUser discordUser) {
        log.debug("Request to save DiscordUser : {}", discordUser);
        return discordUserRepository.save(discordUser);
    }

    /**
     * Get all the discordUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DiscordUser> findAll(Pageable pageable) {
        log.debug("Request to get all DiscordUsers");
        return discordUserRepository.findAll(pageable);
    }


    /**
     * Get one discordUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DiscordUser> findOne(Long id) {
        log.debug("Request to get DiscordUser : {}", id);
        return discordUserRepository.findById(id);
    }

    /**
     * Delete the discordUser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DiscordUser : {}", id);
        discordUserRepository.deleteById(id);
    }

    /**
     * Get one discordUser by id.
     *
     * @param userId the id of the user
     * @return the entity
     */
    @Override
    @Transactional()
    public Optional<DiscordUser> findByUserId(Long userId) {
        log.debug("Request to get DiscordUser : {}", userId);
        return discordUserRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public boolean userExists(Long userId)
    {
        log.debug("Request to see if user exists");
        Optional<DiscordUser> user = findByUserId(userId);
        return user.isPresent();
    }


    @Override
    @Transactional
    public void addNewUser(Long userId)
    {
        log.debug("Request to add new user");
        if ( userExists(userId) ) return;

        DiscordUser user = new DiscordUser(userId);
        save(user);
    }

    @Override
    @Transactional
    public DiscordUser addNewUser(User user)
    {
        log.debug("Request to add new user");

        DiscordUser discordUser = new DiscordUser(user.getIdLong());
        save(discordUser);
        return discordUser;
    }

    @Override
    @Transactional
    public DiscordUser getDiscordUser(User user)
    {
        log.debug("Request to get DiscordUser with id of: {}", user.getIdLong());

        Optional<DiscordUser>  discordUser = findByUserId(user.getIdLong());

        return discordUser.orElseGet(() -> addNewUser(user));

    }

    @Override
    @Transactional
    public DiscordUser getDiscordUserById(Long id, User user)
    {
        log.debug("Request to get DiscordUser with ID={}", id);

        Optional<DiscordUser> discordUser = findOne(id);

        return discordUser.orElseGet(() -> addNewUser(user));
    }

    @Override
    @Transactional
    public void blacklistUser(User user)
    {
        log.debug("Request to blacklist User={}:{}", user.getName(), user.getIdLong());
        Optional<DiscordUser> discordUser = findByUserId(user.getIdLong());
        if ( !discordUser.isPresent() )
        {
            DiscordUser newUser = new DiscordUser(user.getIdLong(), true);
            save(newUser);
            log.info("No User named {} found in Sia database. New entry created with Blacklisted flag set to TRUE", user.getName());
            return;
        }

        discordUser.get().setBlacklisted(true);
        save(discordUser.get());
        log.info("User={} has been blacklisted", user.getName());
    }


    @Override
    @Transactional
    public void removeBlacklist(User user) throws NoDiscordUserFoundException {
        log.debug("Request to remove blacklist from User={}:{}", user.getName(), user.getIdLong());
        Optional<DiscordUser> discordUser = findByUserId(user.getIdLong());
        if ( !discordUser.isPresent() )
            throw new NoDiscordUserFoundException(String.format(
                "User `%s` is not blacklisted", user.getName()
            ));

        discordUser.get().setBlacklisted(false);
        log.info("User={}:{} is not longer blacklisted", user.getName(), user.getIdLong());
    }


    @Override
    @Transactional
    public void addCommand(User user)
    {
        log.debug("Request to add command count for User={}", user.getName());
        Optional<DiscordUser> discordUser = findByUserId(user.getIdLong());
        if ( !discordUser.isPresent() )
        {
            DiscordUser discordUserNew = addNewUser(user);
            discordUserNew.setCommandsIssued(1);
            save(discordUserNew);
            return;
        }

        discordUser.get().setCommandsIssued(1);
    }

    @Override
    @Transactional
    public boolean isUserBlacklisted(Long userid)
    {
        log.debug("Request to see if User={} is blacklisted", userid);
        Optional<DiscordUser> user = findByUserId(userid);
        if ( !user.isPresent() )
        {
            addNewUser(userid);
            return false;
        }

        return user.get().isBlacklisted();
    }
}
