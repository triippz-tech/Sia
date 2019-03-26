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
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractDjCommand;
import com.trievosoftware.discord.music.audio.AudioHandler;

public class SkipToCommand extends AbstractDjCommand {

    public SkipToCommand(Sia sia) {
        super(sia);
        this.name = "skipto";
        this.help = "skips to the specified song";
        this.arguments = "<position>";
        this.aliases = new String[]{"jumpto"};
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        int index = 0;
        try
        {
            index = Integer.parseInt(event.getArgs());
        }
        catch(NumberFormatException e)
        {
            event.reply(event.getClient().getError()+" `"+event.getArgs()+"` is not a valid integer!");
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            return;
        }
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(index<1 || index>handler.getQueue().size())
        {
            event.reply(event.getClient().getError()+" Position must be a valid integer between 1 and "+handler.getQueue().size()+"!");
            return;
        }
        handler.getQueue().skip(index-1);
        event.reply(event.getClient().getSuccess()+" Skipped to **"+handler.getQueue().get(0).getTrack().getInfo().title+"**");
        handler.getPlayer().stopTrack();
    }
}
