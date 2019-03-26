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

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.domain.PremiumInfo;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractMusicModeratorCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;

public class MusicSettingsCommand extends AbstractMusicModeratorCommand {

    public MusicSettingsCommand(Sia sia) {
        super(sia);
        this.name = "msettings";
        this.aliases = new String[]{"musicsettings"};
        this.help = "shows current music settings";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        PremiumInfo pi = sia.getServiceManagers().getPremiumService().getPremiumInfo(event.getGuild());
        event.getChannel().sendMessage(new MessageBuilder().append("**")
            .append(event.getSelfUser().getName()).append("** music settings on **")
            .append(event.getGuild().getName()).append("**:")
            .setEmbed(new EmbedBuilder()
                .addField(sia.getServiceManagers().getGuildMusicSettingsService().getSettingsDisplay(event.getGuild()))
                .addField("Total Servers:", String.valueOf(event.getJDA().getGuilds().size()), false)
                .addField("Audio Connections: ",
                    String.valueOf(event.getJDA().getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count())
                    , false)
                .setFooter(pi.getFooterString(), null)
                .setTimestamp(pi.getTimestamp())
                .setColor(event.getSelfMember().getColor())
                .build()
            )
            .build()
        ).queue();
    }

}
