/*
 * Copyright 2018 Mark Tripoli (mark.tripoli@trievosoftware.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trievosoftware.discord;

import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.exceptions.*;
import com.trievosoftware.discord.logging.MessageCache.CachedMessage;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.bot.entities.ApplicationInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA.ShardInfo;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.events.role.RoleCreateEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
@SuppressWarnings("Duplicates")
public class Listener implements EventListener
{
    private final static Logger LOG = LoggerFactory.getLogger("Listener");
    private final Sia sia;
    private final String defaultPrefix;
    
    Listener(Sia sia)
    {
        this.sia = sia;
        this.defaultPrefix = sia.getApplicationProperties().getDiscord().getPrefix();
    }

    @Override
    public void onEvent(Event event)
    {
        if (event instanceof GuildMessageReceivedEvent)
        {
            Message m = ((GuildMessageReceivedEvent)event).getMessage();
            
            if(!m.getAuthor().isBot()) // ignore bot messages
            {
                // Store the message
                sia.getMessageCache().putMessage(m);
                
                // Run automod on the message
                sia.getAutoMod().performAutomod(m);

                // See if the message is a CustomCommand
                GuildSettings guildSettings = sia.getServiceManagers().getGuildSettingsService().getSettings(((GuildMessageReceivedEvent) event).getGuild());
                if ( guildSettings.getPrefix().isEmpty() )
                    return;

                // Make sure we dont pick up the bot's prefix
                String msgPrefix = m.getContentStripped().substring(0, defaultPrefix.length());
                if ( msgPrefix.equalsIgnoreCase(defaultPrefix))
                    return;

                Message message = null;
                try {
                    message = sia.getServiceManagers().getCustomCommandService()
                        .checkCustomCommand(sia,
                            ((GuildMessageReceivedEvent) event).getMember().getRoles(),
                            ((GuildMessageReceivedEvent) event).getGuild(),
                            sia.getServiceManagers().getGuildSettingsService().getSettings(((GuildMessageReceivedEvent) event).getGuild()),
                            m);
                    ((GuildMessageReceivedEvent) event).getChannel().sendMessage(message).queue();
                }
                catch (CustomCommandException.NoCommandExistsException e) {
                    ((GuildMessageReceivedEvent) event).getChannel().sendMessage( Constants.ERROR + " `" + m.getContentStripped() + "`"
                        + " is not a valid custom command").queue();
                    return;
                }
                catch (CustomCommandException.NoPrefixSetException e1) { return; } //ignore it since its not a command
                catch (CustomCommandException.InvalidRolePermissionException e) {
                    ((GuildMessageReceivedEvent) event).getChannel().sendMessage(Constants.ERROR + " " + e.getMessage()).queue();
                    return;
                }

            }
        }
        else if (event instanceof GuildMessageUpdateEvent)
        {
            Message m = ((GuildMessageUpdateEvent)event).getMessage();
            
            if(!m.getAuthor().isBot()) // ignore bot edits
            {
                // Run automod on the message
                sia.getAutoMod().performAutomod(m);
                
                // Store and log the edit
                CachedMessage old = sia.getMessageCache().putMessage(m);
                sia.getBasicLogger().logMessageEdit(m, old);
            }
        }
        else if (event instanceof GuildMessageDeleteEvent)
        {
            GuildMessageDeleteEvent gevent = (GuildMessageDeleteEvent) event;
            
            // Log the deletion
            CachedMessage old = sia.getMessageCache().pullMessage(gevent.getGuild(), gevent.getMessageIdLong());
            sia.getBasicLogger().logMessageDelete(old);
        }
        else if (event instanceof MessageBulkDeleteEvent)
        {
            MessageBulkDeleteEvent gevent = (MessageBulkDeleteEvent) event;
            
            // Get the messages we had cached
            List<CachedMessage> logged = gevent.getMessageIds().stream()
                    .map(id -> sia.getMessageCache().pullMessage(gevent.getGuild(), Long.parseLong(id)))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // Log the deletion
            sia.getBasicLogger().logMessageBulkDelete(logged, gevent.getMessageIds().size(), gevent.getChannel());
        }
        else if (event instanceof RoleCreateEvent)
        {
            sia.getServiceManagers().guildRolesService().addRole(((RoleCreateEvent) event).getRole());
        }
        else if (event instanceof RoleDeleteEvent)
        {
            sia.getServiceManagers().guildRolesService().removeRole(((RoleDeleteEvent) event).getRole());
        }
        else if (event instanceof RoleUpdateNameEvent)
        {
            sia.getServiceManagers().guildRolesService().updateRole(((RoleUpdateNameEvent) event).getRole());
        }
        else if (event instanceof GuildMemberJoinEvent)
        {
            GuildMemberJoinEvent gevent = (GuildMemberJoinEvent) event;
            
            // Log the join
            sia.getBasicLogger().logGuildJoin(gevent);
            
            // Perform automod on the newly-joined member
            sia.getAutoMod().memberJoin(gevent);

            // Display welcome message
            try {
                Message message = sia.getServiceManagers().getWelcomeMessageService().displayActiveWelcomeMessage(
                    (GuildMemberJoinEvent) event,
                    sia.getServiceManagers().getGuildSettingsService().getSettings(((GuildMemberJoinEvent) event).getGuild()));

                ((GuildMemberJoinEvent) event).getGuild().getDefaultChannel().sendMessage(message).complete();
            } catch (NoWelcomeMessageFound | NullPointerException ignored) { }
        }
        else if (event instanceof GuildMemberLeaveEvent)
        {
            GuildMemberLeaveEvent gmle = (GuildMemberLeaveEvent)event;
            
            // Log the member leaving
            sia.getBasicLogger().logGuildLeave(gmle);
            
            // Signal the modlogger because someone might have been kicked
            sia.getModLogger().setNeedUpdate(gmle.getGuild());
        }
        else if (event instanceof GuildBanEvent)
        {
            // Signal the modlogger because someone was banned
            sia.getModLogger().setNeedUpdate(((GuildBanEvent) event).getGuild());
        }
        else if (event instanceof GuildUnbanEvent)
        {
            GuildUnbanEvent gue = (GuildUnbanEvent) event;
            // Signal the modlogger because someone was unbanned
            try {
                sia.getServiceManagers().getTempBansService().clearBan(gue.getGuild(), gue.getUser().getIdLong());
            } catch (NoBanFoundExcetion noBanFoundExcetion) {
                noBanFoundExcetion.printStackTrace();
            }
            sia.getModLogger().setNeedUpdate((gue).getGuild());
        }
        else if (event instanceof GuildMemberRoleAddEvent)
        {
            GuildMemberRoleAddEvent gmrae = (GuildMemberRoleAddEvent) event;
            
            // Signal the modlogger if someone was muted
            Role mRole = sia.getServiceManagers().getGuildSettingsService().getSettings(gmrae.getGuild()).getMutedRole(gmrae.getGuild());
            if(gmrae.getRoles().contains(mRole))
                sia.getModLogger().setNeedUpdate(gmrae.getGuild());
        }
        else if (event instanceof GuildMemberRoleRemoveEvent)
        {
            GuildMemberRoleRemoveEvent gmrre = (GuildMemberRoleRemoveEvent) event;
            
            // Signal the modlogger if someone was unmuted
            Role mRole = sia.getServiceManagers().getGuildSettingsService().getSettings(gmrre.getGuild()).getMutedRole(gmrre.getGuild());
            if(gmrre.getRoles().contains(mRole))
            {
                try {
                    sia.getServiceManagers().getTempMutesService().removeMute(gmrre.getGuild(), gmrre.getUser().getIdLong());
                } catch (UserNotMutedException e) {
                    e.printStackTrace();
                }
                sia.getModLogger().setNeedUpdate(gmrre.getGuild());
            }
        }
        else if (event instanceof UserUpdateNameEvent)
        {
            UserUpdateNameEvent unue = (UserUpdateNameEvent)event;
            // Log the name change
            sia.getBasicLogger().logNameChange(unue);
            unue.getUser().getMutualGuilds().stream().map(g -> g.getMember(unue.getUser())).forEach(m -> sia.getAutoMod().dehoist(m));
        }
        else if (event instanceof UserUpdateDiscriminatorEvent)
        {
            sia.getBasicLogger().logNameChange((UserUpdateDiscriminatorEvent)event);
        }
        else if (event instanceof GuildMemberNickChangeEvent)
        {
            sia.getAutoMod().dehoist(((GuildMemberNickChangeEvent) event).getMember());
        }
//        else if (event instanceof UserUpdateAvatarEvent)
//        {
//            UserUpdateAvatarEvent uaue = (UserUpdateAvatarEvent)event;
//
//            // Log the avatar change
//            if(!uaue.getUser().isBot())
//                sia.getBasicLogger().logAvatarChange(uaue);
//        }
        else if (event instanceof GuildVoiceJoinEvent)
        {
            GuildVoiceJoinEvent gevent = (GuildVoiceJoinEvent)event;
            
            // Log the voice join
            if(!gevent.getMember().getUser().isBot()) // ignore bots
                sia.getBasicLogger().logVoiceJoin(gevent);
        }
        else if (event instanceof GuildVoiceMoveEvent)
        {
            GuildVoiceMoveEvent gevent = (GuildVoiceMoveEvent)event;
            
            // Log the voice move
            if(!gevent.getMember().getUser().isBot()) // ignore bots
                sia.getBasicLogger().logVoiceMove(gevent);
        }
        else if (event instanceof GuildVoiceLeaveEvent)
        {
            GuildVoiceLeaveEvent gevent = (GuildVoiceLeaveEvent)event;
            
            // Log the voice leave
            if(!gevent.getMember().getUser().isBot()) // ignore bots
                sia.getBasicLogger().logVoiceLeave(gevent);
        }
        else if (event instanceof GuildJoinEvent)
        {
            TextChannel defaultChannel = ((GuildJoinEvent) event).getGuild().getDefaultChannel();
            String message =
                "Type `" + sia.getApplicationProperties().getDiscord().getPrefix() +
                "help" +
                "` for help and information.\n\n" + FormatUtil.helpLinksJoin((GuildJoinEvent) event);
            defaultChannel.sendMessage(new MessageBuilder()
                .setContent(Constants.SIA_EMOJII + "Hi, I am Sia! I was developed by **triippz**#0689" + Constants.SIA_EMOJII)
                .setEmbed(new EmbedBuilder()
                    .setDescription(message)
                    .build()
                ).build()
            ).queue();

            // add the members
            for (Member member : ((GuildJoinEvent) event).getGuild().getMembers() )
            {
                if (! member.getUser().isBot())
                    sia.getServiceManagers().getDiscordUserService().addNewUser(member.getUser().getIdLong());
            }

            //add the roles
            sia.getServiceManagers().guildRolesService().addAllRoles(((GuildJoinEvent) event).getGuild().getRoles());

            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(new MessageBuilder()
                    .setContent("Bot has joined Server: "
                        + ((GuildJoinEvent) event).getGuild().getName() +"/"
                        + ((GuildJoinEvent) event).getGuild().getIdLong())
                    .setEmbed(new EmbedBuilder().setThumbnail(((GuildJoinEvent) event).getGuild().getIconUrl()).build())
                    .build());
        }
        else if (event instanceof MessageReactionAddEvent)
        {
            if (((MessageReactionAddEvent) event).getUser().isBot()) return;

//            Poll poll;
//            try {
//                poll = sia.getServiceManagers().getPollService()
//                    .findByGuildIdAndMessageId(((MessageReactionAddEvent) event).getGuild().getIdLong(), ((MessageReactionAddEvent) event).getMessageIdLong());
//            } catch (NoPollsFoundException e) {
//                // Ignore it, probably wasnt a vote
//                return;
//            }
//
//            PollItems itemFound = null;
//            for (PollItems item : poll.getPollitems() )
//            {
//                String reaction = ((MessageReactionAddEvent) event).getReaction().getReactionEmote().getName();
//                if ( item.getReaction().equals((reaction)) )
//                {
//                    itemFound = item;
//                    break;
//                }
//            }
//
//            if ( itemFound == null )
//            {
//                LOG.error("Adding vote to Poll={}", poll.getTitle());
//                ((MessageReactionAddEvent) event).getUser().openPrivateChannel().complete()
//                    .sendMessage("Sorry, I was unable to add your vote, please try again.").queue();
//                return;
//            }
//
//            sia.getServiceManagers().getPollItemsService().addVote(itemFound.getId());
//            sia.getServiceManagers().getPollService().updatePollMessage(
//                poll,
//                event.getJDA().getTextChannelById(poll.getTextChannelId()).getMessageById(poll.getMessageId()).completeAfter(1, TimeUnit.SECONDS),
//                sia);
        }
        else if (event instanceof MessageReactionRemoveEvent)
        {
            if (((MessageReactionRemoveEvent) event).getUser().isBot()) return;
            Poll poll;
            try {
                poll = sia.getServiceManagers().getPollService()
                    .findByGuildIdAndMessageId(((MessageReactionRemoveEvent) event).getGuild().getIdLong(),
                        ((MessageReactionRemoveEvent) event).getMessageIdLong());
            } catch (NoPollsFoundException e) {
                // Ignore it, probably wasnt a vote
                return;
            }

            PollItems itemFound = null;
            for (PollItems item : poll.getPollitems() )
            {
                String reaction = ((MessageReactionRemoveEvent) event).getReaction().getReactionEmote().getName();
                if ( item.getReaction().equals((reaction)) )
                {
                    itemFound = item;
                    break;
                }
            }

            if ( itemFound == null )
            {
                LOG.error("Adding vote to Poll={}", poll.getTitle());
                ((MessageReactionRemoveEvent) event).getUser().openPrivateChannel().complete()
                    .sendMessage("Sorry, I was unable to add your vote, please try again.").queue();
                return;
            }

            sia.getServiceManagers().getPollItemsService().removeVote(itemFound.getId());
            sia.getServiceManagers().getPollService().updatePollMessage(
                poll,
                event.getJDA().getTextChannelById(poll.getTextChannelId()).getMessageById(poll.getMessageId()).completeAfter(1, TimeUnit.SECONDS),
                sia);
        }
        else if (event instanceof ReadyEvent)
        {
            // Log the shard that has finished loading
            ShardInfo si = event.getJDA().getShardInfo();
            String shardinfo = si==null ? "1/1" : (si.getShardId()+1)+"/"+si.getShardTotal();
            LOG.info("Shard "+shardinfo+" is ready.");
            sia.getLogWebhook().send("\uD83D\uDD3A Shard `"+shardinfo+"` has connected. Guilds: `" // 🔺
                    +event.getJDA().getGuildCache().size()+"` Users: `"+event.getJDA().getUserCache().size()+"`");

            // Search for new users since last join
            sia.checkForUserSinceShutdown();

            // Launch up the threads schedulers
            sia.getThreadpool().scheduleWithFixedDelay(() ->
                sia.getServiceManagers().getTempBansService().checkUnbans(event.getJDA()), 0, 2, TimeUnit.MINUTES);
            sia.getThreadpool().scheduleWithFixedDelay(() ->
                sia.getServiceManagers().getTempMutesService().checkUnmutes(event.getJDA(), sia.getServiceManagers().getGuildSettingsService()), 0, 45, TimeUnit.SECONDS);
            sia.getThreadpool().scheduleWithFixedDelay(() ->
                sia.getServiceManagers().getPollService().checkExpiredPolls(event.getJDA()), 0, 30, TimeUnit.SECONDS);
            sia.getThreadpool().scheduleWithFixedDelay(() ->
                sia.getServiceManagers().getPollService().cleanExpiredPolls(sia.getLogWebhook()), 0, 30, TimeUnit.DAYS);
        }
    }
}
