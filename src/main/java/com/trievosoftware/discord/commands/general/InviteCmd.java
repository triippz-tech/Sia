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
package com.trievosoftware.discord.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class InviteCmd extends AbstractGenericCommand
{
    public InviteCmd(Sia sia)
    {
        super(sia);
        this.name = "invite";
        this.help = "invite the bot";
        this.guildOnly = false;
    }
    
    @Override
    public void doCommand(CommandEvent event)
    {
        event.reply("Hello. I am **"+event.getJDA().getSelfUser().getName()+"**, a simple moderation bot built by **triippz**#0689."
                + "\nYou can find out how to add me to your server with the link below:"
                + "\n\n\uD83D\uDD17 **<"+ Constants.Wiki.START+">**" // 🔗
                + "\n\nFor assistance, check out the wiki: <"+ Constants.Wiki.WIKI_BASE+">");
    }
}
