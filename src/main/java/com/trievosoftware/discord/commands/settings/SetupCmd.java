/*
 * Copyright 2016 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trievosoftware.discord.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.trievosoftware.application.domain.AutoMod;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author John Grosh (jagrosh)
 */
@SuppressWarnings("Duplicates")
public class SetupCmd extends Command
{
    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑
    
    private final Sia sia;
    
    public SetupCmd(Sia sia)
    {
        this.sia = sia;
        this.name = "setup";
        this.category = new Category("Settings");
        this.help = "server setup";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
        this.botPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.children = new Command[]{new MuteSetupCmd(), new AutomodSetupCmd()};
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        StringBuilder sb = new StringBuilder("The following commands can be used to set up a **").append(event.getSelfUser().getName()).append("** feature:\n");
        for(Command cmd: children)
            sb.append("\n`").append(Constants.PREFIX).append(name).append(" ").append(cmd.getName()).append("` - ").append(cmd.getHelp());
        event.replySuccess(sb.toString());
    }
    
    private class AutomodSetupCmd extends Command
    {
        @SuppressWarnings("Duplicates")
        private AutomodSetupCmd()
        {
            this.name = "automod";
            this.aliases = new String[]{"auto"};
            this.category = new Category("Settings");
            this.help = "sets up the auto-moderator";
            this.guildOnly = true;
            this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
            this.botPermissions = new Permission[]{Permission.ADMINISTRATOR};
            this.cooldown = 20;
            this.cooldownScope = CooldownScope.GUILD;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            waitForConfirmation(event, "This will enable all automod defaults. Any existing settings will not be overwritten.", () -> 
            {
                event.getChannel().sendTyping().queue();
                StringBuilder sb = new StringBuilder("**Automod setup complete!**");
                if(sia.getServiceManagers().getActionsService().useDefaultSettings(event.getGuild()))
                    sb.append("\n").append(Constants.SUCCESS).append(" Set up default punishments");
                AutoMod ams = sia.getServiceManagers().getAutoModService().getSettings(event.getGuild());
                if(ams.getInviteStrikes()==0)
                {
                    sia.getServiceManagers().getAutoModService().setInviteStrikes(event.getGuild(), 2);
                    sb.append("\n").append(Constants.SUCCESS).append(" Anti-invite set to `2` strikes");
                }
                if(ams.getRefStrikes()==0)
                {
                    sia.getServiceManagers().getAutoModService().setRefStrikes(event.getGuild(), 3);
                    sb.append("\n").append(Constants.SUCCESS).append(" Anti-referral set to `3` strikes");
                }
                if(!ams.useAntiDuplicate())
                {
                    sia.getServiceManagers().getAutoModService().setDupeSettings(event.getGuild(), 1, 2, 4);
                    sb.append("\n").append(Constants.SUCCESS).append(" Anti-duplicate will start deleting at duplicate `2`, and will assign `1` strike each duplicate starting at duplicate `4`");
                }
                if(ams.getCopyPastaStrikes()==0)
                {
                    sia.getServiceManagers().getAutoModService().setCopypastaStrikes(event.getGuild(), 1);
                    sb.append("\n").append(Constants.SUCCESS).append(" Anti-copypasta set to `1` strikes");
                }
                if(ams.getMaxMentions()==0)
                {
                    sia.getServiceManagers().getAutoModService().setMaxMentions(event.getGuild(), 10);
                    sb.append("\n").append(Constants.SUCCESS).append(" Maximum mentions set to `10` mentions");
                }
                if(ams.getMaxRoleMentions()==0)
                {
                    sia.getServiceManagers().getAutoModService().setMaxRoleMentions(event.getGuild(), 4);
                    sb.append("\n").append(Constants.SUCCESS).append(" Maximum role mentions set to `4` mentions");
                }
                if(ams.getMaxLines()==0)
                {
                    sia.getServiceManagers().getAutoModService().setMaxLines(event.getGuild(), 10);
                    sb.append("\n").append(Constants.SUCCESS).append(" Maximum lines set to `10` lines");
                }
                if(!ams.useAutoRaidMode())
                {
                    sia.getServiceManagers().getAutoModService().setAutoRaidMode(event.getGuild(), 10, 10);
                    sb.append("\n").append(Constants.SUCCESS).append(" Anti-Raid Mode will activate upon `10` joins in `10` seconds");
                }
                if(ams.getDehoistChar()==(char)0)
                {
                    sia.getServiceManagers().getAutoModService().setDehoistChar(event.getGuild(), '!');
                    sb.append("\n").append(Constants.SUCCESS).append(" Names starting with `!` will be dehoisted");
                }
                sb.append("\n").append(Constants.WARNING).append(" Any settings not shown here were not set due to being already set. Please check the automod section of the wiki (<")
                        .append(Constants.Wiki.AUTOMOD).append(">) for more information about the automod.");
                event.replySuccess(sb.toString());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("GuildSettings set for Guild=%s/%d", event.getGuild(), event.getGuild().getIdLong()));
            });
        }
    }
    
    private class MuteSetupCmd extends Command
    {
        @SuppressWarnings("Duplicates")
        private MuteSetupCmd()
        {
            this.name = "muterole";
            this.aliases = new String[]{"muted","mute","mutedrole"};
            this.category = new Category("Settings");
            this.help = "sets up the 'Muted' role";
            this.guildOnly = true;
            this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
            this.botPermissions = new Permission[]{Permission.ADMINISTRATOR};
            this.cooldown = 20;
            this.cooldownScope = CooldownScope.GUILD;
        }
        
        @Override
        protected void execute(CommandEvent event)
        {
            Role muted = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild()).getMutedRole(event.getGuild());
            String confirmation;
            if(muted!=null)
            {
                if(!event.getSelfMember().canInteract(muted))
                {
                    event.replyError("I cannot interact with the existing '"+muted.getName()+"' role. Please move my role(s) higher and then try again.");
                    return;
                }
                if(!event.getMember().canInteract(muted))
                {
                    event.replyError("You do not have permission to interact with the existing '"+muted.getName()+"' role.");
                    return;
                }
                confirmation = "This will modify the existing '"+muted.getName()+"' role and assign it overrides in every channel.";
            }
            else
                confirmation = "This will create a role called 'Muted' and assign it overrides in every channel.";
            waitForConfirmation(event, confirmation, () -> setUpMutedRole(event, muted));
        }
    }
    
    private void setUpMutedRole(CommandEvent event, Role role)
    {
        StringBuilder sb = new StringBuilder(Constants.SUCCESS+" Muted role setup started!\n");
        event.reply(sb + Constants.LOADING+" Initializing role...", m -> event.async(() -> 
        {
            try
            {
                Role mutedRole;
                if(role==null)
                {
                    mutedRole = event.getGuild().getController().createRole().setName("Muted").setPermissions().setColor(1).complete();
                }
                else
                {
                    role.getManager().setPermissions().complete();
                    mutedRole = role;
                }
                sb.append(Constants.SUCCESS+" Role initialized!\n");
                m.editMessage(sb + Constants.LOADING+" Making Category overrides...").complete();
                PermissionOverride po;
                for(net.dv8tion.jda.core.entities.Category cat: event.getGuild().getCategories())
                {
                    po = cat.getPermissionOverride(mutedRole);
                    if(po==null)
                        cat.createPermissionOverride(mutedRole).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                    else
                        po.getManager().deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                }
                sb.append(Constants.SUCCESS+" Category overrides complete!\n");
                m.editMessage(sb + Constants.LOADING + " Making Text Channel overrides...").complete();
                for(TextChannel tc: event.getGuild().getTextChannels())
                {
                    po = tc.getPermissionOverride(mutedRole);
                    if(po==null)
                        tc.createPermissionOverride(mutedRole).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
                    else
                        po.getManager().deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
                }
                sb.append(Constants.SUCCESS+" Text Channel overrides complete!\n");
                m.editMessage(sb + Constants.LOADING + " Making Voice Channel overrides...").complete();
                for(VoiceChannel vc: event.getGuild().getVoiceChannels())
                {
                    po = vc.getPermissionOverride(mutedRole);
                    if(po==null)
                        vc.createPermissionOverride(mutedRole).setDeny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                    else
                        po.getManager().deny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                }
                m.editMessage(sb + Constants.SUCCESS+" Voice Channel overrides complete!\n\n" + Constants.SUCCESS+" Muted role setup has completed!").queue();
            }
            catch(Exception ex)
            {
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), ex.getMessage()));
                m.editMessage(sb + Constants.ERROR+" An error occurred setting up the Muted role. Please check that I have the Administrator permission and that the Muted role is below my roles.").queue();
            }
        }));
    }

    @SuppressWarnings("Duplicates")
    private void waitForConfirmation(CommandEvent event, String message, Runnable confirm)
    {
        new ButtonMenu.Builder()
                .setChoices(CONFIRM, CANCEL)
                .setEventWaiter(sia.getEventWaiter())
                .setTimeout(1, TimeUnit.MINUTES)
                .setText(Constants.WARNING+" "+message+"\n\n"+CONFIRM+" Continue\n"+CANCEL+" Cancel")
                .setFinalAction(m -> m.delete().queue(s->{}, f->{}))
                .setUsers(event.getAuthor())
                .setAction(re ->
                {
                    if(re.getName().equals(CONFIRM))
                        confirm.run();
                }).build().display(event.getChannel());
    }
}
