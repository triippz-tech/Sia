package com.trievosoftware.discord.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.application.exceptions.NoDiscordUserFoundException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractOwnerCommand;
import com.trievosoftware.discord.utils.ArgsUtil;
import net.dv8tion.jda.core.entities.Member;

public class RemoveBlacklistCommand extends AbstractOwnerCommand {

    public RemoveBlacklistCommand(Sia sia) {
        super(sia);
        this.name = "unblacklist";
        this.help = "Unblacklists a user from using Sia";
        this.arguments = "<@user> <@user> <...>";
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        if ( event.getArgs().isEmpty() )
        {
            event.replyError("Must have one or more users to unblacklist");
            return;
        }

        String args = event.getArgs().trim();

        ArgsUtil.ResolvedArgs resolvedArgs = ArgsUtil.resolve(args, event.getGuild());
        for ( Member member : resolvedArgs.members)
        {
            try {
                sia.getServiceManagers().getDiscordUserService().removeBlacklist(member.getUser());
            } catch (NoDiscordUserFoundException e) {
                event.replyError(e.getMessage());
            }
        }
    }
}
