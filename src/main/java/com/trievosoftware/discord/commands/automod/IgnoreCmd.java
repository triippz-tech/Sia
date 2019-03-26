/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trievosoftware.discord.commands.automod;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
public class IgnoreCmd extends AbstractModeratorCommand
{
    public IgnoreCmd(Sia sia)
    {
        super(sia);
        this.guildOnly = true;
        this.name = "ignore";
        this.aliases = new String[]{"addignore","ignored","ignores"};
        this.category = new Category("AutoMod");
        this.arguments = "<role | channel>";
        this.help = "shows ignores, or sets automod to ignore a role or channel";
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void doCommand(CommandEvent event) {
        if(event.getArgs().isEmpty())
        {
            EmbedBuilder ebuilder = new EmbedBuilder();
            ebuilder.setColor(event.getSelfMember().getColor());
            ebuilder.setTitle("Automod Ignores",null);
            StringBuilder builder = new StringBuilder();
            List<Role> roles = sia.getServiceManagers().getIgnoredService().getIgnoredRoles(event.getGuild());
            List<TextChannel> channels = sia.getServiceManagers().getIgnoredService().getIgnoredChannels(event.getGuild());
            event.getGuild().getRoles().forEach(r -> {
                if(roles.contains(r))
                    builder.append("\n").append(r.getAsMention());
                else if(!event.getSelfMember().canInteract(r))
                    builder.append("\n").append(r.getAsMention()).append(" [can't interact]");
                else if(r.getPermissions().contains(Permission.ADMINISTRATOR) 
                        || r.getPermissions().contains(Permission.MANAGE_SERVER) 
                        || r.getPermissions().contains(Permission.BAN_MEMBERS)
                        || r.getPermissions().contains(Permission.KICK_MEMBERS)
                        || r.getPermissions().contains(Permission.MESSAGE_MANAGE))
                    builder.append("\n").append(r.getAsMention()).append(" [elevated perms]");
            });
            channels.forEach(c -> builder.append("\n").append(c.getAsMention()));
            ebuilder.setDescription(builder.length() > 2045 ? builder.substring(0, 2048) + "..." : builder.toString());
            event.reply(ebuilder.build());
            return;
        }
        
        String id = event.getArgs().replaceAll("<#(\\d{17,20})>", "$1");
        TextChannel tc;
        try {
            tc = event.getGuild().getTextChannelById(id);
        } catch(Exception e) {
            tc = null;
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
        }
        if(tc!=null)
        {
            sia.getServiceManagers().getIgnoredService().ignore(tc);
            event.replySuccess("Automod is now ignoring channel <#"+tc.getId()+">");
            return;
        }
        
        List<Role> roles = FinderUtil.findRoles(event.getArgs(), event.getGuild());
        if(roles.isEmpty())
            event.replyError("No roles or text channels found for `"+event.getArgs()+"`");
        else if (roles.size()==1)
        {
            sia.getServiceManagers().getIgnoredService().ignore(roles.get(0));
            event.replySuccess("Automod is now ignoring role `"+roles.get(0).getName()+"`");
        }
        else
            event.replyWarning(FormatUtil.listOfRoles(roles, event.getArgs()));
    }
}
