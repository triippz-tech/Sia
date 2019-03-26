/*
 * Copyright 2018 Mark Tripoli (mark.tripoli@trievosoftware.com).
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
package com.trievosoftware.discord.commands.automod;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractModeratorCommand;
import net.dv8tion.jda.core.Permission;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
public class BlacklistCmd extends AbstractModeratorCommand
{
    public BlacklistCmd(Sia sia)
    {
        super(sia);
        this.guildOnly = true;
        this.name = "blacklistword";
        this.category = new Category("AutoMod");
        this.arguments = "[word]";
        this.help = "shows/edits the word blacklist";
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        
    }
}
