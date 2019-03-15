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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.Pair;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class NowplayingHandler
{
    private final Sia sia;
    private final HashMap<Long, Pair<Long, Long>> lastNP; // guild -> channel,message
    
    public NowplayingHandler(Sia sia)
    {
        this.sia = sia;
        this.lastNP = new HashMap<>();
    }
    
    public void init()
    {
        sia.getThreadpool().scheduleWithFixedDelay(() -> updateAll(), 0, 5, TimeUnit.SECONDS);
    }
    
    public void setLastNPMessage(Message m)
    {
        lastNP.put(m.getGuild().getIdLong(), new Pair<>(m.getTextChannel().getIdLong(), m.getIdLong()));
    }
    
    public void clearLastNPMessage(Guild guild)
    {
        lastNP.remove(guild.getIdLong());
    }
    
    private void updateAll()
    {
        Set<Long> toRemove = new HashSet<>();
        for(long guildId: lastNP.keySet())
        {
            Guild guild = sia.getJDA(guildId).getGuildById(guildId);
            if(guild==null)
            {
                toRemove.add(guildId);
                continue;
            }
            Pair<Long, Long> pair = lastNP.get(guildId);
            TextChannel tc = guild.getTextChannelById(pair.getKey());
            if(tc==null)
            {
                toRemove.add(guildId);
                continue;
            }
            AudioHandler handler = (AudioHandler)guild.getAudioManager().getSendingHandler();
            Message msg = handler.getNowPlaying(sia.getJDA(guildId));
            if(msg==null)
            {
                msg = handler.getNoMusicPlaying(sia.getJDA(guildId));
                toRemove.add(guildId);
            }
            try 
            {
                tc.editMessageById(pair.getValue(), msg).queue(m->{}, t -> lastNP.remove(guildId));
            } 
            catch(Exception e)
            {
                toRemove.add(guildId);
            }
        }
        toRemove.forEach(id -> lastNP.remove(id));
    }
    
    public void updateTopic(long guildId, AudioHandler handler, boolean wait)
    {
        Guild guild = sia.getJDA(guildId).getGuildById(guildId);
        if(guild==null)
            return;
        TextChannel tchan = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(guildId).getTextChannelId(guild);
        if(tchan!=null && guild.getSelfMember().hasPermission(tchan, Permission.MANAGE_CHANNEL))
        {
            String otherText;
            if(tchan.getTopic()==null || tchan.getTopic().isEmpty())
                otherText = "\u200B";
            else if(tchan.getTopic().contains("\u200B"))
                otherText = tchan.getTopic().substring(tchan.getTopic().lastIndexOf("\u200B"));
            else
                otherText = "\u200B\n "+tchan.getTopic();
            String text = handler.getTopicFormat(sia.getJDA(guildId)) + otherText;
            if(!text.equals(tchan.getTopic()))
            {
                try 
                {
                    if(wait)
                        tchan.getManager().setTopic(text).complete();
                    else
                        tchan.getManager().setTopic(text).queue();
                } 
                catch(PermissionException ignore) {}
            }
        }
    }
    
    // "event"-based methods
    public void onTrackUpdate(long guildId, AudioTrack track, AudioHandler handler)
    {
        // update sia status if applicable
        boolean songInStatus = false;
        if(songInStatus)
        {
            if(track!=null && sia.getJDA(guildId).getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count()<=1)
                sia.getJDA(guildId).getPresence().setGame(Game.listening(track.getInfo().title));
            else
                sia.getJDA(guildId).getPresence().setGame(Game.listening("type -help"));
        }
        
        // update channel topic if applicable
        updateTopic(guildId, handler, false);
    }
    
    public void onMessageDelete(Guild guild, long messageId)
    {
        Pair<Long, Long> pair = lastNP.get(guild.getIdLong());
        if(pair==null)
            return;
        if(pair.getValue() == messageId)
            lastNP.remove(guild.getIdLong());
    }
}
