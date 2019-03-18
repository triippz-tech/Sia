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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.application.exceptions.NoSongException;
import com.trievosoftware.application.exceptions.UserPlaylistExistsException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractMusicCommand;
import com.trievosoftware.discord.music.audio.AudioHandler;
import com.trievosoftware.discord.music.audio.QueuedTrack;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class UserPlaylistCommand extends AbstractMusicCommand
{
    private final Logger log = LoggerFactory.getLogger(UserPlaylistCommand.class);
    private String loadingEmoji;
    private final static String LOAD = "\uD83D\uDCE5"; // 📥
    private final static String CANCEL = "\uD83D\uDEAB"; // 🚫

    public UserPlaylistCommand(Sia sia, String loadingEmoji) {
        super(sia);
        this.loadingEmoji = loadingEmoji;
        this.guildOnly = true;
        this.name = "playlist";
        this.aliases = new String[]{"userplaylist"};
        this.arguments = "<append|delete|make|setdefault>";
        this.help = "user playlist management";
        this.children = new AbstractMusicCommand[]{
//            new AppendMultipleCommand(sia),
            new AppendOneCommand(sia),
            new DeletePlaylistCmd(sia),
//            new DeleteMultipleSongsCommand(sia),
            new DeleteOneSongCommand(sia),
            new MakelistCmd(sia),
            new DisplayPlaylistCmd(sia),
            new CopyPlaylist(sia)
        };
    }

    @Override
    public void doCommand(CommandEvent event) {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Playlist Management Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }


    /**
     *
     */
    private class AppendOneCommand extends AbstractMusicCommand {
        AppendOneCommand(Sia sia) {
            super(sia);
            this.guildOnly = false; // can use it in whatever server they want (with access)
            this.name = "append";
            this.aliases = new String[]{"add"};
            this.help = "appends a song to a playlist";
            this.arguments = "playlist | <URL> | [Track Name]";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
//            String[] parts = event.getArgs().split("\\s+", -1);
            String[] parts = event.getArgs().split("\\|");
            if(parts.length <= 2)
            {
                event.reply(event.getClient().getError()+" Please include a playlist name, URL, and a Track Name to add!");
                return;
            }
            String pname = parts[0];
            String songName = parts[2];
            try
            {
                pname = pname.trim().replaceAll(" ", "_").toUpperCase();
                songName = songName.trim().replaceAll(" ", "_").toUpperCase();
                Playlist playlist = sia.getServiceManagers().getPlaylistService().getUserPlaylistByName(event.getAuthor().getIdLong(), pname);

                Songs song = sia.getServiceManagers().getSongService().createSong(songName, parts[1], playlist);
                sia.getServiceManagers().getPlaylistService().addSongToUserPlaylist(event.getAuthor(), playlist.getId(), song);
                event.replySuccess(
                    " Successfully added "
                    + parts[2]
                    + " to playlist `"
                    + playlist.getPlaylistName() + "`!");
            }
            catch (NoPlaylistFoundException e)
            {
                event.replyError(e.getMessage());
            }
        }
    }

    /**
     *
     */
    private class DeletePlaylistCmd extends AbstractMusicCommand {
        DeletePlaylistCmd(Sia sia) {
            super(sia);
            this.name = "delete";
            this.aliases = new String[]{"remove"};
            this.help = "deletes an existing playlist";
            this.arguments = "<name>";
            this.guildOnly = false;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            String pname = event.getArgs().trim().replaceAll(" ", "_").toUpperCase();
            try {
                Playlist playlist = sia.getServiceManagers().getPlaylistService()
                    .getUserPlaylistByName(event.getAuthor().getIdLong(), pname);
                sia.getServiceManagers().getPlaylistService().delete(playlist.getId());
                event.replySuccess("Successfully deleted playlist!");
            } catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }

    /**
     *
     */
    private class DeleteMultipleSongsCommand extends AbstractMusicCommand {
        private DeleteMultipleSongsCommand(Sia sia) {
            super(sia);
        }

        @Override
        public void doCommand(CommandEvent event) {

        }
    }

    /**
     *
     */
    private class DeleteOneSongCommand extends AbstractMusicCommand {
        DeleteOneSongCommand(Sia sia) {
            super(sia);
            this.name = "deletesong";
            this.aliases = new String[]{"dsong"};
            this.help = "deletes one song from a playlist";
            this.arguments = "<playlist name> | <track name>";
            this.guildOnly = false;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            String[] parts = event.getArgs().split("\\|");
            if(parts.length < 2)
            {
                event.reply(event.getClient().getError()+" Please include a playlist name and a Track Name to add!");
                return;
            }
            try {
                String playlistName = parts[0].trim().replaceAll(" ", "_").toUpperCase();
                String trackName = parts[1].trim().replaceAll(" ", "_").toUpperCase();

                if (playlistName.startsWith("<") && playlistName.endsWith(">"))
                    playlistName = playlistName.substring(1, playlistName.length() - 1);
                if (trackName.startsWith("<") && trackName.endsWith(">"))
                    trackName = trackName.substring(1, trackName.length() - 1);


                Playlist playlist = sia.getServiceManagers().getPlaylistService()
                    .getUserPlaylistByName(event.getAuthor().getIdLong(), playlistName);
                Integer numberDeleted;
                try {
                    numberDeleted = sia.getServiceManagers().getSongService().deleteSongByNameAndPlaylist(trackName, playlist);
                } catch (NoSongException e) {
                    event.replyError(e.getMessage());
                    return;
                }
                event.reply(event.getClient().getSuccess() + " Successfully deleted " +
                        (numberDeleted < 2
                            ? "Song `" + trackName
                            : numberDeleted + " entries of Song `" + trackName )
                     + "` From Playlist: `"+ playlist.getPlaylistName() +"`!");

                event.getMessage().delete().queue();
            }
            catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
            }
        }
    }

    /**
     *
     */
    private class MakelistCmd extends AbstractMusicCommand {
        MakelistCmd(Sia sia) {
            super(sia);
            this.name = "make";
            this.aliases = new String[]{"create", "new"};
            this.help = "Creates a new Playlist";
            this.arguments = "<name>";
            this.guildOnly = false;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            String pname = event.getArgs().replaceAll("\\s+", "_");
            try {
                pname = pname.toUpperCase();
                sia.getServiceManagers().getPlaylistService().getUserPlaylistByName(event.getAuthor().getIdLong(), pname);
                event.replyError("You already have a playlist with that name!");
            }
            catch (NoPlaylistFoundException e)
            {

                sia.getServiceManagers().getPlaylistService()
                    .createUserPlaylist(pname.trim().replaceAll(" ", "_"), event.getAuthor());
                event.replySuccess("Successfully created playlist `" + pname + "`!");
            }
        }
    }

    /**
     *
     */
    private class DisplayPlaylistCmd extends AbstractMusicCommand {
        DisplayPlaylistCmd(Sia sia) {
            super(sia);
            this.name = "dplaylist";
            this.aliases = new String[]{"displayplaylist"};
            this.help = "Displays a users playlist(s)";
            this.arguments = "ALL or <name> | <name> | . . .";
            this.guildOnly = false;
        }

        @Override
        public void doCommand(CommandEvent event) {
            String[] parts = event.getArgs().split("\\|", 2);
            List<MessageEmbed.Field> embedFields;
            if (parts[0].trim().equalsIgnoreCase("ALL")) {
                embedFields = sia.getServiceManagers().getPlaylistService().getUserPlaylistDisplays(event.getAuthor());
                if ( embedFields.isEmpty() )
                {
                    event.replyError("No playlists found! Try adding one!");
                    return;
                }
            }
            else {
                String[] newParts = new String[parts.length];
                for ( int i = 0; i < parts.length; i++ )
                {
                    newParts[i] = parts[i].trim().replaceAll(" ", "_").toUpperCase();
                    if (newParts[i].startsWith("<") && newParts[i].endsWith(">"))
                        newParts[i] = newParts[i].substring(1, newParts[i].length() - 1);
                }
                embedFields = sia.getServiceManagers().getPlaylistService().getUserPlaylistDisplays(newParts, event.getAuthor());
                if ( embedFields.isEmpty() )
                {
                    event.replyError("No playlists found! Try adding one!");
                    return;
                }
            }
            //display all
//                    PremiumInfo pi = sia.getServiceManagers().getPremiumService().getPremiumInfo(event.getGuild()); // replace w/ user premium
            MessageBuilder builder = new MessageBuilder().append("**")
                .append(event.getAuthor().getName()).append("'s Playlists **");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            for (MessageEmbed.Field field : embedFields) {
                embedBuilder.addField(field);
            }
            builder.setEmbed(embedBuilder.build());
            event.getChannel().sendMessage(builder.build()).queue();
        }
    }

    public class CopyPlaylist extends AbstractMusicCommand
    {

        CopyPlaylist(Sia sia) {
            super(sia);
            this.name = "copy";
            this.help = "Creates a User playlist based of a playlist from Google (and others in the future). " +
                "If a playlist is found, nothing will be added. If a playlist name is not found, a new one will be created";
            this.aliases = new String[]{"cplaylist"};
            this.arguments = "<Youtube> | <URL> | <playlist name>";
            this.guildOnly = false;
        }

        @Override
        public void doCommand(CommandEvent event) throws NoPlaylistFoundException {
            String[] parts = event.getArgs().split("\\|");
            String provider = parts[0].trim();
            String url = parts[1].trim();
            String playlistName = parts[2].trim().replaceAll(" ", "_").toUpperCase();

            if (provider.startsWith("<") && provider.endsWith(">"))
                provider = provider.substring(1, provider.length() - 1);
            if (url.startsWith("<") && url.endsWith(">"))
                url = url.substring(1, url.length() - 1);
            if (playlistName.startsWith("<") && playlistName.endsWith(">"))
                playlistName = playlistName.substring(1, playlistName.length() - 1);

            String finalUrl = url;
            String finalPlaylistName = playlistName;

            if ( sia.getServiceManagers().getPlaylistService().userPlaylistExists(event.getAuthor().getIdLong(), playlistName)) {
                event.replyError("Playlist `" + playlistName + "` already exists!");
                return;
            }

            event.reply(loadingEmoji + " Loading... `[" + url + "]`", m ->
                sia.getPlayerManager().loadItemOrdered(
                    event.getGuild(), finalUrl,
                    new ResultHandler(m, finalPlaylistName, event, false)
                )
            );
        }

        private class ResultHandler implements AudioLoadResultHandler {
            private final Message m;
            private final CommandEvent event;
            private final boolean ytsearch;
            private GuildMusicSettings settings;
            private String playlistName;

            private ResultHandler(Message m, String playlistName, CommandEvent event, boolean ytsearch) {
                this.m = m;
                this.event = event;
                this.ytsearch = ytsearch;
                this.playlistName = playlistName;
                settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(event.getGuild());
            }

            private void loadSingle(AudioTrack track, AudioPlaylist playlist) {
                if (settings.isTooLong(track)) {
                    m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " This track (**" + track.getInfo().title + "**) is longer than the allowed maximum: `"
                        + FormatUtil.formatTime(track.getDuration()) + "` > `" + FormatUtil.formatTime(settings.getMaxSeconds() * 1000) + "`")).queue();
                    return;
                }
                AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor())) + 1;
                String addMsg = FormatUtil.filter(event.getClient().getSuccess() + " Added **" + track.getInfo().title
                    + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`) " + (pos == 0 ? "to begin playing" : " to the queue at position " + pos));
                if (playlist == null || !event.getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION))
                    m.editMessage(addMsg).queue();
                else {
                    new ButtonMenu.Builder()
                        .setText(addMsg + "\n" + event.getClient().getWarning() + " This track has a playlist of **" + playlist.getTracks().size() + "** tracks attached. Select " + LOAD + " to copy playlist.")
                        .setChoices(LOAD, CANCEL)
                        .setEventWaiter(sia.getEventWaiter())
                        .setTimeout(30, TimeUnit.SECONDS)
                        .setAction(re ->
                        {
                            if (re.getName().equals(LOAD)) {
                                try {
                                    m.editMessage(addMsg + "\n" + event.getClient().getSuccess()
                                        + " Copied **" + loadPlaylist(playlistName, event, playlist, track)
                                        + "** tracks to `" + playlistName + "`!").queue();
                                } catch (NoPlaylistFoundException | UserPlaylistExistsException e) {
                                    event.replyError(e.getMessage());
                                    return;
                                }
                            }
                            else
                                m.editMessage(addMsg).queue();
                        }).setFinalAction(m ->
                    {
                        try {
                            m.clearReactions().queue();
                        } catch (PermissionException ignore) {
                        }
                    }).build().display(m);
                }
            }

            private int loadPlaylist(String playlistName, CommandEvent event, AudioPlaylist playlist, AudioTrack exclude) throws NoPlaylistFoundException, UserPlaylistExistsException {
                int[] count = {0};

                sia.getServiceManagers().getPlaylistService().createUserPlaylist(playlistName, event.getAuthor());
                Playlist userPlaylist = sia.getServiceManagers().getPlaylistService().getUserPlaylistByName(event.getAuthor().getIdLong(), playlistName);

                playlist.getTracks().forEach((track) -> {
                    if (!settings.isTooLong(track) && !track.equals(exclude)) {
                        Songs song = new Songs();
                        song.setPlaylist(userPlaylist);
                        song.setSongName(track.getInfo().title.replaceAll(" ", "_").toUpperCase());
                        song.setSongQuery(track.getInfo().uri);

                        Songs createdSong = sia.getServiceManagers().getSongService().createSong(song);
                        sia.getServiceManagers().getPlaylistService().addSongToUserPlaylist(event.getAuthor(), userPlaylist, createdSong);

                        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                        handler.addTrack(new QueuedTrack(track, event.getAuthor()));
                        count[0]++;
                    }
                });
                return count[0];
            }

            @Override
            public void trackLoaded(AudioTrack track) {
                loadSingle(track, null);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getTracks().size() == 1 || playlist.isSearchResult()) {
                    AudioTrack single = playlist.getSelectedTrack() == null ? playlist.getTracks().get(0) : playlist.getSelectedTrack();
                    loadSingle(single, null);
                } else if (playlist.getSelectedTrack() != null) {
                    AudioTrack single = playlist.getSelectedTrack();
                    loadSingle(single, playlist);
                } else {
                    int count = 0;
                    try {
                        count = loadPlaylist(playlistName, event, playlist, null);
                    } catch (NoPlaylistFoundException | UserPlaylistExistsException e) {
                        event.replyError(e.getMessage());
                        return;
                    }
                    if (count == 0) {
                        m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " All entries in this playlist " + (playlist.getName() == null ? "" : "(**" + playlist.getName()
                            + "**) ") + "were longer than the allowed maximum (`" + settings.getMaxTime() + "`)")).queue();
                    } else {
                        m.editMessage(FormatUtil.filter(event.getClient().getSuccess() + " Found "
                            + (playlist.getName() == null ? "a playlist" : "playlist **" + playlist.getName() + "**") + " with `"
                            + playlist.getTracks().size() + "` entries; added to the queue!"
                            + (count < playlist.getTracks().size() ? "\n" + event.getClient().getWarning() + " Tracks longer than the allowed maximum (`"
                            + settings.getMaxTime() + "`) have been omitted." : ""))).queue();
                    }
                }
            }

            @Override
            public void noMatches() {
                if (ytsearch)
                    m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No results found for `" + event.getArgs() + "`.")).queue();
                else
                    sia.getPlayerManager().loadItemOrdered(event.getGuild(), "ytsearch:" + event.getArgs(), new ResultHandler(m, playlistName, event, true));
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                if (throwable.severity == FriendlyException.Severity.COMMON)
                    m.editMessage(event.getClient().getError() + " Error loading: " + throwable.getMessage()).queue();
                else
                    m.editMessage(event.getClient().getError() + " Error loading track.").queue();
            }
        }

    }
}
