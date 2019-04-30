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
package com.trievosoftware.discord.commands.settings;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.domain.PremiumInfo;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class SettingsCmd extends AbstractModeratorCommand
{
    public SettingsCmd(Sia sia)
    {
        super(sia);
        this.name = "settings";
        this.category = new Category("Settings");
        this.help = "shows current settings";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }
    
    @Override
    public void doCommand(CommandEvent event)
    {
        PremiumInfo pi = sia.getServiceManagers().getPremiumService().getPremiumInfo(event.getGuild());
        DiscordGuild discordGuild = sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild());
        event.getChannel().sendMessage(new MessageBuilder().append("**")
            .append(event.getSelfUser().getName()).append("** settings on **")
            .append(event.getGuild().getName()).append("**:")
                .setEmbed(new EmbedBuilder()
                        //.setThumbnail(event.getGuild().getIconId()==null ? event.getSelfUser().getEffectiveAvatarUrl() : event.getGuild().getIconUrl())
                        .addField(sia.getServiceManagers().getGuildSettingsService().getSettingsDisplay(discordGuild, event.getGuild()))
                        .addField(sia.getServiceManagers().getActionsService().getAllPunishmentsDisplay(event.getGuild()))
                        .addField(sia.getServiceManagers().getAutoModService().getSettingsDisplay(event.getGuild()))
                        .setFooter(pi.getFooterString(), null)
                        .setTimestamp(pi.getTimestamp())
                        .setColor(event.getSelfMember().getColor())
                        .build()
                ).build()
        ).queue();
    }
    
}
