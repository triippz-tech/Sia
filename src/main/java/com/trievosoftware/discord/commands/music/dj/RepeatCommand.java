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

package com.trievosoftware.discord.commands.music.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractDjCommand;

public class RepeatCommand extends AbstractDjCommand {

    public RepeatCommand(Sia sia) {
        super(sia);
        this.name = "repeat";
        this.help = "re-adds music to the queue when finished";
        this.arguments = "[on|off]";
        this.guildOnly = true;
    }

    // override musiccommand's execute because we don't actually care where this is used
    @Override
    protected void execute(CommandEvent event)
    {
        boolean value;
        GuildMusicSettings settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(event.getGuild());
        if(event.getArgs().isEmpty())
        {
            value = !settings.isRepeat();
        }
        else if(event.getArgs().equalsIgnoreCase("true") || event.getArgs().equalsIgnoreCase("on"))
        {
            value = true;
        }
        else if(event.getArgs().equalsIgnoreCase("false") || event.getArgs().equalsIgnoreCase("off"))
        {
            value = false;
        }
        else
        {
            event.replyError("Valid options are `on` or `off` (or leave empty to toggle)");
            return;
        }
        try {
            sia.getServiceManagers().getGuildMusicSettingsService().setRepeat(event.getGuild(), value);
        } catch (NoMusicSettingsException e) {
            event.replyError(e.getMessage());
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
        }
        event.replySuccess("Repeat mode is now `"+(value ? "ON" : "OFF")+"`");
    }

    @Override
    public void doCommand(CommandEvent event) { /* Intentionally Empty */ }
}
