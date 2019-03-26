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

package com.trievosoftware.discord.commands.meta;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.Permission;

@SuppressWarnings("Duplicates")
public abstract class AbstractMusicModeratorCommand extends Command {

    protected Sia sia;

    public AbstractMusicModeratorCommand(Sia sia)
    {
        this.sia = sia;
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.category = new Category("Music Moderation");
    }

    @Override
    protected void execute (CommandEvent event)
    {
        if ( sia.getServiceManagers().getDiscordUserService().isUserBlacklisted(event.getAuthor().getIdLong()))
        {
            event.replyError("You are not authorized to use this bot. If you feel like this is a mistake please contact" +
                "the creators via discord: " + Constants.SERVER_INVITE);
            return;
        }
        sia.getServiceManagers().getDiscordUserService().addCommand(event.getAuthor());
        doCommand(event);
    }

    public abstract void doCommand(CommandEvent event);
}
