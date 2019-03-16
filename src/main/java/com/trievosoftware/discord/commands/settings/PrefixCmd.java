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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.SetPrefixException;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
@SuppressWarnings("Duplicates")
public class PrefixCmd extends Command
{
    private final Sia sia;
    
    public PrefixCmd(Sia sia)
    {
        this.sia = sia;
        this.name = "prefix";
        this.help = "sets the server prefix";
        this.arguments = "<prefix or NONE>";
        this.category = new Category("Settings");
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if(event.getArgs().isEmpty())
        {
            event.replyError("Please include a prefix. The server's current prefix can be seen via the `"+Constants.PREFIX+"settings` command");
            return;
        }
        
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            try {
                sia.getServiceManagers().getGuildSettingsService().setPrefix(event.getGuild(), null);
                event.replySuccess("The server prefix has been reset.");
            } catch (SetPrefixException e) {
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
                event.replyError(e.getMessage());
            }
            return;
        }
        
        if(event.getArgs().length()> GuildSettings.PREFIX_MAX_LENGTH)
        {
            event.replySuccess("Prefixes cannot be longer than `"+GuildSettings.PREFIX_MAX_LENGTH+"` characters.");
            return;
        }

        try {
            sia.getServiceManagers().getGuildSettingsService().setPrefix(event.getGuild(), event.getArgs());
            event.replySuccess("The server prefix has been set to `"+event.getArgs()+"`\n"
                + "Note that the default prefix (`"+Constants.PREFIX+"`) cannot be removed and will work in addition to the custom prefix.");
        } catch (SetPrefixException e) {
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            event.replyError(e.getMessage());
        }

    }
}
