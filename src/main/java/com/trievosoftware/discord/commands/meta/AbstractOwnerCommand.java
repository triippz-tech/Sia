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
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.Permission;

public abstract class AbstractOwnerCommand extends Command
{
    protected Sia sia;
    public AbstractOwnerCommand(Sia sia)
    {
        this.sia = sia;
        this.ownerCommand = true;
        this.category = new Category("Owner Command");
        this.hidden = true;
        this.guildOnly = false;
    }
}
