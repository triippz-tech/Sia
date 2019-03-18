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
package com.trievosoftware.discord.music.playlist;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.domain.Playlist;
import com.trievosoftware.application.domain.Songs;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.*;
import java.util.function.Consumer;

/**
 *
 * @author Mark Tripoli (triippz)
 */
@SuppressWarnings("Duplicates")
public class PlaylistLoader
{
    private final Sia sia;
    
    public PlaylistLoader(Sia sia)
    {
        this.sia = sia;
    }

    /**
     * Returns a Playlist for a User
     *
     * @param name the name of the playlist
     * @param user the user requesting
     * @return PlaylistLoader.Playlist or NULL
     * @throws NoPlaylistFoundException No playlist in database found
     */
    public DiscordPlaylist getUserPlaylist(Guild guild, String name, User user) throws NoPlaylistFoundException
    {
        Playlist playlist = sia.getServiceManagers().getPlaylistService().getUserPlaylistByName(user.getIdLong(), name);

        boolean[] shuffle = {false};
        List<String> list = new ArrayList<>();

        for ( Songs song : playlist.getSongs() )
        {
            String s = song.getSongQuery().trim();
            if ( !s.isEmpty() ) {

                if (s.startsWith("#") || s.startsWith("//")) {
                    s = s.replaceAll("\\s+", "");
                    if (s.equalsIgnoreCase("#shuffle") || s.equalsIgnoreCase("//shuffle"))
                        shuffle[0] = true;
                } else
                    list.add(s);
            }
        }

        if ( shuffle[0] )
            shuffle(list);
        return new DiscordPlaylist(guild, name, list, shuffle[0]);
    }

    public DiscordPlaylist getGuildPlaylist (Long guildId) throws NoPlaylistFoundException {
        Guild guild = sia.getJDA(guildId).getGuildById(guildId);
        return getGuildPlaylist(guild);
    }

    /**
     * Returns the Guild's default playlist
     *
     * @param guild the guild request
     * @return PlaylistLoader.Playlist or NULL
     */
    public DiscordPlaylist getGuildPlaylist(Guild guild) throws NoPlaylistFoundException {
        Playlist playlist = sia.getServiceManagers().getPlaylistService().getGuildPlaylist(guild);

        boolean[] shuffle = {false};
        List<String> list = new ArrayList<>();

        playlist.getSongs().forEach(song ->
        {
            String s = song.getSongQuery().trim();
            if (s.isEmpty())
                return;
            if (s.startsWith("#") || s.startsWith("//")) {
                s = s.replaceAll("\\s+", "");
                if (s.equalsIgnoreCase("#shuffle") || s.equalsIgnoreCase("//shuffle"))
                    shuffle[0] = true;
            } else
                list.add(s);
        });
        if (shuffle[0])
            shuffle(list);
        return new DiscordPlaylist(guild, playlist.getPlaylistName(), list, shuffle[0]);
    }
    
    
    private static <T> void shuffle(List<T> list)
    {
        for(int first =0; first<list.size(); first++)
        {
            int second = (int)(Math.random()*list.size());
            T tmp = list.get(first);
            list.set(first, list.get(second));
            list.set(second, tmp);
        }
    }
    
    
    public class DiscordPlaylist
    {
        private final String name;
        private final List<String> items;
        private final boolean shuffle;
        private final List<AudioTrack> tracks = new LinkedList<>();
        private final List<PlaylistLoadError> errors = new LinkedList<>();
        private boolean loaded = false;
        private GuildMusicSettings settings;

        private DiscordPlaylist(Guild guild, String name, List<String> items, boolean shuffle)
        {
            this.name = name;
            this.items = items;
            this.shuffle = shuffle;

            settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(guild);
        }
        
