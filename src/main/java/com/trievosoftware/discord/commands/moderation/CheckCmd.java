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
package com.trievosoftware.discord.commands.moderation;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.trievosoftware.discord.Action;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild.Ban;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
public class CheckCmd extends AbstractModeratorCommand
{
    public CheckCmd(Sia sia)
    {
        super(sia, Permission.BAN_MEMBERS);
        this.name = "check";
        this.arguments = "<user>";
        this.help = "checks a user";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }
    
    @Override
    public void doCommand(CommandEvent event)
    {
        if(event.getArgs().isEmpty() || event.getArgs().equalsIgnoreCase("help"))
        {
            event.replySuccess("This command is used to see a user's strikes and mute/ban status for the current server. Please include a user or user ID to check.");
            return;
        }
        event.getChannel().sendTyping().queue();
        List<Member> members = FinderUtil.findMembers(event.getArgs(), event.getGuild());
        if(!members.isEmpty())
        {
            check(event, members.get(0).getUser());
            return;
        }
        List<User> users = FinderUtil.findUsers(event.getArgs(), event.getJDA());
        if(!users.isEmpty())
        {
            check(event, users.get(0));
            return;
        }
        try
        {
            Long uid = Long.parseLong(event.getArgs());
            User u = sia.getShardManager().getUserById(uid);
            if(u!=null)
                check(event, u);
            else
                event.getJDA().retrieveUserById(uid).queue(
                        user -> check(event, user), 
                        v -> event.replyError("`"+uid+"` is not a valid user ID"));
        }
        catch(Exception ex)
        {
            event.replyError("Could not find a user `"+event.getArgs()+"`");
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), ex.getMessage()));
        }
    }
    
    private void check(CommandEvent event, User user)
    {
        if(event.getGuild().isMember(user))
            check(event, user, null);
        else
            event.getGuild().getBan(user).queue(ban -> check(event, user, ban), t -> check(event, user, null));
    }
    
    private void check(CommandEvent event, User user, Ban ban)
    {
        int strikes = sia.getServiceManagers().getStrikesService().getStrikes(event.getGuild(), user.getIdLong());
        int minutesMuted = sia.getServiceManagers().getTempMutesService().timeUntilUnmute(event.getGuild(), user.getIdLong());
        Role mRole = sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild())
            .getGuildSettings().getMutedRole(event.getGuild());
        int minutesBanned = sia.getServiceManagers().getTempBansService().timeUntilUnban(event.getGuild(), user.getIdLong());
        String str = "Moderation Information for "+ FormatUtil.formatFullUser(user)+":\n"
                + Action.STRIKE.getEmoji() + " Strikes: **"+strikes+"**\n"
                + Action.MUTE.getEmoji() + " Muted: **" + (event.getGuild().isMember(user)
                        ? (event.getGuild().getMember(user).getRoles().contains(mRole) ? "Yes" : "No") 
                        : "Not In Server") + "**\n"
                + Action.TEMPMUTE.getEmoji() + " Mute Time Remaining: " + (minutesMuted <= 0 ? "N/A" : FormatUtil.secondsToTime(minutesMuted * 60)) + "\n"
                + Action.BAN.getEmoji() + " Banned: **" + (ban==null ? "No**" : "Yes** (`" + ban.getReason() + "`)") + "\n"
                + Action.TEMPBAN.getEmoji() + " Ban Time Remaining: " + (minutesBanned <= 0 ? "N/A" : FormatUtil.secondsToTime(minutesBanned * 60));
        event.replySuccess(str);
    }
}
