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

package com.trievosoftware.discord.commands.music.moderator;/*
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

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.trievosoftware.application.exceptions.NoMusicSettingsException;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractDjCommand;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

@SuppressWarnings("Duplicates")
public class DjRoleCommand extends AbstractDjCommand {

    public DjRoleCommand(Sia sia) {
        super(sia);
        this.name = "djrole";
        this.help = "sets the dj role";
        this.arguments = "<role|NONE>";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        if (event.getArgs().isEmpty()) {
            event.replyError("Please include the name of a role to use as the DJ role. Members with this role will be able to use all DJ commands.");
            return;
        } else if (event.getArgs().equalsIgnoreCase("none") || event.getArgs().equalsIgnoreCase("off")) {
            try {
                sia.getServiceManagers().getGuildMusicSettingsService().setDjRole(event.getGuild(), null);
            } catch (NoMusicSettingsException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
            event.replySuccess("DJ role cleared. Now only moderators may use DJ commands.");
            return;
        }

        List<Role> roles = FinderUtil.findRoles(event.getArgs(), event.getGuild());
        if (roles.isEmpty())
            event.replyError("No roles found called `" + event.getArgs() + "`");
        else if (roles.size() == 1) {
            try {
                sia.getServiceManagers().getGuildMusicSettingsService().setDjRole(event.getGuild(), roles.get(0));
            } catch (NoMusicSettingsException e) {
                event.replyError(e.getMessage());
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            }
            event.replySuccess("Users with the `" + roles.get(0).getName() + "` role can now use all DJ commands.");
        } else
            event.replyWarning(FormatUtil.listOfRoles(roles, event.getArgs()));
    }

}
