package com.trievosoftware.discord.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.domain.WelcomeMessage;
import com.trievosoftware.application.exceptions.IncorrectWelcomeMessageParamsException;
import com.trievosoftware.application.exceptions.NoActiveWelcomeMessage;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class WelcomeMessageCommand extends AbstractModeratorCommand
{
    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑

    public WelcomeMessageCommand(Sia sia) {
        super(sia, Permission.ADMINISTRATOR);
        this.name = "welcomemessage";
        this.aliases = new String[]{"greeting", "/wm"};
        this.help = "Welcome Message Settings";
        this.arguments = "create|delete|change";
        this.guildOnly = true;
        this.children = new AbstractModeratorCommand[]{
            new CreateWelcomeMessage(sia),
            new DeleteWelcomeMessage(sia),
            new ChangeWelcomeMessage(sia),
            new SwitchWelcomeMessage(sia),
            new DisplayWelcomeMessages(sia),
            new DisplayActiveWelcome(sia)
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

    public class CreateWelcomeMessage extends AbstractModeratorCommand
    {

        CreateWelcomeMessage(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "create";
            this.arguments = "<name> | <message title> | <message body> | <message footer> | [website url] | [logo url] | <active (YES/NO)>";
            this.aliases = new String[]{"new"};
            this.help = "Creates a new welcome message for new users";
        }

        @Override
        public void doCommand(CommandEvent event) throws IncorrectWelcomeMessageParamsException
        {
            List<WelcomeMessage> welcomeMessages =
                sia.getServiceManagers().getWelcomeMessageService().findAllByGuildsettings(
                    sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild())
                );
            // limit them to 4 per for now
            if ( welcomeMessages.size() > 4 )
            {
                event.reply("You may not have more than 4 saved Welcome Messages");
                return;
            }


            WelcomeMessage welcomeMessage;

            String[] args = event.getArgs().split("\\|");
            String name         =   args[0].trim().replaceAll(" ", "_").toUpperCase();
            String title        =   args[1].trim();
            String body         =   args[2].trim();
            String footer       =   args[3].trim();
            String url          =   args[4].trim();
            String logo         =   args[5].trim();
            String activeStr    =   args[6].trim().toUpperCase();

            GuildSettings guildSettings = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild());
            welcomeMessage = sia.getServiceManagers().getWelcomeMessageService()
                    .generateWelcomeMessage(event, sia, name, title, body, footer, url, logo, activeStr, guildSettings);

            event.reply(welcomeMessage.getWelcomeMessage(event.getGuild(), event.getAuthor()));
            waitForConfirmation(event, "Please confirm that the message looks ok", () -> {
                sia.getServiceManagers().getWelcomeMessageService().createWelcomeMessage(welcomeMessage);
                event.getMessage().delete().complete();
                event.replySuccess("New Welcome Message created" + (welcomeMessage.isActive() ? "" : " and is set as active"));
            });
        }
    }

    public class DeleteWelcomeMessage extends AbstractModeratorCommand
    {

        DeleteWelcomeMessage(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "delete";
            this.aliases = new String[]{"remove"};
            this.help = "Deletes a saved welcome message";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            List<WelcomeMessage> welcomeMessages =
                sia.getServiceManagers().getWelcomeMessageService().findAllByGuildsettings(
                    sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild())
                );

            if ( welcomeMessages.isEmpty() )
            {
                event.replyError("This Guild has no saved Welcome Messages");
                return;
            }

            Message allMsgMessage = new MessageBuilder()
                .setContent("Here are all saved Welcome Messages for **" + event.getGuild().getName() + "**").build();
            event.getTextChannel().sendMessage(allMsgMessage)
                .queue( sentMsg -> sentMsg.delete().queueAfter(30, TimeUnit.SECONDS));

            List<Message> messages = new ArrayList<>();
            for ( WelcomeMessage message : welcomeMessages )
                messages.add(message.getWelcomeMessage(event.getGuild(), event.getAuthor()));
            messages.forEach( message -> event.getTextChannel()
                .sendMessage(message).queue( sentMsg -> sentMsg.delete().queueAfter(30, TimeUnit.SECONDS)));

            TreeMap<Integer, WelcomeMessage> welcomeMessageMap = new TreeMap<>();
            for ( int i = 0; i < welcomeMessages.size(); i++ )
                welcomeMessageMap.put(i+1, welcomeMessages.get(i));

            OrderedMenu.Builder builder = new OrderedMenu.Builder()
                .setText("Please choose a greeting to delete")
                .useNumbers()
                .useCancelButton(true)
                .setUsers(event.getAuthor())
                .setEventWaiter(sia.getEventWaiter())
                .setSelection((msg, i) ->
                {
                    welcomeMessageMap.forEach( (k,v) -> {
                        if ( k.intValue() == i)
                            sia.getServiceManagers().getWelcomeMessageService().removeWelcomeMessage(v);
                    });
                    event.reply("Successfully removed Welcome Message: `" + welcomeMessageMap.get(i).getName() + "` from saved.");

                })
                .setCancel( (msg) -> {});
                welcomeMessageMap.forEach( (k,v) -> builder.addChoice(v.getName()));

            builder.build().display(event.getTextChannel());
        }
    }

    public class ChangeWelcomeMessage extends AbstractModeratorCommand
    {

        ChangeWelcomeMessage(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "change";
            this.aliases = new String[]{"modify"};
            this.arguments = "<message name>";
            this.help = "Changes a Guild's Welcome Message";
        }

        @Override
        public void doCommand(CommandEvent event) throws IncorrectWelcomeMessageParamsException, NoActiveWelcomeMessage {
            List<WelcomeMessage> welcomeMessages =
                sia.getServiceManagers().getWelcomeMessageService().findAllByGuildsettings(
                    sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild())
                );

            if ( welcomeMessages.isEmpty() )
            {
                event.replyError("This Guild has no saved Welcome Messages");
                return;
            }

            WelcomeMessage welcomeMessage;

            String[] args = event.getArgs().split("\\|");
            String name         =   args[0].trim().replaceAll(" ", "_").toUpperCase();
            String title        =   args[1].trim();
            String body         =   args[2].trim();
            String footer       =   args[3].trim();
            String url          =   args[4].trim();
            String logo         =   args[5].trim();
            String activeStr    =   args[6].trim().toUpperCase();

            GuildSettings guildSettings = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild());
            welcomeMessage = sia.getServiceManagers().getWelcomeMessageService()
                .modifyWelcomeMessage(event, sia, name, title, body, footer, url, logo, activeStr, guildSettings);

            event.reply(welcomeMessage.getWelcomeMessage(event.getGuild(), event.getAuthor()));
            waitForConfirmation(event, "Please confirm that the modified message looks ok", () -> {
                sia.getServiceManagers().getWelcomeMessageService().createWelcomeMessage(welcomeMessage);
                event.getMessage().delete().complete();
                event.replySuccess("Modified Welcome Message " + (welcomeMessage.isActive() ? "" : " and is set as active"));
            });
        }
    }

    public class SwitchWelcomeMessage extends AbstractModeratorCommand
    {

        SwitchWelcomeMessage(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "switch";
            this.help = "Switches between welcome messages";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            List<WelcomeMessage> welcomeMessages =
                sia.getServiceManagers().getWelcomeMessageService().findAllByGuildsettings(
                    sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild())
                );

            if ( welcomeMessages.isEmpty() )
            {
                event.replyError("This Guild has no saved Welcome Messages");
                return;
            }

            Message allMsgMessage = new MessageBuilder()
                .setContent("Here are all saved Welcome Messages for **" + event.getGuild().getName() + "**").build();
            event.getTextChannel().sendMessage(allMsgMessage)
                .queue( sentMsg -> sentMsg.delete().queueAfter(30, TimeUnit.SECONDS));

            List<Message> messages = new ArrayList<>();
            for ( WelcomeMessage message : welcomeMessages )
                messages.add(message.getWelcomeMessage(event.getGuild(), event.getAuthor()));
            messages.forEach( message -> event.getTextChannel()
                .sendMessage(message).queue( sentMsg -> sentMsg.delete().queueAfter(30, TimeUnit.SECONDS)));

            TreeMap<Integer, WelcomeMessage> welcomeMessageMap = new TreeMap<>();
            for ( int i = 0; i < welcomeMessages.size(); i++ )
                welcomeMessageMap.put(i+1, welcomeMessages.get(i));

            OrderedMenu.Builder builder = new OrderedMenu.Builder()
                .setText("Please select the greeting to make active for this server")
                .useNumbers()
                .useCancelButton(true)
                .setUsers(event.getAuthor())
                .setEventWaiter(sia.getEventWaiter())
                .setSelection((msg, i) ->
                {
                    welcomeMessageMap.forEach( (k,v) -> {
                        if ( k.intValue() == i)
                            sia.getServiceManagers().getWelcomeMessageService().setActive(v);
                    });
                    event.reply("Successfully change active greeting to: `" + welcomeMessageMap.get(i).getName() + "`.");

                })
                .setCancel( (msg) -> {});
            welcomeMessageMap.forEach( (k,v) -> builder.addChoice(v.getName()));

            builder.build().display(event.getTextChannel());
        }
    }

    public class DisplayWelcomeMessages extends AbstractModeratorCommand
    {
        DisplayWelcomeMessages(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "display";
            this.help = "Displays saved welcome messages for a Guild/Server";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            List<WelcomeMessage> welcomeMessages =
                sia.getServiceManagers().getWelcomeMessageService().findAllByGuildsettings(
                    sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild())
                );

            if ( welcomeMessages.isEmpty() )
            {
                event.replyError("This Guild has no saved Welcome Messages");
                return;
            }

            Message allMsgMessage = new MessageBuilder()
                .setContent("Here are all saved Welcome Messages for **" + event.getGuild().getName() + "**" +
                    "\nThese messages will automatically disappear in 5 minutes ").build();
            event.getTextChannel().sendMessage(allMsgMessage)
                .queue( sentMsg -> sentMsg.delete().queueAfter(5, TimeUnit.MINUTES));

            List<Message> messages = new ArrayList<>();
            for ( WelcomeMessage message : welcomeMessages )
                messages.add(message.getWelcomeMessage(event.getGuild(), event.getAuthor()));
            messages.forEach( message -> event.getTextChannel()
                .sendMessage(message).queue( sentMsg -> sentMsg.delete().queueAfter(5, TimeUnit.MINUTES)));
        }
    }

    private class DisplayActiveWelcome extends AbstractModeratorCommand
    {

        DisplayActiveWelcome(Sia sia, Permission... altPerms) {
            super(sia, altPerms);
            this.name = "active";
            this.help = "Displays the active Welcome Greeting";
        }

        @Override
        public void doCommand(CommandEvent event) throws NoActiveWelcomeMessage {
            GuildSettings guildSettings = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild());
            WelcomeMessage welcomeMessage = sia.getServiceManagers().getWelcomeMessageService().getActiveWelcome(guildSettings);

            event.reply(welcomeMessage.getWelcomeMessage(event.getGuild(), event.getAuthor()));
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

}