        public void loadTracks(Guild guild, AudioPlayerManager manager, Consumer<AudioTrack> consumer, Runnable callback)
        {
            if(!loaded)
            {
                loaded = true;
                for(int i=0; i<items.size(); i++)
                {
                    boolean last = i+1==items.size();
                    int index = i;
                    manager.loadItemOrdered(name, items.get(i), new AudioLoadResultHandler()
                    {
                        @Override
                        public void trackLoaded(AudioTrack at)
                        {
//                            GuildMusicSettings settings = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(guild);
                            if(settings.isTooLong(at))
                                errors.add(new PlaylistLoadError(index, items.get(index), "This track is longer than the allowed maximum"));
                            else
                            {
                                at.setUserData(0L);
                                tracks.add(at);
                                consumer.accept(at);
                            }
                            if(last && callback!=null)
                                callback.run();
                        }
                        
                        @Override
                        public void playlistLoaded(AudioPlaylist ap)
                        {
                            if(ap.isSearchResult())
                            {
                                if(sia.getServiceManagers().getGuildMusicSettingsService().getSettings(guild).isTooLong(ap.getTracks().get(0)))
                                    errors.add(new PlaylistLoadError(index, items.get(index), "This track is longer than the allowed maximum"));
                                else
                                {
                                    ap.getTracks().get(0).setUserData(0L);
                                    tracks.add(ap.getTracks().get(0));
                                    consumer.accept(ap.getTracks().get(0));
                                }
                            }
                            else if(ap.getSelectedTrack()!=null)
                            {
                                if(sia.getServiceManagers().getGuildMusicSettingsService().getSettings(guild).isTooLong(ap.getSelectedTrack()))
                                    errors.add(new PlaylistLoadError(index, items.get(index), "This track is longer than the allowed maximum"));
                                else
                                {
                                    ap.getSelectedTrack().setUserData(0L);
                                    tracks.add(ap.getSelectedTrack());
                                    consumer.accept(ap.getSelectedTrack());
                                }
                            }
                            else
                            {
                                List<AudioTrack> loaded = new ArrayList<>(ap.getTracks());
                                if(shuffle)
                                    for(int first =0; first<loaded.size(); first++)
                                    {
                                        int second = (int)(Math.random()*loaded.size());
                                        AudioTrack tmp = loaded.get(first);
                                        loaded.set(first, loaded.get(second));
                                        loaded.set(second, tmp);
                                    }
                                loaded.removeIf(track -> sia.getServiceManagers().getGuildMusicSettingsService().getSettings(guild).isTooLong(track));
                                loaded.forEach(at -> at.setUserData(0L));
                                tracks.addAll(loaded);
                                loaded.forEach(consumer);
                            }
                            if(last && callback!=null)
                                callback.run();
                        }

                        @Override
                        public void noMatches() 
                        {
                            errors.add(new PlaylistLoadError(index, items.get(index), "No matches found."));
                            if(last && callback!=null)
                                callback.run();
                        }

                        @Override
                        public void loadFailed(FriendlyException fe)
                        {
                            errors.add(new PlaylistLoadError(index, items.get(index), "Failed to load track: "+fe.getLocalizedMessage()));
                            if(last && callback!=null)
                                callback.run();
                        }
                    });
                }
            }
            if(shuffle)
                shuffleTracks();
        }
        
        public void shuffleTracks()
        {
            if(tracks!=null)
                shuffle(tracks);
        }
        
        public String getName()
        {
            return name;
        }

        public List<String> getItems()
        {
            return items;
        }

        public List<AudioTrack> getTracks()
        {
            return tracks;
        }
        
        public List<PlaylistLoadError> getErrors()
        {
            return errors;
        }
    }
    
    public class PlaylistLoadError
    {
        private final int number;
        private final String item;
        private final String reason;
        
        private PlaylistLoadError(int number, String item, String reason)
        {
            this.number = number;
            this.item = item;
            this.reason = reason;
        }
        
        public int getIndex()
        {
            return number;
        }
        
        public String getItem()
        {
            return item;
        }
        
        public String getReason()
        {
            return reason;
        }
    }
}
