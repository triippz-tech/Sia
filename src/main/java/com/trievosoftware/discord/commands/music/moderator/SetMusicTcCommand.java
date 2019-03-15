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
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class SetMusicTcCommand extends AbstractDjCommand {

    public SetMusicTcCommand(Sia sia) {
        super(sia);
        this.name = "setmtc";
        this.aliases = new String[]{"setmusictextchannel", "setmusictc"};
        this.help = "sets a dedicated text channel for music commands";
        this.arguments = "<channel|NONE>";
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        if(event.getArgs().isEmpty())
        {
            event.reply(event.getClient().getError()+" Please include a text channel or NONE");
            return;
        }
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            try {
                sia.getServiceManagers().getGuildMusicSettingsService().setTextChannel(event.getGuild(),null);
            } catch (NoMusicSettingsException e) {
                event.replyError(e.getMessage());
            }
            event.reply(event.getClient().getSuccess()+" Music commands can now be used in any channel");
        }
        else
        {
            List<TextChannel> list = FinderUtil.findTextChannels(event.getArgs(), event.getGuild());
            if(list.isEmpty())
                event.reply(event.getClient().getWarning()+" No Text Channels found matching \""+event.getArgs()+"\"");
            else if (list.size()>1)
                event.reply(event.getClient().getWarning()+ FormatUtil.listOfTChannels(list, event.getArgs()));
            else
            {
                try {
                    sia.getServiceManagers().getGuildMusicSettingsService().setTextChannel(event.getGuild(), list.get(0));
                } catch (NoMusicSettingsException e) {
                    event.replyError(e.getMessage());
                }
                event.reply(event.getClient().getSuccess()+" Music commands can now only be used in <#"+list.get(0).getId()+">");
            }
        }
    }
}
