package com.trievosoftware.discord.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractOwnerCommand;
import com.trievosoftware.discord.utils.ArgsUtil;
import net.dv8tion.jda.core.entities.Member;

public class BlacklistCommand extends AbstractOwnerCommand
{

    private String DEFAULT_MESSAGE =
        "You have been blacklist from using Sia for the following reason:\n `%s`.\n\nIf you feel like this is a mistake," +
            "please join the support discord channel and make contact with a Mod. " + Constants.SERVER_INVITE;

    public BlacklistCommand(Sia sia) {
        super(sia);
        this.name = "blacklist";
        this.help = "Blacklist's a user from using any of the bots features";
        this.arguments = "<@user> <@user> <...> | <reason>";
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        String[] args = event.getArgs().split("\\|");
        if ( args.length < 2 )
        {
            event.replyError("Must have one or more users and a reason for blacklisting");
            return;
        }
        ArgsUtil.ResolvedArgs resolvedArgs = ArgsUtil.resolve(args[0].trim(), event.getGuild());
        for ( Member member : resolvedArgs.members)
        {
            sia.getServiceManagers().getDiscordUserService().blacklistUser(member.getUser());
            member.getUser().openPrivateChannel().complete().sendMessage(String.format(DEFAULT_MESSAGE, args[1].trim())).complete();
        }
    }
}
