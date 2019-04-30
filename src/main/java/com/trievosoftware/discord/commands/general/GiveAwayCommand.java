package com.trievosoftware.discord.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import com.trievosoftware.application.domain.DiscordGuild;
import com.trievosoftware.application.domain.GiveAway;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.InvalidTimeUnitException;
import com.trievosoftware.application.exceptions.NonTimeInputException;
import com.trievosoftware.application.exceptions.StringNotIntegerException;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import com.trievosoftware.discord.utils.OtherUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class GiveAwayCommand extends AbstractModeratorCommand
{
    private final String CANCEL_PHRASE =
        "\nYou can type `cancel` at any time to cancel creation.";
    private final String MESSAGE_PHRASE =
        "\n\nPlease enter a message for this Give Away (1500 characters max). Use this to describe the giveaway. The " +
            "message supports [Markdown](https://www.writebots.com/discord-text-formatting/).";
    private final String TIME_PHRASE =
        "\n\nPlease enter how long you want this Give Away to last. You may use seconds(S), minutes (M), hours(H), or days(D)." +
            " Examples are: `1H` `30S` `4D` `120M`";
    private final String CANCEL_COMPLETE =
        "\n\nGiveaway creation has been cancelled.";

    public GiveAwayCommand(Sia sia, Permission... altPerms) {
        super(sia, altPerms);
        this.name = "giveaway";
        this.help = "Guild Giveaway commands";
        this.guildOnly = true;
        this.children = new Command[]
            {
                new CreateGiveAwayCommand(sia)
            };
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild GiveAway Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class CreateGiveAwayCommand extends AbstractModeratorCommand
    {
        CreateGiveAwayCommand(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "create";
            this.help = "Creates a new GiveAway";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            DiscordGuild discordGuild = sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild());
            List<GiveAway> giveAwayList = sia.getServiceManagers().getGiveAwayService()
                .findAllByDiscordGuildAndExpired(discordGuild, false);

            if (giveAwayList.size() > 3 ) // wait for sia pro
            {
                event.replyError("Can not have more than 3 active Give Aways. Please upgrade to Sia Pro for more!");
                return;
            }

            String NAME_PHRASE = "\n\nPlease enter a name for this Give Away (200 Characters Max). An example of a name is: `STEAM KEY GIVEAWAY`";
            Message message = new MessageBuilder()
                .setContent("Let's get started creating your Give Away!"
                    + CANCEL_PHRASE + NAME_PHRASE).build();
            message = event.getTextChannel().sendMessage(message).complete();
            waitForGiveAwayName(event, message);
        }
    }


    // The events waiters for building
    private void waitForGiveAwayName(CommandEvent event, Message oldMessage)
    {
        sia.getEventWaiter().waitForEvent(GuildMessageReceivedEvent.class,
            e -> e.getAuthor().equals(event.getAuthor())
                && e.getChannel().equals(event.getChannel())
                && e.getGuild().getIdLong() == event.getGuild().getIdLong(),
            e -> {
                if(e.getMessage().getContentRaw().equalsIgnoreCase("cancel"))
                {
                    event.replyWarning(CANCEL_COMPLETE);
                }
                else
                {
                    String giveAwayName =
                        e.getMessage().getContentRaw().trim().replaceAll(" ", "_").toUpperCase();
                    if ( giveAwayName.length() > 250 )
                    {
                        event.replyError("A Give Away name must be no more than 200 characters long");
                        waitForGiveAwayName(event, oldMessage);
                    }

                    // delete old message
                    oldMessage.delete().queue();

                    // move to next
                    Message message = new MessageBuilder()
                        .setEmbed(
                            new EmbedBuilder()
                                .setDescription("Good, now:" + MESSAGE_PHRASE + CANCEL_PHRASE)
                                .build()
                        ).build();
                    message = event.getTextChannel().sendMessage(message).complete();
                    waitForGiveAwayMessage(event, message, giveAwayName);
                }
            });
    }

    private void waitForGiveAwayMessage(CommandEvent event, Message oldMessage, String name)
    {
        sia.getEventWaiter().waitForEvent(GuildMessageReceivedEvent.class,
            e -> e.getAuthor().equals(event.getAuthor())
                && e.getChannel().equals(event.getChannel())
                && e.getGuild().getIdLong() == event.getGuild().getIdLong(),
            e -> {
                if(e.getMessage().getContentRaw().equalsIgnoreCase("cancel"))
                {
                    event.replyWarning(CANCEL_COMPLETE);
                }
                else
                {
                    String giveAwayMessage = e.getMessage().getContentRaw().trim();
                    if ( giveAwayMessage.length() > 1500 )
                    {
                        event.replyError("A Give Away Message must be no more than 1500 characters long");
                        waitForGiveAwayMessage(event, oldMessage, name);
                    }

                    // delete old message
                    oldMessage.delete().queue();

                    // move to next
                    Message message = new MessageBuilder()
                        .setContent("Sweet!" + TIME_PHRASE + CANCEL_PHRASE).build();
                    message = event.getTextChannel().sendMessage(message).complete();
                    waitForGiveAwayFinish(event, message, name, giveAwayMessage);
                }
            });
    }

    private void waitForGiveAwayFinish(CommandEvent event, Message oldMessage,
                                       String name, String message)
    {
        sia.getEventWaiter().waitForEvent(GuildMessageReceivedEvent.class,
            e -> e.getAuthor().equals(event.getAuthor())
                && e.getChannel().equals(event.getChannel())
                && e.getGuild().getIdLong() == event.getGuild().getIdLong(),
            e -> {
                if (e.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
                    event.replyWarning(CANCEL_COMPLETE);
                }
                else
                {
                    String timeStr = e.getMessage().getContentRaw().trim().toUpperCase();
                    Instant finish;
                    try {
                        finish = getTime(timeStr);

                        // delete old message
                        oldMessage.delete().queue();

                        // move to next
                        Message newMessage = new MessageBuilder()
                            .setContent("Awesome, almost done!").build();
                        newMessage = event.getTextChannel().sendMessage(newMessage).complete();
                        waitForGiveAwayTextChannel(event, newMessage, name, message, finish);
                    }
                    catch (StringNotIntegerException | InvalidTimeUnitException |  NonTimeInputException ex) {
                        event.replyError(ex.getMessage());
                        waitForGiveAwayFinish(event, oldMessage, name, message);
                    }
                }
            });
    }

    private void waitForGiveAwayTextChannel(CommandEvent event, Message oldMessage,
                                            String name, String message, Instant finish)
    {
        DiscordGuild discordGuild = sia.getServiceManagers().getDiscordGuildService().getDiscordGuild(event.getGuild());
        GiveAway giveAway =
            sia.getServiceManagers().getGiveAwayService().createGiveAway(name, message, finish, discordGuild);

        Message newMessage = FormatUtil.formatGiveAwayMessage(giveAway, event.getGuild(), false);
        newMessage = event.getTextChannel().sendMessage(newMessage).complete();

        // ❌
        String CANCEL = "\u274C";
        String SELECT_CANCEL = "\nYou can select cancel (" + CANCEL + ") to quit at anytime";
        String SELECT_CHANNEL_PHRASE = "\n\nThis is what your giveaway will look like. Please select a text channel for this Give Away to display in. " +
            "We suggest you make a channel dedicated to giveaways or other announcements, so users do not need to search.";
        waitForSelection(event, SELECT_CHANNEL_PHRASE + SELECT_CANCEL, giveAway,
            () -> event.replyWarning(CANCEL_COMPLETE));

        oldMessage.delete().complete();
        newMessage.delete().complete();
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
            throw new StringNotIntegerException(e.getMessage());
        }

        String timeType = time.substring(time.length() - 1);
        switch (timeType)
        {
            case "S":
                return Instant.now().plus(timeLen, ChronoUnit.SECONDS);
            case "M":
                return Instant.now().plus(timeLen, ChronoUnit.MINUTES);
            case "H":
                return Instant.now().plus(timeLen, ChronoUnit.HOURS);
            case "D":
                return Instant.now().plus(timeLen, ChronoUnit.DAYS);
            default:
                throw new InvalidTimeUnitException("`" + timeType + "`: Is an invalid Time Type. " +
                    "Please only use `S (seconds), M (minutes), H (hours), D (days)`");
        }
    }

    private void waitForSelection(CommandEvent event, String message, GiveAway giveAway, Runnable cancel)
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
            Message giveAwayMessage = event.getGuild().getTextChannelById(textChannel.getIdLong())
                .sendMessage(FormatUtil.formatGiveAwayMessage(giveAway, event.getGuild(),true)).complete();
            giveAwayMessage.addReaction(Constants.PARTY_EMOJI).queue();

            giveAway.setMessageId(giveAwayMessage.getIdLong());
            giveAway.setTextChannelId(textChannel.getIdLong());
            sia.getServiceManagers().getGiveAwayService().save(giveAway);

            messageObj.delete().complete();
            event.replySuccess("New Give Away created, users may enter this at  " + textChannel.getAsMention() +
                " and will end at: " + OtherUtil.formatDateTime(event.getGuild(), giveAway.getFinish()));

        });
        builder.build().display(event.getChannel());
    }
}
