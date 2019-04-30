package com.trievosoftware.discord.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import com.trievosoftware.application.domain.GuildEvent;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.*;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.OtherUtil;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class GuildEventCommand extends AbstractModeratorCommand
{
    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑
    private final String CREATE_HELP =
        "<event name> | <message> | [message image url] | <text channel> | <time until start>\n\n" +
            "Raid Temple | We will be raiding the Temple on US-EAST server at 7PM Saturday | www.example.com/example.jpg | 5H";

    public GuildEventCommand(Sia sia, Permission... altPerms) {
        super(sia);
        this.name = "event";
        this.aliases = new String[]{"guildevent"};
        this.help = "Guild Event Commands";
        this.arguments = "create|delete|change";
        this.guildOnly = true;
        this.children = new Command[]{
            new CreateGuildEventCommand(sia, Permission.MANAGE_SERVER),
            new DeleteGuildEventCommand(sia, Permission.MANAGE_SERVER),
            new ShowGuildEventsCommand(sia)
        };
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Poll Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class CreateGuildEventCommand extends AbstractModeratorCommand
    {

        CreateGuildEventCommand(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "create";
            this.aliases = new String[]{"make", "new"};
            this.help = "Creates a new Guild Event";
            this.arguments = CREATE_HELP;
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event) throws NonTimeInputException, InvalidTimeUnitException,
            GuildEventException.IncorrectGuildEventParamsException, StringNotIntegerException
        {
            GuildSettings guildSettings =
                sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild()).getGuildSettings();
            List<GuildEvent> guildEventList =
                sia.getServiceManagers().getGuildEventService().findAllByGuildSettingsAndExpired(guildSettings, false);

            if ( guildEventList.size() > 4 )
            {
                event.reply("You may not have more than 4 active Guild Events. To have more, consider upgrading your Sia plan");
                return;
            }

            String[] args = event.getArgs().split("\\|");
            if (args.length != 4)
            {
                event.replyError("A Guild Event must contain: " + CREATE_HELP);
                return;
            }

            String eventName        = args[0].trim().replaceAll(" ", "_").toUpperCase();
            String eventMessage     = args[1].trim();
            String eventImageUrl    = args[2].trim();
            String timeStr          = args[3].trim().replaceAll(" ", "").toUpperCase();

            GuildEvent guildEvent = sia.getServiceManagers().getGuildEventService()
                .generateGuildEvent(guildSettings, eventName, eventMessage, eventImageUrl, timeStr);

            event.reply(guildEvent.getGuildEventMessage(event.getGuild(), true));
            waitForConfirmation(event, "Please confirm that the message looks ok", () -> {
                waitForSelection(event, "What text channel would you like to announce this in?", guildEvent,
                    () -> {
                        event.getMessage().delete().complete();
                        event.replyWarning("Guild Event discarded.");
                    });
            });
        }
    }

    public class DeleteGuildEventCommand extends AbstractModeratorCommand
    {

        DeleteGuildEventCommand(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "delete";
            this.aliases = new String[]{"remove"};
            this.help = "Deletes Guild Event";
//            this.arguments = "<";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            GuildSettings guildSettings =
                sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild()).getGuildSettings();
            List<GuildEvent> guildEventList =
                sia.getServiceManagers().getGuildEventService().findAllByGuildSettingsAndExpired(guildSettings, false);

            if ( guildEventList.size() == 0 )
            {
                event.reply("This Guild/Server does not have any active Guild Events");
                return;
            }

            Message allMsgMessage = new MessageBuilder()
                .setContent("Here are all saved Welcome Messages for **" + event.getGuild().getName() + "**").build();
            event.getTextChannel().sendMessage(allMsgMessage)
                .queue( sentMsg -> sentMsg.delete().queueAfter(30, TimeUnit.SECONDS));

            List<Message> messages = new ArrayList<>();
            for ( GuildEvent message : guildEventList )
                messages.add(message.getGuildEventMessage(event.getGuild(), true));
            messages.forEach( message -> event.getTextChannel()
                .sendMessage(message).queue( sentMsg -> sentMsg.delete().queueAfter(30, TimeUnit.SECONDS)));

            TreeMap<Integer, GuildEvent> guildEventMap = new TreeMap<>();
            for ( int i = 0; i < guildEventList.size(); i++ )
                guildEventMap.put(i+1, guildEventList.get(i));

            OrderedMenu.Builder builder = new OrderedMenu.Builder()
                .setText("Please choose an event to delete")
                .useNumbers()
                .useCancelButton(true)
                .setUsers(event.getAuthor())
                .setEventWaiter(sia.getEventWaiter())
                .setSelection((msg, i) ->
                {
                    guildEventMap.forEach( (k,v) -> {
                        if ( k.intValue() == i)
                            sia.getServiceManagers().getGuildEventService().delete(v.getId());
                    });
                    event.reply("Successfully removed Guild Event: `" + guildEventMap.get(i).getEventName() + "` from saved.");

                })
                .setCancel( (msg) -> {});
            guildEventMap.forEach( (k,v) -> builder.addChoice(v.getEventName()));

            builder.build().display(event.getTextChannel());
        }
    }

    public class ShowGuildEventsCommand extends AbstractGenericCommand
    {

        ShowGuildEventsCommand(Sia sia) {
            super(sia);
            this.name = "display";
            this.aliases = new String[] {"show"};
            this.help = "Displays saved Guild Events for a Guild/Server";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            GuildSettings guildSettings =
                sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild()).getGuildSettings();
            List<GuildEvent> guildEventList =
                sia.getServiceManagers().getGuildEventService().findAllByGuildSettingsAndExpired(guildSettings, false);

            if ( guildEventList.isEmpty() )
            {
                event.replyError("This Guild has no active Guild Events");
                return;
            }

            Message allMsgMessage = new MessageBuilder()
                .setContent("Here are all saved Guild Events for **" + event.getGuild().getName() + "**" +
                    "\nThese messages will automatically disappear in 5 minutes ").build();
            event.getTextChannel().sendMessage(allMsgMessage)
                .queue( sentMsg -> sentMsg.delete().queueAfter(5, TimeUnit.MINUTES));

            List<Message> messages = new ArrayList<>();
            for ( GuildEvent guildEvent : guildEventList )
                messages.add(guildEvent.getGuildEventMessage(event.getGuild(), true));
            messages.forEach( message -> event.getTextChannel()
                .sendMessage(message).queue( sentMsg -> sentMsg.delete().queueAfter(5, TimeUnit.MINUTES)));
        }
    }

    @SuppressWarnings("Duplicates")
    private void waitForConfirmation(CommandEvent event, String message,  Runnable confirm)
    {
        new ButtonMenu.Builder()
            .setChoices(CONFIRM, CANCEL)
            .setEventWaiter(sia.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES)

            .setText(Constants.WARNING + message + "\n\n"+CONFIRM+" Continue\n"+CANCEL+" Cancel")
            .setFinalAction(m -> m.delete().queue(s->{}, f->{}))
            .setUsers(event.getAuthor())
            .setAction(re ->
            {
                if(re.getName().equals(CONFIRM))
                    confirm.run();
            }).build().display(event.getChannel());
    }

    private void waitForSelection(CommandEvent event, String message, GuildEvent guildEvent, Runnable cancel)
    {
        SelectionDialog.Builder builder = new SelectionDialog.Builder()
            .setEventWaiter(sia.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES)
            .setUsers(event.getAuthor())
            .useLooping(true)
            .setSelectedEnds("➡️", "⬅️")
            .setText(Constants.WARNING + message + "\n\n Please use the arrow keys to scroll to your selection")
            .setCanceled( re -> {
                re.delete().complete();
                cancel.run();
            } );

        Map<String, Long> channels = new HashMap<>();
        for (TextChannel channel : event.getGuild().getTextChannels())
            channels.put(channel.getAsMention(), channel.getIdLong());

        List<String> channelsAsMention = new ArrayList<>();
        channels.forEach( (k,v) -> channelsAsMention.add(k));
        String[] choices = channelsAsMention.toArray(new String[0]);

        builder.setChoices(choices);
        builder.setSelectionConsumer( (messageObj, selection) ->
        {
            String channelId = choices[selection-1]
                .replaceAll("<", "")
                .replaceAll("#", "")
                .replaceAll(">","");
            TextChannel textChannel = event.getGuild().getTextChannelById(channelId);
            guildEvent.setTextChannelId(textChannel.getIdLong());
            sia.getServiceManagers().getGuildEventService().save(guildEvent);

            event.getMessage().delete().complete();
            messageObj.delete().complete();
            event.replySuccess("New Guild Event created and will send a notification to " + textChannel.getAsMention() +
                " at: " + OtherUtil.formatDateTime(event.getGuild(), guildEvent.getEventStart()));
        });
        builder.build().display(event.getChannel());
    }
}
