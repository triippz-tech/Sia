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
package com.trievosoftware.discord.commands.entertainment;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.giphy4j.Giphy;
import com.trievosoftware.giphy4j.entity.search.SearchFeed;
import com.trievosoftware.giphy4j.entity.search.SearchGiphy;
import com.trievosoftware.giphy4j.exception.GiphyException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@Author("Mark Tripoli (Triippz)")
public class GiphyCommand extends Command {
    private static final Logger log = LoggerFactory.getLogger(GiphyCommand.class);
    private Giphy giphy;

    public GiphyCommand(Sia sia)
    {
        this.name = "gif";
        this.aliases = new String[]{"gifs","giphy","meme"};
        this.help = "Searches for a gif based on a query parameter";
        this.arguments = "[search query]";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.giphy = new Giphy(sia.getApplicationProperties().getApi().getGiphy());
    }


    @Override
    protected void execute(CommandEvent event) {

        if(event.getArgs().isEmpty())
        {
            EmbedBuilder builder = new EmbedBuilder();
            try {
                SearchGiphy searchGiphy = giphy.searchByID("pOD7xmRum8R6o");
                builder.setImage(searchGiphy.getData().getImages().getDownsized().getUrl());
            } catch (GiphyException e) {
                event.reply("You forgot to include a search word!");
            }
            builder.setTitle("Looks like you forgot to include a search word!");
            builder.setColor(Color.red);
            event.reply(builder.build());
        }

        String args = event.getArgs();
        EmbedBuilder builder = new EmbedBuilder();
        try {
            SearchFeed searchGiphy = giphy.search(args, 1);
            builder.setImage(searchGiphy.getDataList().get(0).getImages().getDownsized().getUrl());
            event.reply(builder.build());
        } catch (GiphyException e) {
            try {
                SearchGiphy notFound = giphy.searchByID("IHOOMIiw5v9VS");
                builder.setColor(Color.red);
                builder.setTitle("Unable to find a related gif!");
                builder.setImage(notFound.getData().getImages().getDownsized().getUrl());
                event.reply(builder.build());
            } catch (GiphyException e1) {
                log.error("Error receiving default <NOT FOUND> GIF from Giphy: {}", e.getMessage());
            }
        }
    }
}
