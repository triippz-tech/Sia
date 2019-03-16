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

package com.trievosoftware.discord.commands.music.moderator;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractDjCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.List;

@SuppressWarnings("Duplicates")
public class SetMusicVcCommand extends AbstractDjCommand {

    public SetMusicVcCommand(Sia sia) {
        super(sia);
        this.name = "setmvc";
        this.aliases = new String[]{"setmusicvoicechannel", "setmusicvc"};
        this.help = "sets a dedicated voice channel for playing music";
        this.arguments = "<channel|NONE>";
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        if(event.getArgs().isEmpty())
        {
            event.reply(event.getClient().getError()+" Please include a voice channel or NONE");
            return;
        }
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            try {
                sia.getServiceManagers().getGuildMusicSettingsService().setVoiceChannel(event.getGuild(),null);
            } catch (NoMusicSettingsException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
            event.reply(event.getClient().getSuccess()+" Music can now be played in any channel");
        }
        else
        {
            List<VoiceChannel> list = FinderUtil.findVoiceChannels(event.getArgs(), event.getGuild());
            if(list.isEmpty())
                event.reply(event.getClient().getWarning()+" No Voice Channels found matching \""+event.getArgs()+"\"");
            else if (list.size()>1)
                event.reply(event.getClient().getWarning()+ FormatUtil.listOfVChannels(list, event.getArgs()));
            else
            {
                try {
                    sia.getServiceManagers().getGuildMusicSettingsService().setVoiceChannel(event.getGuild(), list.get(0));
                } catch (NoMusicSettingsException e) {
                    event.replyError(e.getMessage());
                    if ( sia.isDebugMode() )
                        sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                            event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
                }
                event.reply(event.getClient().getSuccess()+" Music can now only be played in **"+list.get(0).getName()+"**");
            }
        }
    }
}
