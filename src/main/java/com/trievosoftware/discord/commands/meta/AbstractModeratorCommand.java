/*
 *    Copyright 2019 Mark Tripoli
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.trievosoftware.discord.commands.meta;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.exceptions.IncorrectWelcomeMessageParamsException;
import com.trievosoftware.application.exceptions.NoActiveWelcomeMessage;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

@SuppressWarnings("Duplicates")
public abstract class AbstractModeratorCommand extends Command {

    protected Sia sia;
    public AbstractModeratorCommand(Sia sia, Permission... altPerms)
    {
        this.sia = sia;
        this.category = new Category("Moderation", event ->
        {
            if(event.getGuild()==null)
            {
                event.replyError("This command is not available in Direct Messages!");
                return false;
            }
            Role modrole = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild()).getModeratorRole(event.getGuild());
            if(modrole!=null && event.getMember().getRoles().contains(modrole))
                return true;

            boolean missingPerms = false;
            for(Permission altPerm: altPerms)
            {
                if(altPerm.isText())
                {
                    if(!event.getMember().hasPermission(event.getTextChannel(), altPerm))
                        missingPerms = true;
                }
                else
                {
                    if(!event.getMember().hasPermission(altPerm))
                        missingPerms = true;
                }
            }
            if(!missingPerms)
                return true;
            if(event.getMember().getRoles().isEmpty())
                event.getMessage().addReaction(Constants.ERROR_REACTION).queue();
            else
                event.replyError("You must have the following permissions to use that: "+listPerms(altPerms));
            return false;
        });
    }

    @Override
    protected void execute (CommandEvent event)
    {
        if ( sia.getServiceManagers().getDiscordUserService().isUserBlacklisted(event.getAuthor().getIdLong()))
        {
            event.replyError("You are not authorized to use this bot. If you feel like this is a mistake please contact" +
                "the creators via discord: " + Constants.SERVER_INVITE);
            return;
        }
        sia.getServiceManagers().getDiscordUserService().addCommand(event.getAuthor());
        try {
            doCommand(event);
        } catch (IncorrectWelcomeMessageParamsException | NoActiveWelcomeMessage e) {
            event.replyError(e.getMessage());
        }
    }

    public abstract void doCommand(CommandEvent event) throws IncorrectWelcomeMessageParamsException, NoActiveWelcomeMessage;

    private static String listPerms(Permission... perms)
    {
        if(perms.length==0)
            return "";
        StringBuilder sb = new StringBuilder(perms[0].getName());
        for(int i=1; i<perms.length; i++)
            sb.append(", ").append(perms[i].getName());
        return sb.toString();
    }
}
