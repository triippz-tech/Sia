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
package com.trievosoftware.discord.commands.settings;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.Punishment;
import com.trievosoftware.application.exceptions.NoActionsExceptions;
import com.trievosoftware.discord.Action;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.CommandExceptionListener;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import com.trievosoftware.discord.utils.OtherUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
public class PunishmentCmd extends AbstractModeratorCommand
{
    private final static String SETTING_STRIKES = "\n\nUsage: `"+ Constants.PREFIX+"punishment <number> <action> [time]`\n"
            + "`<number>` - the number of strikes at which to perform the action\n"
            + "`<action>` - the action, such as `None`, `Kick`, `Mute`, `Softban`, or `Ban`\n"
            + "`[time]` - optional, the amount of time to keep the user muted or banned\n"
            + "Do not include <> nor [] in your commands!\n\n"
            + "The `"+ Constants.PREFIX+"settings` command can be used to view current punishments.";

    public PunishmentCmd(Sia sia)
    {
        super(sia);
        this.name = "punishment";
        this.help = "sets strikes for a punishment";
        this.aliases = new String[]{"setstrikes","setstrike","punishments"};
        this.arguments = "<number> <action> [time]";
        this.category = new Category("Settings");
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        String[] parts = event.getArgs().split("\\s+", 3);
        if(parts.length<2)
            throw new CommandExceptionListener.CommandErrorException("Please include a number of strikes and an action to perform!"+SETTING_STRIKES);
        int numstrikes;
        try
        {
            numstrikes = Integer.parseInt(parts[0]);
        }
        catch(NumberFormatException ex)
        {
            event.replyError("`"+parts[0]+"` is not a valid integer!"+SETTING_STRIKES);
            return;
        }
        if(numstrikes<1 || numstrikes> Punishment.MAX_STRIKES)
        {
            event.replyError("`<numstrikes>` must be between 1 and "+ Punishment.MAX_STRIKES+"!"+SETTING_STRIKES);
            return;
        }

        try {
            if(!parts[1].equalsIgnoreCase("none") &&
                sia.getServiceManagers().getActionsService().getAllPunishments(event.getGuild()).size()>= Punishment.MAX_SET)
                throw new CommandExceptionListener.CommandErrorException("This server already has "+ Punishment.MAX_SET+" punishments set up; please remove some before adding more.");
        } catch (NoActionsExceptions e) {
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
        }

        String successMessage;
        switch(parts[1].toLowerCase())
        {
            case "none":
            {
                try {
                    sia.getServiceManagers().getActionsService().removeAction(event.getGuild(), numstrikes);
                } catch (NoActionsExceptions e) {
                    if ( sia.isDebugMode() )
                        sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                            event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
                }
                successMessage = "No action will be taken on `"+numstrikes+"` strikes.";
                break;
            }
            case "tempmute":
            case "temp-mute":
            case "mute":
            {
                int minutes = parts.length>2 ? OtherUtil.parseTime(parts[2])/60 : 0;
                if(minutes<0)
                {
                    event.replyError("Temp-Mute time cannot be negative!");
                    return;
                }
                sia.getServiceManagers().getActionsService().setAction(event.getGuild(), numstrikes, Action.MUTE, minutes);
                successMessage = "Users will now be `muted` "+(minutes>0 ? "for "+ FormatUtil.secondsToTime(minutes*60)+" " : "")+"upon reaching `"+numstrikes+"` strikes.";
                if(sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild()).getMutedRole(event.getGuild())==null)
                    successMessage += event.getClient().getWarning()+" No muted role currently exists!";
                break;
            }
            case "kick":
            {
                sia.getServiceManagers().getActionsService().setAction(event.getGuild(), numstrikes, Action.KICK);
                successMessage = "Users will now be `kicked` upon reaching `"+numstrikes+"` strikes.";
                break;
            }
            case "softban":
            {
                sia.getServiceManagers().getActionsService().setAction(event.getGuild(), numstrikes, Action.SOFTBAN);
                successMessage = "Users will now be `softbanned` upon reaching `"+numstrikes+"` strikes.";
                break;
            }
            case "tempban":
            case "temp-ban":
            case "ban":
            {
                int minutes = parts.length>2 ? OtherUtil.parseTime(parts[2])/60 : 0;
                if(minutes<0)
                {
                    event.replyError("Temp-Ban time cannot be negative!");
                    return;
                }
                sia.getServiceManagers().getActionsService().setAction(event.getGuild(), numstrikes, Action.BAN, minutes);
                successMessage = "Users will now be `banned` "+(minutes>0 ? "for "+ FormatUtil.secondsToTime(minutes*60)+" " : "")+"upon reaching `"+numstrikes+"` strikes.";
                break;
            }
            default:
            {
                event.replyError("`"+parts[1]+"` is not a valid action!"+SETTING_STRIKES);
                return;
            }
        }
        event.reply(new MessageBuilder()
                .append(event.getClient().getSuccess())
                .append(" ").append(FormatUtil.filterEveryone(successMessage))
                .setEmbed(new EmbedBuilder().setColor(event.getSelfMember().getColor())
                        .addField(sia.getServiceManagers().getActionsService().getAllPunishmentsDisplay(event.getGuild()))
                        .build())
                .build());
    }
}
