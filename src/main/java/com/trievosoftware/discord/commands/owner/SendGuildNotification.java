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

package com.trievosoftware.discord.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractOwnerCommand;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;

public class SendGuildNotification extends AbstractOwnerCommand
{
    public SendGuildNotification(Sia sia) {
        super(sia);
        this.name = "notifyusers";
        this.help = "Sends a mass notification to all guilds";
        this.children = new AbstractOwnerCommand[]{
            new FileSendCommand(sia),
            new NotificationCommand(sia)
        };
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void doCommand(CommandEvent event)
    {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning()+" Owner Notification Commands:\n");
        for(Command cmd: this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments()==null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }


    public class FileSendCommand extends AbstractOwnerCommand {

        public FileSendCommand (Sia sia)
        {
            super(sia);
            this.name = "filesend";
            this.help = "Sends a file to all guilds using this bot (patch notes, etc.)";
        }

        @Override
        public void doCommand(CommandEvent event)
        {

        }
    }

    public class NotificationCommand extends AbstractOwnerCommand
    {
        public NotificationCommand (Sia sia)
        {
            super(sia);
            this.name = "notification";
            this.help = "Sends a mass notification to all guilds using the bot";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            event.reply("This command is in development");
            List<Guild> guildList = sia.getShardManager().getGuilds();

            for ( Guild guild : guildList )
            {
                // I would rather add a new column to the guild settings database,
                // called "Bot Update Channel" which would be the ID to a Text Channel
                // Which is automatically setup during "setup" for each guild.
                // This channel will have a webhook attached, where the bot OWNER
                // and ONLY THE OWNER can send out mass updates to all guilds using the bot
                // Such as: Scheduled Downtime, Updates, Patch Notes, etc.
                guild.getDefaultChannel().sendMessage(event.getArgs()).queue();
            }
        }
    }
}
