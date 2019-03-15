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
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.music.audio.AudioHandler;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

public abstract class AbstractMusicCommand extends Command {

    protected Sia sia;
    protected boolean bePlaying;
    protected boolean beListening;

    public AbstractMusicCommand(Sia sia)
    {
        this.sia = sia;
        this.guildOnly = true;
        this.category = new Category("Music");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        GuildMusicSettings settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(event.getGuild());

        TextChannel tchannel = settings.getTextChannel(event.getGuild());
        if(tchannel!=null && !event.getTextChannel().equals(tchannel))
        {
            try
            {
                event.getMessage().delete().queue();
            } catch(PermissionException ignore){}
            event.replyInDm(event.getClient().getError()+" You can only use that command in "+tchannel.getAsMention()+"!");
            return;
        }
        sia.getPlayerManager().setUpHandler(event.getGuild()); // no point constantly checking for this later
        if(bePlaying && !((AudioHandler)event.getGuild().getAudioManager().getSendingHandler()).isMusicPlaying(event.getJDA()))
        {
            event.reply(event.getClient().getError()+" There must be music playing to use that!");
            return;
        }
        if(beListening)
        {
            VoiceChannel current = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(current==null)
                current = settings.getVoiceChannel(event.getGuild());
            GuildVoiceState userState = event.getMember().getVoiceState();
            if(!userState.inVoiceChannel() || userState.isDeafened() || (current!=null && !userState.getChannel().equals(current)))
            {
                event.replyError("You must be listening in "+(current==null ? "a voice channel" : "**"+current.getName()+"**")+" to use that!");
                return;
            }
            if(!event.getGuild().getSelfMember().getVoiceState().inVoiceChannel())
            {
                try
                {
                    event.getGuild().getAudioManager().openAudioConnection(userState.getChannel());
                }
                catch(PermissionException ex)
                {
                    event.reply(event.getClient().getError()+" I am unable to connect to **"+userState.getChannel().getName()+"**!");
                    return;
                }
            }
        }

        try {
            doCommand(event);
        } catch (NoPlaylistFoundException | NoMusicSettingsException e) {
            event.replyError(e.getMessage());
        }
    }

    public abstract void doCommand(CommandEvent event) throws NoPlaylistFoundException, NoMusicSettingsException;
}
