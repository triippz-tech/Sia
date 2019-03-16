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

package com.trievosoftware.discord.commands.music.moderator;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractDjCommand;
import com.trievosoftware.discord.commands.meta.AbstractMusicCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class SetupMusicCommand extends AbstractDjCommand {

    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑

    public SetupMusicCommand(Sia sia) {
        super(sia);
        this.name = "msetup";
        this.aliases = new String[]{"musicsetup"};
        this.help = "music setup";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.children = new Command[]{new DjRoleCommand(this.sia)};
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        StringBuilder sb = new StringBuilder("The following commands can be used to set up a **").append(event.getSelfUser().getName()).append("** feature:\n");
        for(Command cmd: children)
            sb.append("\n`").append(Constants.PREFIX).append(name).append(" ").append(cmd.getName()).append("` - ").append(cmd.getHelp());
        event.replySuccess(sb.toString());
    }

    private class DjSetupCommand extends AbstractMusicCommand
    {
        @SuppressWarnings("Duplicates")
        private DjSetupCommand(Sia sia)
        {
            super(sia);
            this.name = "djrole";
            this.aliases = new String[]{"musicrole"};
            this.help = "sets up the 'DJ' role";
            this.guildOnly = true;
            this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
            this.botPermissions = new Permission[]{Permission.ADMINISTRATOR};
            this.cooldown = 20;
            this.cooldownScope = Command.CooldownScope.GUILD;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            Role djRole = sia.getServiceManagers().getGuildMusicSettingsService().getSettings(event.getGuild()).getDjRole(event.getGuild());
            String confirmation;
            if(djRole!=null)
            {
                if(!event.getSelfMember().canInteract(djRole))
                {
                    event.replyError("I cannot interact with the existing '"+djRole.getName()
                        +"' role. Please move my role(s) higher and then try again.");
                    return;
                }
                if(!event.getMember().canInteract(djRole))
                {
                    event.replyError("You do not have permission to interact with the existing '"+djRole.getName()+"' role.");
                    return;
                }
                confirmation = "This will modify the existing '"+djRole.getName()+"' role and assign it overrides in every channel.";
            }
            else
                confirmation = "This will create a role called 'DJ' and assign it overrides in every channel.";
            waitForConfirmation(event, confirmation, () -> setUpDjRole(event, djRole));
        }

        @Override
        public void doCommand(CommandEvent event) {

        }
    }

    private void setUpDjRole(CommandEvent event, Role role)
    {
        StringBuilder sb = new StringBuilder(Constants.SUCCESS+" DJ role setup started!\n");
        event.reply(sb + Constants.LOADING+" Initializing role...", m -> event.async(() ->
        {
            try
            {
                Role djRole;
                if(role==null)
                {
                    djRole = event.getGuild().getController().createRole().setName("DJ").setPermissions().setColor(1).complete();
                }
                else
                {
                    role.getManager().setPermissions().complete();
                    djRole = role;
                }
                sb.append(Constants.SUCCESS+" Role initialized!\n");
                m.editMessage(sb + Constants.LOADING+" Making Category overrides...").complete();
                PermissionOverride po;
                for(net.dv8tion.jda.core.entities.Category cat: event.getGuild().getCategories())
                {
                    po = cat.getPermissionOverride(djRole);
                    if(po==null)
                        cat.createPermissionOverride(djRole).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                    else
                        po.getManager().deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                }
                sb.append(Constants.SUCCESS+" Category overrides complete!\n");
                m.editMessage(sb + Constants.LOADING + " Making Text Channel overrides...").complete();
                for(TextChannel tc: event.getGuild().getTextChannels())
                {
                    po = tc.getPermissionOverride(djRole);
                    if(po==null)
                        tc.createPermissionOverride(djRole).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
                    else
                        po.getManager().deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
                }
                sb.append(Constants.SUCCESS+" Text Channel overrides complete!\n");
                m.editMessage(sb + Constants.LOADING + " Making Voice Channel overrides...").complete();
                for(VoiceChannel vc: event.getGuild().getVoiceChannels())
                {
                    po = vc.getPermissionOverride(djRole);
                    if(po==null)
                        vc.createPermissionOverride(djRole).setDeny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                    else
                        po.getManager().deny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).complete();
                }
                m.editMessage(sb + Constants.SUCCESS+" Voice Channel overrides complete!\n\n" + Constants.SUCCESS+" DJ role setup has completed!").queue();
            }
            catch(Exception ex)
            {
                m.editMessage(sb + Constants.ERROR+" An error occurred setting up the DJ role. Please check that I have the Administrator permission and that the DJ role is below my roles.").queue();
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), ex.getMessage()));
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
