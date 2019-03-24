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

package com.trievosoftware.discord.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractOwnerCommand;

public class EnableDebugCommand extends AbstractOwnerCommand {

    public EnableDebugCommand(Sia sia) {
        super(sia);
        this.name = "debugmode";
        this.help = "Enables debug logging";
        this.arguments = "<ON/OFF>";
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        if ( event.getArgs().equalsIgnoreCase("ON"))
        {
            sia.setDebugMode(true);
            event.replySuccess("Debug mode has been enabled.");
            sia.getLogWebhook().send("Debug mode has been ENABLED");
        }
        else if ( event.getArgs().equalsIgnoreCase("OFF"))
        {
            sia.setDebugMode(false);
            event.replySuccess("Debug mode has been disabled");
        } else {
            event.replyError("Please enter `ON` or `OFF`");
            sia.getLogWebhook().send("Debug mode has been DISABLED");
        }
    }
}
