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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.PremiumInfo;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.application.exceptions.NoSongException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractMusicModeratorCommand;
import com.trievosoftware.discord.utils.Pair;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class GuildPlaylistCommand extends AbstractMusicModeratorCommand {

    private final Logger log = LoggerFactory.getLogger(GuildPlaylistCommand.class);

    public GuildPlaylistCommand(Sia sia) {
        super(sia);
        this.guildOnly = true;
        this.name = "gplaylist";
        this.aliases = new String[]{"guildplaylist"};
        this.arguments = "<append|delete|make|setdefault>";
        this.help = "playlist management";
        this.children = new AbstractMusicModeratorCommand[]{
//            new AppendMultipleCommand(sia),
            new AppendOneCommand(sia),
            new DeletePlaylistCmd(sia),
            new DeleteMultipleSongsCommand(sia),
            new DeleteOneSongCommand(sia),
            new MakelistCmd(sia),
            new DisplayPlaylistCmd(sia)
        };
    }

    @Override
    public void doCommand(CommandEvent event) {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Playlist Management Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class MakelistCmd extends AbstractMusicModeratorCommand {
        public MakelistCmd(Sia sia) {
            super(sia);
            this.name = "make";
            this.aliases = new String[]{"create"};
            this.help = "makes a new playlist";
            this.arguments = "<name>";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event) {
            String pname = event.getArgs().replaceAll("\\s+", "_");
            try {
                sia.getServiceManagers().getPlaylistService().getGuildPlaylist(event.getGuild());
                event.reply(event.getClient().getError() + "This guild alreeady has a playlist! Pleease delete existing playlist");
            } catch (NoPlaylistFoundException e) {
                try {
                    Playlist playlist = sia.getServiceManagers().getPlaylistService().createGuildPlaylist(pname, event.getGuild());
                    sia.getServiceManagers().getGuildMusicSettingsService().setDefaultPlaylist(event.getGuild(), playlist);
                    event.reply(event.getClient().getSuccess() + " Successfully created playlist `" + pname + "`!");
                } catch (NoMusicSettingsException e1) {
                    event.reply(event.getClient().getError() + " I was unable to create the playlist: " + e1.getMessage());
                    if ( sia.isDebugMode() )
                        sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                            event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
                }
            }
        }
    }

    public class DeletePlaylistCmd extends AbstractMusicModeratorCommand {
        public DeletePlaylistCmd(Sia sia) {
            super(sia);
            this.name = "delete";
            this.aliases = new String[]{"remove"};
            this.help = "deletes an existing playlist";
            this.arguments = "<name>";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event) {
            String pname = event.getArgs().replaceAll("\\s+", "_");
            try {
                GuildMusicSettings settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(event.getGuild());
                Playlist playlist = sia.getServiceManagers().getPlaylistService().getGuildPlaylist(event.getGuild());
                sia.getServiceManagers().getPlaylistService().delete(playlist.getId());

                settings.setPlaylist(null);
                sia.getServiceManagers().getGuildMusicSettingsService().save(settings);
                event.reply(event.getClient().getSuccess() + " Successfully deleted playlist!");
            } catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }

    public class DeleteMultipleSongsCommand extends AbstractMusicModeratorCommand {
        public DeleteMultipleSongsCommand(Sia sia) {
            super(sia);
            this.name = "deletesongs";
            this.aliases = new String[]{"dsongs"};
            this.help = "deletes multiple songs from a playlist";
            this.arguments = "<name> | <name> | ...";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event) {
            String[] songNames = event.getArgs().split("\\|+", 2);
            try {
                for ( String songName : songNames )
                {
                    songName = songName.trim().replaceAll(" ", "_").toUpperCase();

                    Playlist playlist = sia.getServiceManagers().getPlaylistService().getGuildPlaylist(event.getGuild());

                    try {
                        sia.getServiceManagers().getSongService().deleteSongByNameAndPlaylist(songName, playlist);
                    } catch (NoSongException e) {
                        event.replyError(e.getMessage());
                        if ( sia.isDebugMode() )
                            sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                                event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
                    }
                    event.reply(event.getClient().getSuccess() + " Successfully deleted Song `" + songName
                        + "` From Playlist: `"+ playlist.getPlaylistName() +"`!");
                }
            } catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
            event.getMessage().delete().queue();
        }
    }

    public class DeleteOneSongCommand extends AbstractMusicModeratorCommand {
        public DeleteOneSongCommand(Sia sia) {
            super(sia);
            this.name = "deletesong";
            this.aliases = new String[]{"dsong"};
            this.help = "deletes one song from a playlist";
            this.arguments = "<name>";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            try {
                String songName = event.getArgs().trim().replaceAll(" ", "_").toUpperCase();

                Playlist playlist = sia.getServiceManagers().getPlaylistService().getGuildPlaylist(event.getGuild());

                try {
                    sia.getServiceManagers().getSongService().deleteSongByNameAndPlaylist(songName, playlist);
                } catch (NoSongException e) {
                    event.replyError(e.getMessage());
                    if ( sia.isDebugMode() )
                        sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                            event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
                    return;
                }
                event.reply(event.getClient().getSuccess() + " Successfully deleted Song `" + songName
                    + "` From Playlist: `"+ playlist.getPlaylistName() +"`!");

                event.getMessage().delete().queue();
            }
            catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }

    public class AppendOneCommand extends AbstractMusicModeratorCommand {
        public AppendOneCommand(Sia sia) {
            super(sia);
            this.guildOnly = true;
            this.name = "append";
            this.aliases = new String[]{"add"};
            this.help = "appends a song to an existing playlist";
            this.arguments = "<URL> | [Track Name]";
        }

        @Override
        public void doCommand(CommandEvent event) {
            String[] parts = event.getArgs().split("\\|", 2);
            if (parts.length <= 1) {
                event.replyError(" Please include URL and Names to add and ensure you separate the " +
                    "`URL` and `Track Name` with the `|` symbol!");
                return;
            }
            try {
                Playlist playlist = sia.getServiceManagers().getPlaylistService().getGuildPlaylist(event.getGuild());

                String query = parts[0].trim();
                String name = parts[1].trim().replaceAll(" ", "_").toUpperCase();

                if (query.startsWith("<") && query.endsWith(">"))
                    query = query.substring(1, query.length() - 1);
                if (name.startsWith("[") && name.endsWith("]"))
                    name = name.substring(1, name.length() - 1);

                Songs song = sia.getServiceManagers().getSongService().createSong(name, query, playlist);
                sia.getServiceManagers().getPlaylistService().addSongToGuildPlaylist(event.getGuild(), song);

                event.reply(event.getClient().getSuccess()
                    + " Successfully added "
                    + name
                    + " to playlist `"
                    + playlist.getPlaylistName() + "`!");
                event.getMessage().delete().queue();

            } catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }


    public class AppendMultipleCommand extends AbstractMusicModeratorCommand {
        public AppendMultipleCommand(Sia sia) {
            super(sia);
            this.guildOnly = true;
            this.name = "appendmultiple";
            this.aliases = new String[]{"addmultiple"};
            this.help = "appends songs to an existing playlist";
            this.arguments = "<URL> \\[Track Name] | <URL> \\[Track Name] | ...";
        }


        @Override
        public void doCommand(CommandEvent event) {
//            String[] parts = event.getArgs().split("\\s+", 2);
            String[] parts = event.getArgs().split("\\|", 2); // Split the urls by the pipes
            if (parts.length < 1) {
                event.replyError(event.getClient().getError() + " Please include URLs and Names to add!");
                return;
            }
            //String pname = parts[0];
            try {
                Playlist playlist = sia.getServiceManagers().getPlaylistService().getGuildPlaylist(event.getGuild());
//                String[] urls = parts[0].split("\\|");
                List<Pair<String, String>> playListInfo = new ArrayList<>();
                for (String part : parts) {
                    String[] splitPart = part.split("\\\\", -1);
                    playListInfo.add(new Pair<>(splitPart[1], splitPart[0]));
                }

                for (Pair<String, String> playlistPair : playListInfo) {
                    String query = playlistPair.getValue().trim();
                    String name = playlistPair.getKey().trim().replaceAll(" ", "_").toUpperCase();
                    if (query.startsWith("<") && query.endsWith(">"))
                        query = query.substring(1, query.length() - 1);
                    if (name.startsWith("[") && name.endsWith("]"))
                        name = name.substring(1, name.length() - 1);
                    Songs song = sia.getServiceManagers().getSongService().createSong(name, query, playlist);
                    sia.getServiceManagers().getPlaylistService().addSongToGuildPlaylist(event.getGuild(), song);
                }
                event.reply(event.getClient().getSuccess()
                    + " Successfully added "
                    + parts.length
                    + " items to playlist `"
                    + playlist.getPlaylistName() + "`!");
                event.getMessage().delete().queue();
            } catch (NoPlaylistFoundException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
            event.getMessage().delete().queue();
        }
    }


    public class DisplayPlaylistCmd extends AbstractMusicModeratorCommand {
        public DisplayPlaylistCmd(Sia sia) {
            super(sia);
            this.guildOnly = true;
            this.name = "displayplaylist";
            this.aliases = new String[]{"dp", "dplaylist"};
            this.help = "Shows information on the Guild's default playlist";
        }

        @Override
        public void doCommand(CommandEvent event) {
            PremiumInfo pi = sia.getServiceManagers().getPremiumService().getPremiumInfo(event.getGuild());
            event.getChannel().sendMessage(new MessageBuilder().append("**")
                .append(event.getSelfUser().getName()).append("** settings on **")
                .append(event.getGuild().getName()).append("**:")
                .setEmbed(new EmbedBuilder()
                    .addField(sia.getServiceManagers().getPlaylistService().getGuildPlaylistDisplay(event.getGuild()))
                    .setFooter(pi.getFooterString(), null)
                    .setTimestamp(pi.getTimestamp())
                    .setColor(event.getSelfMember().getColor())
                    .build()
                ).build()
            ).queue();

        }
    }
}


//    public class DefaultlistCmd extends AbstractMusicModeratorCommand
//    {
//        public DefaultlistCmd(Sia sia)
//        {
//            super(sia);
//            this.name = "setdefault";
//            this.aliases = new String[]{"default"};
//            this.arguments = "<playlistname|NONE>";
//            this.guildOnly = true;
//        }
//
//        @Override
//        protected void execute(CommandEvent event) {
//
//        }
//    }

//    public class ListCmd extends AbstractMusicModeratorCommand
//    {
//        public ListCmd(Sia sia)
//        {
//            super(sia);
//            this.name = "all";
//            this.aliases = new String[]{"available","list"};
//            this.help = "lists all available playlists";
//            this.guildOnly = true;
//        }
//
//        @Override
//        protected void execute(CommandEvent event)
//        {
//            if(!bot.getPlaylistLoader().folderExists())
//                bot.getPlaylistLoader().createFolder();
//            if(!bot.getPlaylistLoader().folderExists())
//            {
//                event.reply(event.getClient().getWarning()+" Playlists folder does not exist and could not be created!");
//                return;
//            }
//            List<String> list = bot.getPlaylistLoader().getPlaylistNames();
//            if(list==null)
//                event.reply(event.getClient().getError()+" Failed to load available playlists!");
//            else if(list.isEmpty())
//                event.reply(event.getClient().getWarning()+" There are no playlists in the Playlists folder!");
//            else
//            {
//                StringBuilder builder = new StringBuilder(event.getClient().getSuccess()+" Available playlists:\n");
//                list.forEach(str -> builder.append("`").append(str).append("` "));
//                event.reply(builder.toString());
//            }
//        }
//    }

