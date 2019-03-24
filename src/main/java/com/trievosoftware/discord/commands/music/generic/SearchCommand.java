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

package com.trievosoftware.discord.commands.music.generic;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractMusicCommand;
import com.trievosoftware.discord.music.audio.AudioHandler;
import com.trievosoftware.discord.music.audio.QueuedTrack;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.TimeUnit;

public class SearchCommand extends AbstractMusicCommand {

    protected String searchPrefix = "ytsearch:";
    private final OrderedMenu.Builder builder;
    private final String searchingEmoji;

    public SearchCommand(Sia sia, String searchingEmoji)
    {
        super(sia);
        this.searchingEmoji = searchingEmoji;
        this.name = "search";
        this.aliases = new String[]{"ytsearch"};
        this.arguments = "<query>";
        this.help = "searches Youtube for a provided query";
        this.beListening = true;
        this.bePlaying = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        builder = new OrderedMenu.Builder()
            .allowTextInput(true)
            .useNumbers()
            .useCancelButton(true)
            .setEventWaiter(sia.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES);
    }
    @Override
    public void doCommand(CommandEvent event)
    {
        if(event.getArgs().isEmpty())
        {
            event.replyError("Please include a query.");
            return;
        }
        event.reply(searchingEmoji+" Searching... `["+event.getArgs()+"]`",
            m -> sia.getPlayerManager().loadItemOrdered(event.getGuild(), searchPrefix + event.getArgs(), new ResultHandler(m,event)));
    }

    private class ResultHandler implements AudioLoadResultHandler
    {
        private final Message m;
        private final CommandEvent event;
        private GuildMusicSettings settings;

        private ResultHandler(Message m, CommandEvent event)
        {
            this.m = m;
            this.event = event;
            this.settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(event.getGuild());
        }

        @Override
        public void trackLoaded(AudioTrack track)
        {
            if(settings.isTooLong(track))
            {
                m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" This track (**"+track.getInfo().title+"**) is longer than the allowed maximum: `"
                    + FormatUtil.formatTime(track.getDuration())+"` > `"+ settings.getMaxTime()+"`")).queue();
                return;
            }
            AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
            int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor()))+1;
            m.editMessage(FormatUtil.filter(event.getClient().getSuccess()+" Added **"+track.getInfo().title
                +"** (`"+ FormatUtil.formatTime(track.getDuration())+"`) "+(pos==0 ? "to begin playing"
                : " to the queue at position "+pos))).queue();
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist)
        {
            builder.setColor(event.getSelfMember().getColor())
                .setText(FormatUtil.filter(event.getClient().getSuccess()+" Search results for `"+event.getArgs()+"`:"))
                .setChoices(new String[0])
                .setSelection((msg,i) ->
                {
                    AudioTrack track = playlist.getTracks().get(i-1);
                    if(settings.isTooLong(track))
                    {
                        event.replyWarning("This track (**"+track.getInfo().title+"**) is longer than the allowed maximum: `"
                            + FormatUtil.formatTime(track.getDuration())+"` > `"+ settings.getMaxTime()+"`");
                        return;
                    }
                    AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
                    int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor()))+1;
                    event.replySuccess("Added **"+track.getInfo().title
                        +"** (`"+ FormatUtil.formatTime(track.getDuration())+"`) "+(pos==0 ? "to begin playing"
                        : " to the queue at position "+pos));
                })
                .setCancel((msg) -> {})
                .setUsers(event.getAuthor())
            ;
            for(int i=0; i<4 && i<playlist.getTracks().size(); i++)
            {
                AudioTrack track = playlist.getTracks().get(i);
                builder.addChoices("`["+ FormatUtil.formatTime(track.getDuration())+"]` [**"+track.getInfo().title+"**]("+track.getInfo().uri+")");
            }
            builder.build().display(m);
        }

        @Override
        public void noMatches()
        {
            m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" No results found for `"+event.getArgs()+"`.")).queue();
        }

        @Override
        public void loadFailed(FriendlyException throwable)
        {
            if(throwable.severity== FriendlyException.Severity.COMMON)
                m.editMessage(event.getClient().getError()+" Error loading: "+throwable.getMessage()).queue();
            else
                m.editMessage(event.getClient().getError()+" Error loading track.").queue();
        }
    }
}
