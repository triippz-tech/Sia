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

package com.trievosoftware.discord.commands.moderation;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import net.dv8tion.jda.core.entities.Game;

@SuppressWarnings("Duplicates")
public class SetGameCommand extends AbstractModeratorCommand
{
    public SetGameCommand(Sia sia)
    {
        super(sia);
        this.name = "setgame";
        this.help = "sets the game the bot is playing";
        this.arguments = "[action] [game]";
        this.guildOnly = false;
        this.children = new AbstractModeratorCommand[]{
            new SetlistenCmd(sia),
            new SetstreamCmd(sia),
            new SetwatchCmd(sia)
        };
    }

    @Override
    protected void execute(CommandEvent event)
    {
        String title = event.getArgs().toLowerCase().startsWith("playing") ? event.getArgs().substring(7).trim() : event.getArgs();
        try
        {
            event.getGuild().getJDA().getPresence().setGame(title.isEmpty() ? null : Game.playing(title));
            event.reply(event.getClient().getSuccess()+" **"+event.getSelfUser().getName()
                +"** is "+(title.isEmpty() ? "no longer playing anything." : "now playing `"+title+"`"));
        }
        catch(Exception e)
        {
            event.reply(event.getClient().getError()+" The game could not be set!");
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
        }
    }

    private class SetstreamCmd extends AbstractModeratorCommand
    {
        private SetstreamCmd(Sia sia)
        {
            super(sia);
            this.name = "stream";
            this.aliases = new String[]{"twitch","streaming"};
            this.help = "sets the game the bot is playing to a stream";
            this.arguments = "<username> <game>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            String[] parts = event.getArgs().split("\\s+", 2);
            if(parts.length<2)
            {
                event.replyError("Please include a twitch username and the name of the game to 'stream'");
                return;
            }
            try
            {
                event.getGuild().getJDA().getPresence().setGame(Game.streaming(parts[1], "https://twitch.tv/"+parts[0]));
                event.replySuccess("**"+event.getSelfUser().getName()
                    +"** is now streaming `"+parts[1]+"`");
            }
            catch(Exception e)
            {
                event.reply(event.getClient().getError()+" The game could not be set!");
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }

    private class SetlistenCmd extends AbstractModeratorCommand
    {
        private SetlistenCmd(Sia sia)
        {
            super(sia);
            this.name = "listen";
            this.aliases = new String[]{"listening"};
            this.help = "sets the game the bot is listening to";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("Please include a title to listen to!");
                return;
            }
            String title = event.getArgs().toLowerCase().startsWith("to") ? event.getArgs().substring(2).trim() : event.getArgs();
            try
            {
                event.getGuild().getJDA().getPresence().setGame(Game.listening(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** is now listening to `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" The game could not be set!");
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }

    private class SetwatchCmd extends AbstractModeratorCommand
    {
        private SetwatchCmd(Sia sia)
        {
            super(sia);
            this.name = "watch";
            this.aliases = new String[]{"watching"};
            this.help = "sets the game the bot is watching";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("Please include a title to watch!");
                return;
            }
            String title = event.getArgs();
            try
            {
                event.getGuild().getJDA().getPresence().setGame(Game.watching(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** is now watching `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" The game could not be set!");
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }

    private class SetPlayCommand extends AbstractModeratorCommand
    {
        private SetPlayCommand(Sia sia)
        {
            super(sia);
            this.name = "play";
            this.aliases = new String[]{"playing"};
            this.help = "sets the game the bot is playing";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("Please include a title to play!");
                return;
            }
            String title = event.getArgs();
            try
            {
                event.getGuild().getJDA().getPresence().setGame(Game.playing(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** is now playing `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" The game could not be set!");
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
        }
    }
}
