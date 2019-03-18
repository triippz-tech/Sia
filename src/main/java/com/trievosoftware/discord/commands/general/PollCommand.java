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

package com.trievosoftware.discord.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class PollCommand extends AbstractModeratorCommand
{

    public PollCommand(Sia sia)
    {
        super(sia, Permission.MANAGE_ROLES);
        this.name = "poll";
        this.arguments = "create|active";
        this.help = "Poll management";
        this.guildOnly = true;
        this.children = new Command[] {
                new CreatePollCommand(sia),
                new ActivePollsCommand(sia),
                new PollsVoteCommand(sia),
                new AnnouncePollCommand(sia)
            };
    }

    @Override
    protected void execute(CommandEvent event)
    {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Poll Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class CreatePollCommand extends AbstractModeratorCommand
    {
        private final OrderedMenu.Builder builder;

        public CreatePollCommand(Sia sia)
        {
            super(sia);
            this.name = "create";
            this.aliases = new String[]{"make"};
            this.help = "Creates a new poll: Example: /poll create Best Video Game | 1h | The Division 2 | Halo 2 | Anthem(lol) | WoW";
            this.arguments = "<question> | <duration> | <answer 1> | <answer 2> | <answer 3> | <....>";

            builder = new OrderedMenu.Builder()
                .allowTextInput(true)
                .useNumbers()
//                .useCancelButton(true)
                .setEventWaiter(sia.getEventWaiter());
//                .setTimeout(1, TimeUnit.MINUTES);
        }

        @Override
        protected void execute(CommandEvent event)
        {
            Message m = new MessageBuilder().append("Oh hai").build();
            OrderedMenu menuBuilder = new OrderedMenu.Builder()
                .allowTextInput(true)
                .useNumbers()
                .setEventWaiter(sia.getEventWaiter())
                .setDescription("Testing")
                .setTimeout(1, TimeUnit.MINUTES)
                .addChoices("Choice 1")
                .addChoices("Choice 2")
                .build();
            menuBuilder.display(m);
        }
    }

    public class AnnouncePollCommand extends AbstractModeratorCommand
    {
        public AnnouncePollCommand(Sia sia)
        {
            super(sia);
            this.name = "announce";
            this.aliases = new String[]{"-a", "shout", "notify"};
            this.help = "Announces a poll to a room. Example: /poll announce Who do you want to win? | #General | Come vote all!";
            this.arguments = "<poll name> | <#room> | <message>";
        }

        @Override
        protected void execute(CommandEvent event)
        {

        }
    }

    public class ActivePollsCommand extends AbstractGenericCommand
    {
        public ActivePollsCommand(Sia sia)
        {
            super(sia);
            this.name = "active";
            this.help = "Shows active polls";
        }

        @Override
        protected void execute(CommandEvent event) {

        }
    }

    public class PollsVoteCommand extends AbstractGenericCommand
    {

        public PollsVoteCommand(Sia sia) {
            super(sia);
            this.name = "vote";
            this.help = "Votes on a poll polls";
            this.arguments = "<poll name>";

        }

        @Override
        protected void execute(CommandEvent event) {

        }
    }
}
