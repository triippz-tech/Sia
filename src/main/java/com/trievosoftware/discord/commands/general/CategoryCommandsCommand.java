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

package com.trievosoftware.discord.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;

public class CategoryCommandsCommand extends AbstractGenericCommand {

    public CategoryCommandsCommand(Sia sia)
    {
        super(sia);
        this.name = "commands";
        this.aliases = new String[]{"cmds"};
        this.help = "Displays all commands for a category";
    }

    @Override
    public void doCommand(CommandEvent event) {

    }

    public class Categories extends AbstractGenericCommand
    {
        public Categories (Sia sia)
        {
            super(sia);
            this.name = "categories";
            this.help = "Shows the different categories of commands";
            this.guildOnly = true;
        }

        @Override
        public void doCommand(CommandEvent event)
        {
        }
    }
}
