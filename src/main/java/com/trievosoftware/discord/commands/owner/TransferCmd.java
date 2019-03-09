/*
 * Copyright 2018 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trievosoftware.discord.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class TransferCmd extends Command
{
    private final Sia sia;
    
    public TransferCmd(Sia sia)
    {
        this.sia = sia;
        this.name = "transfer";
        this.help = "transfers old settings";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.hidden = true;
    }
    
    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            List<String> list = Files.readAllLines(Paths.get("transfer.txt"));
            HashMap<Long,String[]> oldSettings = new HashMap<>();
            list.stream().map(str -> str.split(" ")).forEach(split -> oldSettings.put(Long.parseLong(split[0]), split));
            int mcount = 0;
            int count = 0;
            for(Guild g: sia.getShardManager().getGuilds())
            {
                if(!oldSettings.containsKey(g.getIdLong()))
                    continue;
                if(sia.getServiceManagers().getGuildSettingsService().hasSettings(g))
                    continue;
                if(sia.getServiceManagers().getGuildSettingsService().hasSettings(g))
                    continue;
                String[] split = oldSettings.get(g.getIdLong());
                int maxmentions = Integer.parseInt(split[4]);
                TextChannel modlog = g.getTextChannelById(Long.parseLong(split[5]));
                boolean autoraid = Boolean.parseBoolean(split[6]);
                int spamLimit = Integer.parseInt(split[2]);
                if("N".equalsIgnoreCase(split[3]))
                    spamLimit = 0;
                int inviteStrikes = "N".equalsIgnoreCase(split[1]) ? 0 : 1;
                if(modlog!=null)
                {
                    sia.getServiceManagers().getGuildSettingsService().setModLogChannel(g, modlog);
                    mcount++;
                }
                if(maxmentions<7 && !autoraid && spamLimit<=3 && inviteStrikes==0)
                    continue;
                if(sia.getServiceManagers().getActionsService().useDefaultSettings(g))
                {
                    count++;
                    if(autoraid)
                        sia.getServiceManagers().getAutoModService().setAutoRaidMode(g, 10, 10);
                    if(maxmentions>=7)
                        sia.getServiceManagers().getAutoModService().setMaxMentions(g, maxmentions);
                    if(inviteStrikes>0)
                        sia.getServiceManagers().getAutoModService().setInviteStrikes(g, inviteStrikes);
                    if(spamLimit>3)
                        sia.getServiceManagers().getAutoModService().setDupeSettings(g, 1, 3, spamLimit);
                }
            }
            event.replySuccess("Complete!\nLoaded: "+oldSettings.size()+"\nModlogs: "+mcount+"\nAutomod: "+count);
        }
        catch(Exception ex)
        {
            event.replyError("Error: "+ex+"\nat "+ex.getStackTrace()[0]);
            
        }
    }
}
