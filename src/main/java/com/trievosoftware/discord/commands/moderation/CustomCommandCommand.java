package com.trievosoftware.discord.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.trievosoftware.application.domain.CustomCommand;
import com.trievosoftware.application.domain.GuildSettings;
import com.trievosoftware.application.exceptions.CustomCommandException;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class CustomCommandCommand extends AbstractModeratorCommand {

    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑

    public CustomCommandCommand(Sia sia, Permission... altPerms) {
        super(sia, altPerms);
        this.name = "command";
        this.help = "Custom Commands for Guilds";
        this.arguments = "create|delete|show|change";
        this.guildOnly = true;
        this.children = new AbstractModeratorCommand[]{
            new CreateCustomCommand(sia),
            new DeleteCustomCommand(sia),
            new ChangeCustomCommand(sia),
            new ShowGuildCommands(sia)
        };
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Custom Commands:\n");
        builder.append("To have a custom command you must have a prefix set for the guild. You may utilize Markdown for your command message")
            .append(". If you have questions about Markdown, type `/markdown` for help. It is reccomended to create")
            .append(" your command's message in an editor like Notepad or TextEdit for easier reading.\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class CreateCustomCommand extends AbstractModeratorCommand
    {

        CreateCustomCommand(Sia sia) {
            super(sia, Permission.MANAGE_SERVER);
            this.name = "create";
            this.help = "Creates a new custom command for a server.";
            this.arguments = "<command code> | <@role> <@role> | message";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event) throws CustomCommandException.CommandInvalidParamException, CustomCommandException.CommandExistsException {
            String[] args = event.getArgs().split("\\|");

            if ( args.length < 3 )
            {
                event.replyError("A command must contain: A command code, one or more roles, a message to display when the command is called");
                return;
            }
            GuildSettings guildSettings = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild());

            if ( guildSettings.getPrefix().isEmpty() )
            {
                event.replyError("Your guild must have a custom prefix set to create custom commands. To set a prefix type:" +
                    " `/prefix <prefix>`");
                return;
            }

            String[] rolesQuery = args[1].trim().split(",");
            List<Role> roles = new ArrayList<>();
            for ( String rolQ : rolesQuery )
            {
                List<Role> foundRoles = FinderUtil.findRoles(rolQ.trim(), event.getGuild());
                roles.addAll(foundRoles);
            }

            if(roles.isEmpty()) {
                event.replyError("No roles found called `" + event.getArgs() + "`");
                return;
            }

            CustomCommand customCommand = sia.getServiceManagers().getCustomCommandService()
                .addNewCommand(sia, event.getGuild(), args[0].replaceAll(" ", ""), roles, args[2], guildSettings);

            waitForConfirmation(event,"Please confirm that the command message looks ok", customCommand.getMessage(),
                () -> {
                    event.getMessage().delete().complete();
                    event.replySuccess("New Custom Command created. To use this command type: `"
                        + guildSettings.getPrefix() + args[0].trim() + "`");
                },
                () -> {
                    try {
                        sia.getServiceManagers().getCustomCommandService().removeCommand(event.getGuild(), customCommand.getCommandName());
                        event.getMessage().delete().complete();
                    } catch (CustomCommandException.NoCommandExistsException e) {
                        event.replyError(e.getMessage());
                    }
                });
        }
    }

    public class DeleteCustomCommand extends AbstractModeratorCommand
    {

        DeleteCustomCommand(Sia sia) {
            super(sia, Permission.MANAGE_SERVER);
            this.name = "delete";
            this.help = "Deletes a custom command for a Guild/Server.";
            this.arguments = "<command code>";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event) throws CustomCommandException.NoCommandExistsException {
            if ( event.getArgs().isEmpty() )
            {
                event.replyError("Must annotate the custom command's code to delete a custom command");
                return;
            }
            CustomCommand command = sia.getServiceManagers().getCustomCommandService().getCommand(event.getGuild(), event.getArgs());
            GuildSettings settings = command.getGuildsettings();

            waitForConfirmation(event, "Are you sure you want to delete this command?", command.getMessage(),
                () -> {
                    try {
                        sia.getServiceManagers().getCustomCommandService().removeCommand(event.getGuild(), command.getCommandName());
                        event.replySuccess("Command `" + settings.getPrefix() + command.getCommandName() + "` has been deleted from guild.");
                        event.getMessage().delete().queue();
                    } catch (CustomCommandException.NoCommandExistsException e) {
                        event.replyError(e.getMessage());
                    }
                },
                () -> event.reply("Ok, i'll keep that command"));

        }
    }

    public class ChangeCustomCommand extends AbstractModeratorCommand
    {

        ChangeCustomCommand(Sia sia) {
            super(sia, Permission.MANAGE_SERVER);
            this.name = "change";
            this.help = "Changes a Custom Command";
            this.arguments = "prefix|addroles|removeroles||message";
            this.guildOnly = true;
            this.children = new AbstractModeratorCommand[]{
                new ChangeCommandNameCommand(sia, Permission.MANAGE_SERVER),
                new AddCommandRolesCommand(sia, Permission.MANAGE_SERVER),
                new RemoveCommandRolesCommand(sia, Permission.MANAGE_SERVER),
                new ChangeCommandMessageCommand(sia, Permission.MANAGE_SERVER)
            };
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Custom Commands Changes:\n");
            builder.append("To have a custom command you must have a prefix set for the guild. You may utilize Markdown for your command message")
                .append(". If you have questions about Markdown, type `/markdown` for help. It is reccomended to create")
                .append(" your command's message in an editor like Notepad or TextEdit for easier reading.\n");
            for (Command cmd : this.children)
                builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                    .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
            event.reply(builder.toString());
        }

        public class ChangeCommandNameCommand extends AbstractModeratorCommand
        {

            ChangeCommandNameCommand(Sia sia, Permission... altPerms) {
                super(sia, altPerms);
                this.name = "name";
                this.help = "Changes the Command Name (i.e. `<<command`)";
                this.arguments = "<old command name> | <new command name>";
                this.guildOnly = true;
            }

            @Override
            public void doCommand(CommandEvent event) throws CustomCommandException.NoCommandExistsException {
                String[] args = event.getArgs().trim().split("\\|");

                if ( args.length < 2 )
                {
                    event.replyError("To change a Commands Name (Code), you must enter the Old Command Name and a new Command Name");
                    return;
                }

                CustomCommand command =
                    sia.getServiceManagers().getCustomCommandService().changeCommandName(event.getGuild(), args[0], args[1].replaceAll(" ", ""));
                event.replySuccess("Successfully changed CustomCommand Name from `" + args[0] + "` to `" + command.getCommandName() + "`");
                event.getMessage().delete().queue();

            }
        }

        public class AddCommandRolesCommand extends AbstractModeratorCommand
        {

            AddCommandRolesCommand(Sia sia, Permission... altPerms) {
                super(sia, altPerms);
                this.name = "addrole";
                this.help = "Adds roles to an existing Custom Command";
                this.arguments = "<command name> | <role>";
                this.guildOnly = true;
            }

            @Override
            public void doCommand(CommandEvent event) throws CustomCommandException.NoCommandExistsException {
                String[] args = event.getArgs().trim().split("\\|");
                if ( args.length < 2 )
                {
                    event.replyError("To add a Role to a CustomCommand, you must enter the Command Name and at-least 1 Role");
                    return;
                }

                String[] rolesQuery = args[1].trim().split(",");
                List<Role> roles = new ArrayList<>();
                for ( String rolQ : rolesQuery )
                {
                    List<Role> foundRoles = FinderUtil.findRoles(rolQ.trim(), event.getGuild());
                    roles.addAll(foundRoles);
                }

                if(roles.isEmpty()) {
                    event.replyError("No roles found called `" + event.getArgs() + "`");
                    return;
                }

                CustomCommand command = sia.getServiceManagers().getCustomCommandService()
                    .addCommandRoles(sia, event.getGuild(), args[0], roles);

                StringBuilder builder = new StringBuilder();
                builder.append("The role: \n");
                for ( Role role : roles )
                    builder.append(role.getName()).append("\n");

                builder.append("Was added to: `").append(command.getCommandName()).append("`");
                event.replySuccess(builder.toString());
                event.getMessage().delete().queue();
            }
        }

        public class RemoveCommandRolesCommand extends AbstractModeratorCommand
        {

            RemoveCommandRolesCommand(Sia sia, Permission... altPerms) {
                super(sia, altPerms);
                this.name = "removeroles";
                this.help = "Removes roles from an existing Custom Command";
                this.arguments = "<command name> | <role> <role>";
                this.guildOnly = true;
            }

            @Override
            public void doCommand(CommandEvent event) throws CustomCommandException.NoCommandExistsException
            {
                String[] args = event.getArgs().trim().split("\\|");
                if ( args.length < 2 )
                {
                    event.replyError("To add a Role to a CustomCommand, you must enter the Command Name and at-least 1 Role");
                    return;
                }

                String[] rolesQuery = args[1].trim().split(",");
                List<Role> roles = new ArrayList<>();
                for ( String rolQ : rolesQuery )
                {
                    List<Role> foundRoles = FinderUtil.findRoles(rolQ.trim(), event.getGuild());
                    roles.addAll(foundRoles);
                }

                if(roles.isEmpty()) {
                    event.replyError("No roles found called `" + event.getArgs() + "`");
                    return;
                }

                CustomCommand command = sia.getServiceManagers().getCustomCommandService()
                    .removeCommandRoles(sia, event.getGuild(), args[0], roles);

                StringBuilder builder = new StringBuilder();
                builder.append("The role(s): \n");
                for ( Role role : roles )
                    builder.append("`").append(role.getName()).append("`\n");

                builder.append("Were removed from: `").append(command.getCommandName()).append("`\n")
                    .append("If any Roles are missing, please ensure you spelled them correctly");
                event.replySuccess(builder.toString());
                event.getMessage().delete().queue();
            }
        }

        public class ChangeCommandMessageCommand extends AbstractModeratorCommand
        {

            ChangeCommandMessageCommand(Sia sia, Permission... altPerms) {
                super(sia, altPerms);
                this.name = "message";
                this.help = "Changes the Custom Commands Message";
                this.arguments = "<command name> | <message>";
                this.guildOnly = true;
            }

            @Override
            public void doCommand(CommandEvent event)
                throws CustomCommandException.NoCommandExistsException, CustomCommandException.CommandInvalidParamException
            {
                String[] args = event.getArgs().split("\\|");
                if ( args.length < 2 )
                {
                    event.replyError("To change a CustomCommand message, you must give the CustomCommand Name and the new Message");
                    return;
                }

                CustomCommand oldCommand = sia.getServiceManagers().getCustomCommandService()
                    .getCommand(event.getGuild(), args[0]);

                CustomCommand command = sia.getServiceManagers().getCustomCommandService()
                    .changeCommandMessage(event.getGuild(), args[0], args[1]);

                waitForConfirmation(event,"Please confirm that the command message looks ok", command.getMessage(),
                    () -> {
                        event.getMessage().delete().complete();
                        event.replySuccess("Custom Command message changed. To use this command type: `"
                            + oldCommand.getGuildsettings().getPrefix() + args[0].trim() + "`");
                    },
                    () -> {
                        try {
                            sia.getServiceManagers().getCustomCommandService()
                                .changeCommandMessage(event.getGuild(), command.getCommandName(), oldCommand.getMessage());
                            event.getMessage().delete().complete();
                        } catch (CustomCommandException.NoCommandExistsException | CustomCommandException.CommandInvalidParamException e) {
                            event.replyError(e.getMessage());
                        }
                    });
            }
        }
    }

    public class ShowGuildCommands extends AbstractModeratorCommand // not really a moderator command
    {

        ShowGuildCommands(Sia sia) {
            super(sia, Permission.MESSAGE_WRITE);
            this.name = "show";
            this.help = "Displays all Custom Commands for a Guild";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            List<CustomCommand> customCommands = sia.getServiceManagers().getCustomCommandService().findAllGuildCommands(event.getGuild());
            if ( customCommands.isEmpty() )
            {
                event.replyError("This guild has no Custom Commands");
                return;
            }

            GuildSettings settings = customCommands.get(0).getGuildsettings();
            MessageBuilder builder = new MessageBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            builder.setContent("Custom Commands for **" + event.getGuild().getName() + "**");

            for ( CustomCommand command : customCommands )
            {
                stringBuilder.append(settings.getPrefix()).append(command.getCommandName()).append("\n");
            }

            builder.setEmbed(new EmbedBuilder().setDescription(stringBuilder.toString()).build());
            event.reply(builder.build());
        }
    }

    @SuppressWarnings("Duplicates")
    private void waitForConfirmation(CommandEvent event, String message, String messageBody, Runnable confirm, Runnable cancel)
    {
        new ButtonMenu.Builder()
            .setChoices(CONFIRM, CANCEL)
            .setEventWaiter(sia.getEventWaiter())
            .setTimeout(1, TimeUnit.MINUTES)

            .setText(Constants.WARNING + message + "\n\n" + messageBody + "\n\n" + CONFIRM+" Continue\n"+CANCEL+" Cancel")
            .setFinalAction(m -> m.delete().queue(s->{}, f->{}))
            .setUsers(event.getAuthor())
            .setAction(re ->
            {
                if(re.getName().equals(CONFIRM))
                    confirm.run();
                if(re.getName().equals(CANCEL))
                    cancel.run();
            })
            .build().display(event.getChannel());
    }
}
