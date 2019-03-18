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

package com.trievosoftware.discord.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.trievosoftware.application.domain.GuildMusicSettings;
import com.trievosoftware.application.exceptions.NoPlaylistFoundException;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.music.playlist.PlaylistLoader.DiscordPlaylist;
import com.trievosoftware.discord.music.queue.FairQueue;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AudioHandler extends AudioEventAdapter implements AudioSendHandler {

    private final FairQueue<QueuedTrack> queue = new FairQueue<>();
    private final List<AudioTrack> defaultQueue = new LinkedList<>();
    private final Set<String> votes = new HashSet<>();

    private final PlayerManager manager;
    private final AudioPlayer audioPlayer;
    private long guildId;
    private long userId;

    private AudioFrame lastFrame;

    protected AudioHandler(PlayerManager manager, Guild guild, AudioPlayer player)
    {
        this.manager = manager;
        this.audioPlayer = player;
        this.guildId = guild.getIdLong();
    }

    protected AudioHandler(PlayerManager manager, User user, AudioPlayer player)
    {
        this.manager = manager;
        this.audioPlayer = player;
        this.userId = user.getIdLong();
    }

    public int addTrackToFront(QueuedTrack qtrack)
    {
        if(audioPlayer.getPlayingTrack()==null)
        {
            audioPlayer.playTrack(qtrack.getTrack());
            return -1;
        }
        else
        {
            queue.addAt(0, qtrack);
            return 0;
        }
    }

    public int addTrack(QueuedTrack qtrack)
    {
        if(audioPlayer.getPlayingTrack()==null)
        {
            audioPlayer.playTrack(qtrack.getTrack());
            return -1;
        }
        else
            return queue.add(qtrack);
    }

    public FairQueue<QueuedTrack> getQueue()
    {
        return queue;
    }

    public void stopAndClear()
    {
        queue.clear();
        defaultQueue.clear();
        audioPlayer.stopTrack();
        //current = null;
    }

    public boolean isMusicPlaying(JDA jda)
    {
        return guild(jda).getSelfMember().getVoiceState().inVoiceChannel() && audioPlayer.getPlayingTrack()!=null;
    }

    public Set<String> getVotes()
    {
        return votes;
    }

    public AudioPlayer getPlayer()
    {
        return audioPlayer;
    }

    public long getRequester()
    {
        if(audioPlayer.getPlayingTrack()==null || audioPlayer.getPlayingTrack().getUserData(Long.class)==null)
            return 0;
        return audioPlayer.getPlayingTrack().getUserData(Long.class);
    }

    public boolean playFromGuildDefault()
    {
        if(!defaultQueue.isEmpty())
        {
            audioPlayer.playTrack(defaultQueue.remove(0));
            return true;
        }
        GuildMusicSettings settings = manager.getSia().getServiceManagers().getGuildMusicSettingsService().getSettings(guildId);
        if(settings==null || settings.getPlaylist()==null)
            return false;

        try {
            DiscordPlaylist playlist = manager.getSia().getPlaylistLoader().getGuildPlaylist(guildId);

            if(playlist==null || playlist.getItems().isEmpty())
                return false;

            playlist.loadTracks(manager.getSia().getJDA(guildId).getGuildById(guildId), manager, (audioTrack) ->
            {
                if(audioPlayer.getPlayingTrack()==null)
                    audioPlayer.playTrack(audioTrack);
                else
                    defaultQueue.add(audioTrack);
            }, () ->
            {
                if(playlist.getTracks().isEmpty() && !manager.getSia().getServiceManagers()
                    .getGuildMusicSettingsService().getSettings(guildId).getStayInChannel())
                    manager.getSia().closeAudioConnection(guildId);
            });
            return true;

        } catch (NoPlaylistFoundException e) {

            return false;
        }
    }

    // Audio Events
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        // if the track ended normally, and we're in repeat mode, re-add it to the queue
        if(endReason==AudioTrackEndReason.FINISHED && manager.getSia().getServiceManagers().getGuildMusicSettingsService().getSettings(guildId).isRepeat())
        {
            queue.add(new QueuedTrack(track.makeClone(), track.getUserData(Long.class)==null ? 0L : track.getUserData(Long.class)));
        }

        if(queue.isEmpty())
        {
            if(!playFromGuildDefault())
            {
                manager.getSia().getNowplayingHandler().onTrackUpdate(guildId, null, this);
                if(!manager.getSia().getServiceManagers().getGuildMusicSettingsService().getSettings(guildId).getStayInChannel())
                    manager.getSia().closeAudioConnection(guildId);
            }
        }
        else
        {
            QueuedTrack qt = queue.pull();
            player.playTrack(qt.getTrack());
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {
        votes.clear();
        manager.getSia().getNowplayingHandler().onTrackUpdate(guildId, track, this);
    }


    // Formatting
    public Message getNowPlaying(JDA jda)
    {
        if(isMusicPlaying(jda))
        {
            Guild guild = guild(jda);
            AudioTrack track = audioPlayer.getPlayingTrack();
            MessageBuilder mb = new MessageBuilder();
            mb.append(FormatUtil.filter(Constants.SUCCESS +" **Now Playing in "+guild.getSelfMember().getVoiceState().getChannel().getName()+"...**"));
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(guild.getSelfMember().getColor());
            if(getRequester() != 0)
            {
                User u = guild.getJDA().getUserById(getRequester());
                if(u==null)
                    eb.setAuthor("Unknown (ID:"+getRequester()+")", null, null);
                else
                    eb.setAuthor(u.getName()+"#"+u.getDiscriminator(), null, u.getEffectiveAvatarUrl());
            }

            try
            {
                eb.setTitle(track.getInfo().title, track.getInfo().uri);
            }
            catch(Exception e)
            {
                eb.setTitle(track.getInfo().title);
            }

            if(track instanceof YoutubeAudioTrack && manager.getSia().getServiceManagers()
                .getGuildMusicSettingsService().getSettings(guildId).getNowPlayingImages())
            {
                eb.setThumbnail("https://img.youtube.com/vi/"+track.getIdentifier()+"/mqdefault.jpg");
            }

            if(track.getInfo().author != null && !track.getInfo().author.isEmpty())
                eb.setFooter("Source: " + track.getInfo().author, null);

            double progress = (double)audioPlayer.getPlayingTrack().getPosition()/track.getDuration();
            eb.setDescription((audioPlayer.isPaused() ? Constants.Music.PAUSE_EMOJI : Constants.Music.PLAY_EMOJI)
                + " "+ FormatUtil.progressBar(progress)
                + " `[" + FormatUtil.formatTime(track.getPosition()) + "/" + FormatUtil.formatTime(track.getDuration()) + "]` "
                + FormatUtil.volumeIcon(audioPlayer.getVolume()));

            return mb.setEmbed(eb.build()).build();
        }
        else return null;
    }

    public Message getNoMusicPlaying(JDA jda)
    {
        Guild guild = guild(jda);
        return new MessageBuilder()
            .setContent(FormatUtil.filter(Constants.SUCCESS+" **Now Playing...**"))
            .setEmbed(new EmbedBuilder()
                .setTitle("No music playing")
                .setDescription(Constants.Music.STOP_EMOJI+" "+ FormatUtil.progressBar(-1)+" "+ FormatUtil.volumeIcon(audioPlayer.getVolume()))
                .setColor(guild.getSelfMember().getColor())
                .build()).build();
    }

    public String getTopicFormat(JDA jda)
    {
        if(isMusicPlaying(jda))
        {
            long userid = getRequester();
            AudioTrack track = audioPlayer.getPlayingTrack();
            String title = track.getInfo().title;
            if(title==null || title.equals("Unknown Title"))
                title = track.getInfo().uri;
            return "**"+title+"** ["+(userid==0 ? "autoplay" : "<@"+userid+">")+"]"
                + "\n" + (audioPlayer.isPaused() ? Constants.Music.PAUSE_EMOJI : Constants.Music.PLAY_EMOJI) + " "
                + "[" + FormatUtil.formatTime(track.getDuration()) + "] "
                + FormatUtil.volumeIcon(audioPlayer.getVolume());
        }
        else return "No music playing " + Constants.Music.STOP_EMOJI + " " + FormatUtil.volumeIcon(audioPlayer.getVolume());
    }

    // Audio Send Handler methods
    @Override
    public boolean canProvide()
    {
        if (lastFrame == null)
            lastFrame = audioPlayer.provide();

        return lastFrame != null;
    }

    @Override
    public byte[] provide20MsAudio()
    {
        if (lastFrame == null)
            lastFrame = audioPlayer.provide();

        byte[] data = lastFrame != null ? lastFrame.getData() : null;
        lastFrame = null;

        return data;
    }

    @Override
    public boolean isOpus()
    {
        return true;
    }


    // Private methods
    private Guild guild(JDA jda)
    {
        return jda.getGuildById(guildId);
    }
}
