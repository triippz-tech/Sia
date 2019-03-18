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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.entities.Category;

import java.util.List;

public class CategoryCommandsCommand extends Command {

    private Sia sia;
    public CategoryCommandsCommand(Sia sia)
    {
        this.sia = sia;
        this.name = "commands";
        this.aliases = new String[]{"cmds"};
        this.help = "Displays all commands for a category";
    }

    @Override
    protected void execute(CommandEvent event) {

    }

    public class Categories extends Command
    {
        private Sia sia;
        public Categories (Sia sia)
        {
            this.sia = sia;
            this.name = "categories";
            this.help = "Shows the different categories of commands";
            this.guildOnly = true;
        }

        @Override
        protected void execute(CommandEvent event)
        {
        }
    }
}
