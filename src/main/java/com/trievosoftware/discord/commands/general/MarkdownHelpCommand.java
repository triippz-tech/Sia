package com.trievosoftware.discord.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;

public class MarkdownHelpCommand extends AbstractGenericCommand {

    public MarkdownHelpCommand(Sia sia) {
        super(sia);
        this.name = "markdown";
        this.aliases = new String[]{"formatting"};
    }

    @Override
    public void doCommand(CommandEvent event) {
        event.reply(Constants.MARKDOWN);
    }
}
