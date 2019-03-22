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
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.trievosoftware.application.domain.Poll;
import com.trievosoftware.application.domain.PollItems;
import com.trievosoftware.application.exceptions.*;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.CommandExceptionListener;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class PollCommand extends AbstractModeratorCommand
{
    Logger log = LoggerFactory.getLogger(PollCommand.class);
    private final static String PARTY_EMOJI = "\uD83C\uDF89"; //🎉

    private final static String FORMAT = "Please include a channel and role name, a | as a separator, the poll name (spelled exactly as saved)," +
        "another | as a separator,  and a message to send!\n"
        + "Please see the full command reference for examples - <"+ Constants.Wiki.COMMANDS+"#-general-commands>";

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
                new AnnouncePollCommand(sia),
                new VoteCommand(sia)
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
//        private final OrderedMenu.Builder builder;
        private Integer amountOfTime;
        private TimeUnit timeUnits;

        public CreatePollCommand(Sia sia)
        {
            super(sia);
            this.name = "create";
            this.aliases = new String[]{"make"};
            this.help = "Creates a new poll: Example: /poll create Best Video Game | 1h | The Division 2 | Halo 2 | Anthem(lol) | WoW";
            this.arguments = "<question> | <duration> | <answer 1> | <answer 2> | <answer 3> | <....>";
        }

        @Override
        protected void execute(CommandEvent event)
        {
            String[] parts = event.getArgs().split("\\|");
            if ( parts.length <= 3 )
            {
                event.replyError("A poll must have a `Question`, `Duration`, and at least `2 Answers` ");
                return;
            }
            String question = parts[0].trim().replaceAll(" ", "_").toUpperCase();
            Instant duration = null;
            try {
                duration = getTime(parts[1].trim().toUpperCase());
            } catch (StringNotIntegerException | InvalidTimeUnitException | NonTimeInputException e) {
                event.replyError(e.getMessage());
                return;
            }
            List<String> answers = new ArrayList<>();
            for ( int i = 2 ; i < parts.length ; i++ )
            {
                answers.add(parts[i].trim().replaceAll(" ", "_").toUpperCase());
            }
            Poll poll;
            try {
                poll = createNewPoll(event, question, duration, answers);
            } catch (NoPollsFoundException e) {
                log.error("{}", e.getMessage());
                event.replyError("I was unable to create your Poll. Please ensure you executed the command properly and please try again");
                return;
            } catch (PollExpiredException e) {
                event.replyError(e.getMessage());
                return;
            }
            Message message = sia.getServiceManagers().getPollService().buildPollMessage(poll, event.getTextChannel(), sia);
            message.pin().complete();
        }

        private Instant getTime(String time)
            throws StringNotIntegerException, InvalidTimeUnitException, NonTimeInputException
        {
            int timeLen;
            try {
                timeLen = FormatUtil.getMinutes(time);
                if ( timeLen <= 0 )
                    throw new NonTimeInputException("Time may not be `0` or a `Negative` number");
            } catch (StringNotIntegerException e) {
                log.error("{}", e.getMessage());
                throw new StringNotIntegerException(e.getMessage());
            }

            String timeType = time.substring(time.length() - 1);
            switch (timeType)
            {
                case "S":
                    amountOfTime = timeLen;
                    timeUnits = TimeUnit.SECONDS;
                    return Instant.now().plus(timeLen, ChronoUnit.SECONDS);
                case "M":
                    amountOfTime = timeLen;
                    timeUnits = TimeUnit.MINUTES;
                    return Instant.now().plus(timeLen, ChronoUnit.MINUTES);
                case "H":
                    amountOfTime = timeLen;
                    timeUnits = TimeUnit.HOURS;
                    return Instant.now().plus(timeLen, ChronoUnit.HOURS);
                case "D":
                    amountOfTime = timeLen;
                    timeUnits = TimeUnit.DAYS;
                    return Instant.now().plus(timeLen, ChronoUnit.DAYS);
                default:
                    throw new InvalidTimeUnitException("`" + timeType + "`: Is an invalid Time Type. " +
                        "Please only use `S (seconds), M (minutes), H (hours), D (days)`");
            }
        }

        private Poll createNewPoll(CommandEvent event, String question, Instant finishTime, List<String> answers) throws NoPollsFoundException, PollExpiredException {
            Poll poll = sia.getServiceManagers().getPollService()
                .createPoll(event.getGuild().getIdLong(), event.getAuthor().getIdLong(), question, finishTime);

            for ( String answer : answers )
            {
                PollItems pollItem = sia.getServiceManagers().getPollItemsService().createPollItem(answer, poll);
                try {
                    sia.getServiceManagers().getPollService().addItemToPoll(poll.getId(), pollItem);
                } catch (NoPollsFoundException e) {
                    //delete the created poll
                    sia.getServiceManagers().getPollService().delete(poll.getId());
                    throw new NoPollsFoundException(e.getMessage());
                }
            }
            return sia.getServiceManagers().getPollService().getPoll(poll.getId());
        }
    }

    public class AnnouncePollCommand extends AbstractModeratorCommand
    {
        public AnnouncePollCommand(Sia sia)
        {
            super(sia);
            this.name = "announce";
            this.aliases = new String[]{"-a", "shout", "notify"};
            this.help = "Announces a poll to a room. Example: /poll announce #general @everyone | Who do you want to win? | Come vote all!";
            this.arguments = "<#channel> | <rolename> | <poll name> | <message>";
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
                throw new CommandExceptionListener.CommandErrorException(FORMAT);

            String[] parts = event.getArgs().split("\\|");
            if ( parts.length < 4 )
            {
                event.replyError("Invalid number of parameters. Please ensure you give the Poll Name and your Message");
                return;
            }

            String pollTitle = parts[2].trim().replaceAll(" ", "_").toUpperCase();
            String[] room = event.getArgs().split("\\s+", 2);
            if ( !sia.getServiceManagers().getPollService().activePollExists(event.getGuild(), pollTitle ) )
            {
                event.replyError("There is not active Poll with that name. Please ensure you spelled it exactly as saved");
                return;
            }

            List<TextChannel> list = FinderUtil.findTextChannels(room[0], event.getGuild());
            if(list.isEmpty())
                throw new CommandExceptionListener.CommandErrorException("I couldn't find any text channel called `"+parts[0]+"`.");
            if(list.size()>1)
                throw new CommandExceptionListener.CommandWarningException(FormatUtil.listOfText(list, parts[0]));

            TextChannel tc = list.get(0);
            if(!tc.canTalk())
                throw new CommandExceptionListener.CommandWarningException("I do not have permission to Send Messages in "+tc.getAsMention()+"!");
            if(!tc.canTalk(event.getMember()))
                throw new CommandExceptionListener.CommandErrorException("You do not have permission to Send Messages in "+tc.getAsMention()+"!");
            if(parts.length<3)
                throw new CommandExceptionListener.CommandErrorException(FORMAT);

            String parts2 = parts[1].trim().replaceAll("@", "");
            List<Role> rlist = FinderUtil.findRoles(parts2, event.getGuild());

            if(rlist.isEmpty())
                throw new CommandExceptionListener.CommandErrorException("I couldn't find any role called `"+parts2+"`.");
            if(rlist.size()>1)
                throw new CommandExceptionListener.CommandWarningException(FormatUtil.listOfRoles(rlist, parts2));

            Role role = rlist.get(0);
            if(!event.getSelfMember().canInteract(role))
                throw new CommandExceptionListener.CommandWarningException("I cannot modify the role `"+role.getName()+"`. Try moving my highest role above the `"+role.getName()+"` role.");
            if(!event.getMember().canInteract(role))
                throw new CommandExceptionListener.CommandErrorException("You cannot modify the `"+role.getName()+"` role.");

            String message = PARTY_EMOJI + " " + role.getAsMention()+" "+ "There is an active Poll! `"
                + pollTitle + "`: " + parts[3] + " " + PARTY_EMOJI + "\n\n" +
                "To vote type `/poll vote add " + pollTitle + " | <choice number>`";

            if(!event.getMember().hasPermission(tc, Permission.MESSAGE_MENTION_EVERYONE))
                message = FormatUtil.filterEveryone(message);
            if(message.length() > 2000)
                message = message.substring(0, 2000);
            String fmessage = message;
            if(!role.isMentionable())
            {
                String reason = "Poll Announcement by "+event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator();
                role.getManager().setMentionable(true).reason(reason).queue(s ->
                {
                    tc.sendMessage(fmessage).queue(m ->
                    {
                        event.replySuccess("Poll Announcement for `"+role.getName()+"` sent to "+tc.getAsMention()+"!");
                        role.getManager().setMentionable(false).reason(reason).queue(s2->{}, f2->{});
                    }, f ->
                    {
                        event.replyError("Failed to send message.");
                        role.getManager().setMentionable(false).reason(reason).queue(s2->{}, f2->{});
                    });
                }, f -> event.replyError("Failed to modify the role `"+role.getName()+"`."));
            }
            else
            {
                tc.sendMessage(fmessage).queue(
                    m -> event.replySuccess("Poll Announcement for `"+role.getName()+"` sent to "+tc.getAsMention()+"!"),
                    f -> event.replyError("Failed to send message."));
            }
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
        protected void execute(CommandEvent event)
        {
            Message activePolls = sia.getServiceManagers().getPollService().getActivePollsForGuild(event.getGuild());
            event.reply(activePolls);
        }
    }

    public class VoteCommand extends AbstractGenericCommand
    {

        public VoteCommand(Sia sia) {
            super(sia);
            this.name = "vote";
            this.help = "Displays options for voting";
            this.arguments = "add|remove|change";
            this.children = new AbstractGenericCommand[]{
                new AddVoteCommand(sia),
                new RemoveVoteCommand(sia),
                new ChangeVoteCommand(sia)
            };
        }

        @Override
        protected void execute(CommandEvent event) {
            StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Poll Voting Commands:\n");
            for (Command cmd : this.children)
                builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                    .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
            event.reply(builder.toString());
        }

        private class AddVoteCommand extends AbstractGenericCommand {
            public AddVoteCommand(Sia sia) {
                super(sia);
                this.name = "add";
                this.help = "adds a vote to a poll";
                this.arguments = "<poll_name> | <item number>";
            }

            @Override
            protected void execute(CommandEvent event)
            {
                if ( event.getArgs().isEmpty() )
                {
                    event.replyError("Must include a Poll name and your item choice");
                    return;
                }

                String[] parts = event.getArgs().split("\\|");

                if ( parts.length < 2 )
                {
                    event.replyError("Must include a Poll name and your item choice");
                    return;
                }

                int choice;
                try {
                    choice = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e)
                {
                    event.replyError("Choice must be a numeric value");
                    return;
                }

                String pollName = parts[0].trim().replaceAll(" ", "_").toUpperCase();
                Poll poll;
                try {
                    poll = sia.getServiceManagers().getPollService().getPoll(event.getGuild(), pollName);
                } catch (NoPollsFoundException | PollExpiredException e) {
                    event.replyError(e.getMessage());
                    return;
                }

                if ( choice > poll.getPollitems().size() )
                {
                    event.replyError("You may only choose 1-" + poll.getPollitems().size());
                    return;
                }
                 // check if user has voted already


                String reaction = Constants.NUMBERS[choice-1];
                for ( PollItems item : poll.getPollitems() )
                {
                    if ( item.getReaction().equals(reaction))
                    {
                        sia.getServiceManagers().getPollItemsService().addVote(item.getId());
                        event.getMessage().delete().complete();
                        event.replySuccess("Vote casted!");
                        return;
                    }
                }
            }
        }

        private class RemoveVoteCommand extends AbstractGenericCommand {
            public RemoveVoteCommand(Sia sia) {
                super(sia);
                this.name = "remove";
                this.help = "removes a users vote from a poll";
                this.arguments = "<item number>";
            }

            @Override
            protected void execute(CommandEvent event)
            {
                if ( event.getArgs().isEmpty() )
                {
                    event.replyError("Must include a Poll name and your item choice");
                    return;
                }

                String[] parts = event.getArgs().split("\\|");

                if ( parts.length < 2 )
                {
                    event.replyError("Must include a Poll name and your item choice");
                    return;
                }

                int choice;
                try {
                    choice = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e)
                {
                    event.replyError("Choice must be a numeric value");
                    return;
                }
                String pollName = parts[0].trim().replaceAll(" ", "_").toUpperCase();
                Poll poll;
                try {
                    poll = sia.getServiceManagers().getPollService().getPoll(event.getGuild(), pollName);
                } catch (NoPollsFoundException | PollExpiredException e) {
                    event.replyError(e.getMessage());
                    return;
                }

                if ( choice > poll.getPollitems().size() )
                {
                    event.replyError("You may only choose 1-" + poll.getPollitems().size());
                    return;
                }
                // check if user has voted already


                String reaction = Constants.NUMBERS[choice-1];
                for ( PollItems item : poll.getPollitems() )
                {
                    if ( item.getReaction().equals(reaction))
                    {
                        sia.getServiceManagers().getPollItemsService().removeVote(item.getId());
                        event.getMessage().delete().complete();
                        event.replySuccess("Vote Removed!");
                        return;
                    }
                }
            }
        }

        private class ChangeVoteCommand extends AbstractGenericCommand {
            public ChangeVoteCommand(Sia sia) {
                super(sia);
                this.name = "change";
                this.help = "Changes a users vote to a new item";
                this.arguments = "<poll name> | <old choice>, <new choice>";
            }

            @Override
            protected void execute(CommandEvent event)
            {
                if ( event.getArgs().isEmpty() )
                {
                    event.replyError("Must include a Poll name, your old choice, and your new choice");
                    return;
                }

                String[] parts = event.getArgs().split("\\|");
                if ( parts.length < 2 )
                {
                    event.replyError("Must include a Poll name, your old choice, and your new choice");
                    return;
                }

                String[] itemParts = parts[1].split(",");
                if ( itemParts.length < 2 )
                {
                    event.replyError("Must include your old choice, and your new choice");
                    return;
                }

                String pollName = parts[0].trim().replaceAll(" ", "_").toUpperCase();
                String newChoiceStr = itemParts[1].trim();
                String oldChoiceStr = itemParts[0].trim();
                int oldChoice;
                int newChoice;
                try {
                    oldChoice = Integer.parseInt(oldChoiceStr);
                    newChoice = Integer.parseInt(newChoiceStr);
                } catch (NumberFormatException e)
                {
                    event.replyError("Choice must be a numeric value");
                    return;
                }

                Poll poll;
                try {
                    poll = sia.getServiceManagers().getPollService().getPoll(event.getGuild(), pollName);
                } catch (NoPollsFoundException | PollExpiredException e) {
                    event.replyError(e.getMessage());
                    return;
                }

                if ( oldChoice < 1 || oldChoice > 10 )
                {
                    event.replyError("You may only choose 1-" + poll.getPollitems().size());
                    return;
                }
                if ( newChoice < 1 || newChoice > poll.getPollitems().size() )
                {
                    event.replyError("You may only choose 1-" + poll.getPollitems().size());
                    return;
                }
                // check if user has voted already


                String oldReaction = Constants.NUMBERS[oldChoice - 1];
                String newReaction = Constants.NUMBERS[newChoice - 1];
                for ( PollItems item : poll.getPollitems() )
                {
                    if ( item.getReaction().equals(oldReaction))
                        sia.getServiceManagers().getPollItemsService().removeVote(item.getId());
                    else if ( item.getReaction().equals(newReaction))
                      sia.getServiceManagers().getPollItemsService().addVote(item.getId());

                }
                event.getMessage().delete().complete();
                event.replySuccess("Vote has been changed!");
            }
        }
    }

}
