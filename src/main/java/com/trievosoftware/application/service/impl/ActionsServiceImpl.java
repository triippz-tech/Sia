package com.trievosoftware.application.service.impl;

import com.trievosoftware.application.domain.Punishment;
import com.trievosoftware.application.exceptions.NoActionsExceptions;
import com.trievosoftware.application.service.ActionsService;
import com.trievosoftware.application.domain.Actions;
import com.trievosoftware.application.repository.ActionsRepository;
import com.trievosoftware.discord.Action;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Actions.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class ActionsServiceImpl implements ActionsService {

    private final Logger log = LoggerFactory.getLogger(ActionsServiceImpl.class);

    private final ActionsRepository actionsRepository;

    public static final int MAX_STRIKES = 100;
    public static final int MAX_SET = 20;
    private static final String STRIKES_TITLE = Action.STRIKE.getEmoji()+" Punishments";

    public static final String DEFAULT_SETUP_MESSAGE = "\n" + Constants.WARNING + " It looks like you've set up some " +
        "automoderation without assigning any punishments! "
        + "I've gone ahead and set up some default punishments; you can see the settings with `" + Constants.PREFIX
        + "settings` and set or change any punishments with the `" + Constants.PREFIX+"punishment` command!";
    private static final int[] DEFAULT_STRIKE_COUNTS = {2,               3,               4,           5,              6};
    private static final Action[] DEFAULT_ACTIONS =    {Action.TEMPMUTE, Action.TEMPMUTE, Action.KICK, Action.TEMPBAN, Action.BAN};
    private static final int[] DEFAULT_TIMES =         {10,              120,             0,           1440,           0};

    public ActionsServiceImpl(ActionsRepository actionsRepository) {
        this.actionsRepository = actionsRepository;
    }

    /**
     * Save a actions.
     *
     * @param actions the entity to save
     * @return the persisted entity
     */
    @Override
    public Actions save(Actions actions) {
        log.debug("Request to save Actions : {}", actions);
        return actionsRepository.save(actions);
    }

    /**
     * Get all the actions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Actions> findAll() {
        log.debug("Request to get all Actions");
        return actionsRepository.findAll();
    }


    /**
     * Get one actions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Actions> findOne(Long id) {
        log.debug("Request to get Actions : {}", id);
        return actionsRepository.findById(id);
    }

    /**
     * Delete the actions by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actions : {}", id);
        actionsRepository.deleteById(id);
    }

    /**
     * Get all the actions.
     *
     * @param guildId the Guild to look for
     * @return the list of entities
     */
    @Override
    public List<Actions> findAllByGuildId(long guildId) {
        log.debug("Request to get all Actions for Guild={}", guildId);
        return actionsRepository.findAllByGuildId(guildId);
    }

    /**
     *
     * @param guildId
     * @param numStrikes
     * @return
     */
    @Override
    public List<Actions> findAllByGuildIdAndNumStrikesIs(Long guildId, Integer numStrikes) {
        log.debug("Request to get all Actions for Guild={} with NumStrikes={}", guildId, numStrikes);
        return actionsRepository.findAllByGuildIdAndNumStrikesIs(guildId, numStrikes);
    }

    /**
     *
     * @param guild
     * @return
     */
    @Override
    public boolean useDefaultSettings(Guild guild) // only activates if none set
    {
        log.debug("Request to use default Actions settings for Guild={}", guild.getName());
        List<Actions> actionsList = findAllByGuildId(guild.getIdLong());
        if (!actionsList.isEmpty())
            return false;
        else {
            for (int i = 0; i < DEFAULT_STRIKE_COUNTS.length; i++ )
            {
                Actions actions = new Actions();
                actions.setAction(DEFAULT_ACTIONS[i].getBit());
                actions.setGuildId(guild.getIdLong());
                actions.setNumStrikes(DEFAULT_STRIKE_COUNTS[i]);
                actions.setTime(DEFAULT_TIMES[i]);
                save(actions);
            }
        }
        log.info("Added new default settings for Guild={}", guild.getName());
        return true;
    }

    /**
     *
     * @param guild
     * @param numStrikes
     */
    @Override
    public void removeAction(Guild guild, int numStrikes) throws NoActionsExceptions
    {
        log.debug("Request to remove Action for Guild={}", guild.getName());
        List<Actions> actionsList = findAllByGuildIdAndNumStrikesIs(guild.getIdLong(), numStrikes);
        if (!actionsList.isEmpty()) {
            for (Actions actions: actionsList) {
                delete(actions.getId());
                log.info("Deleted Action={}", actions.getId());
            }
        } else {
            throw new NoActionsExceptions(String.format("Guild=%s has no strikes", guild.getName()));
        }
    }

    /**
     *
     * @param guild
     * @param numStrikes
     * @param action
     */
    @Override
    public void setAction(Guild guild, int numStrikes, Action action)
    {
        setAction(guild, numStrikes, action, 0);
    }

    /**
     *
     * @param guild
     * @param numStrikes
     * @param action
     * @param time
     */
    @Override
    public void setAction(Guild guild, int numStrikes, Action action, int time)
    {
        log.debug("Request to set Actions for Guild={}", guild.getName());
        Action act;
        if(action==Action.MUTE && time>0)
            act = Action.TEMPMUTE;
        else if(action==Action.TEMPMUTE && time==0)
            act = Action.MUTE;
        else if(action==Action.BAN && time>0)
            act = Action.TEMPBAN;
        else if(action==Action.TEMPBAN && time==0)
            act = Action.BAN;
        else
            act = action;
        List<Actions> list = findAllByGuildIdAndNumStrikesIs(guild.getIdLong(), numStrikes);
        if ( !list.isEmpty() ) {
            for ( Actions actions: list ) {
                actions.setTime(time);
                actions.setAction(act.getBit());
                save(actions);
                log.info("Updated Actions={} for Guild={}", actions.getId(), guild.getName());
            }
        } else {
            Actions actions = new Actions();
            actions.setGuildId(guild.getIdLong());
            actions.setNumStrikes(numStrikes);
            actions.setAction(act.getBit());
            actions.setTime(time);
            save(actions);
            log.info("New Action added for Guild={}", guild.getName());
        }
    }

    /**
     *
     * @param guild
     * @return
     */
    @Override
    public List<Punishment> getAllPunishments(Guild guild) throws NoActionsExceptions
    {
        log.debug("Request to get all Punishments for Guild={}", guild.getName());
        List<Actions> actionsList = findAllByGuildId(guild.getIdLong());
        if ( !actionsList.isEmpty() ) {
            List<Punishment> list = new LinkedList<>();
            for ( Actions actions: actionsList ) {
                list.add(new Punishment(
                    actions.getAction(),
                    actions.getNumStrikes(),
                    actions.getTime()
                ));
            }
            return list;
        } else {
            throw new NoActionsExceptions(String.format("Guild=%s, has no actions listed", guild.getName()));
        }
    }

    /**
     *
     * @param guild
     * @param from
     * @param to
     * @return
     */
    @Override
    public List getPunishments(Guild guild, int from, int to)
    {
        List<Punishment> punishments = null;
        try {
            punishments = getAllPunishments(guild);
        } catch (NoActionsExceptions e) {
            log.warn("{}", e.getMessage());
            return Collections.EMPTY_LIST;
        }

        List<Punishment> filtered = punishments.stream().filter(p -> p.numStrikes>from && p.numStrikes<=to).collect(Collectors.toList());
        if(!filtered.isEmpty())
            return filtered;
        Punishment max = punishments.get(0);
        for(Punishment p: punishments)
            if(p.numStrikes>max.numStrikes)
                max = p;
        if(from>=max.numStrikes)
            return Collections.singletonList(max);
        return Collections.EMPTY_LIST;
    }

    /**
     *
     * @param guild
     * @return
     */
    @Override
    public MessageEmbed.Field getAllPunishmentsDisplay(Guild guild)
    {
        List<Punishment> all = null;
        try {
            all = getAllPunishments(guild);
        } catch (NoActionsExceptions noActionsExceptions) {
            log.warn("{}", noActionsExceptions.getMessage());
            return new MessageEmbed.Field(STRIKES_TITLE, "No strikes set!", true);
        }

        all.sort(Comparator.comparingInt(a -> a.numStrikes));
        StringBuilder sb = new StringBuilder();
        all.forEach(p -> sb.append("\n`").append(p.numStrikes).append(" ").append(Action.STRIKE.getEmoji()).append("`: **")
            .append(FormatUtil.capitalize(p.action.name())).append("** ").append(p.action.getEmoji())
            .append(p.time>0 ? " "+FormatUtil.secondsToTimeCompact(p.time*60) : ""));
        return new MessageEmbed.Field(STRIKES_TITLE, sb.toString().trim(), true);
    }


}
