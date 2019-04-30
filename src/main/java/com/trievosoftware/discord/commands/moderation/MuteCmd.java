/*
 * Copyright 2016 John Grosh (jagrosh).
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
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.CommandExceptionListener.CommandErrorException;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.ArgsUtil;
import com.trievosoftware.discord.utils.FormatUtil;
import com.trievosoftware.discord.utils.LogUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class MuteCmd extends AbstractModeratorCommand
{
    public MuteCmd(Sia sia)
    {
        super(sia, Permission.MANAGE_ROLES);
        this.name = "mute";
        this.arguments = "<@users> [time] [reason]";
        this.help = "applies muted role to users";
        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.guildOnly = true;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void doCommand(CommandEvent event)
    {
        Role muteRole = sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild())
            .getGuildSettings().getMutedRole(event.getGuild());
        if(muteRole == null)
        {
            event.replyError("No Muted role exists!");
            return;
        }
        if(!event.getMember().canInteract(muteRole))
        {
            event.replyError("You do not have permissions to assign the '"+muteRole.getName()+"' role!");
            return;
        }
        if(!event.getSelfMember().canInteract(muteRole))
        {
            event.reply(event.getClient().getError()+" I do not have permissions to assign the '"+muteRole.getName()+"' role!");
            return;
        }
        
        ArgsUtil.ResolvedArgs args = ArgsUtil.resolve(event.getArgs(), true, event.getGuild());
        if(args.isEmpty())
        {
            event.replyError("Please include at least one user to mute (@mention or ID)!");
            return;
        }
        int minutes;
        if(args.time < 0)
            throw new CommandErrorException("Timed mutes cannot be negative time!");
        else if(args.time == 0)
            minutes = 0;
        else if(args.time > 60)
            minutes = (int)Math.round(args.time/60.0);
        else
            minutes = 1;
        String reason = LogUtil.auditReasonFormat(event.getMember(), minutes, args.reason);
        Role modrole = sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild())
            .getGuildSettings().getModeratorRole(event.getGuild());
        StringBuilder builder = new StringBuilder();
        List<Member> toMute = new LinkedList<>();
        
        args.members.forEach(m -> 
        {
            if(!event.getMember().canInteract(m))
                builder.append("\n").append(event.getClient().getError()).append(" You do not have permission to mute ").append(FormatUtil.formatUser(m.getUser()));
            else if(!event.getSelfMember().canInteract(m))
                builder.append("\n").append(event.getClient().getError()).append(" I am unable to mute ").append(FormatUtil.formatUser(m.getUser()));
            else if(m.getRoles().contains(muteRole))
                builder.append("\n").append(event.getClient().getError()).append(" ").append(FormatUtil.formatUser(m.getUser())).append(" is already muted!");
            else if(modrole!=null && m.getRoles().contains(modrole))
                builder.append("\n").append(event.getClient().getError()).append(" I won't mute ").append(FormatUtil.formatUser(m.getUser())).append(" because they have the Moderator Role");
            else
                toMute.add(m);
        });
        
        args.unresolved.forEach(un -> builder.append("\n").append(event.getClient().getWarning()).append(" Could not resolve `").append(un).append("` to a member"));
        
        args.users.forEach(u -> builder.append("\n").append(event.getClient().getWarning()).append(" The user ").append(u.getAsMention()).append(" is not in this server."));
        
        args.ids.forEach(id -> builder.append("\n").append(event.getClient().getWarning()).append(" The user <@").append(id).append("> is not in this server."));
        
        if(toMute.isEmpty())
        {
            event.reply(builder.toString());
            return;
        }
        
        if(toMute.size() > 5)
            event.reactSuccess();
        
        Instant unmuteTime = Instant.now().plus(minutes, ChronoUnit.MINUTES);
        String time = minutes==0 ? "" : " for "+ FormatUtil.secondsToTimeCompact(minutes*60);
        for(int i=0; i<toMute.size(); i++)
        {
            Member m = toMute.get(i);
            boolean last = i+1 == toMute.size();
            event.getGuild().getController().addSingleRoleToMember(m, muteRole).reason(reason).queue(success -> 
            {
                builder.append("\n").append(event.getClient().getSuccess()).append(" Successfully muted ").append(FormatUtil.formatUser(m.getUser())).append(time);
                if(minutes>0)
                    sia.getServiceManagers().getTempMutesService().overrideMute(event.getGuild(), m.getUser().getIdLong(), unmuteTime);
                else
                    sia.getServiceManagers().getTempMutesService().overrideMute(event.getGuild(), m.getUser().getIdLong(), Instant.MAX);
                if(last)
                    event.reply(builder.toString());
            }, failure -> 
            {
                builder.append("\n").append(event.getClient().getError()).append(" Failed to mute ").append(m.getUser().getAsMention());
                if(last)
                    event.reply(builder.toString());
            });
        }
    }
}
