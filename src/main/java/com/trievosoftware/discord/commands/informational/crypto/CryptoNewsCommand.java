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

package com.trievosoftware.discord.commands.informational.crypto;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;
import me.joshmcfarlin.cryptocompareapi.CryptoCompareAPI;
import me.joshmcfarlin.cryptocompareapi.Exceptions.OutOfCallsException;
import me.joshmcfarlin.cryptocompareapi.models.news.NewsStory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Author("Mark Tripoli (Triippz)")
public class CryptoNewsCommand extends AbstractGenericCommand {

    private final Logger log = LoggerFactory.getLogger(CryptoNewsCommand.class);

    private CryptoCompareAPI cryptoCompare;
    private static final String BASE_URL = "https://www.cryptocompare.com";
    private static final String HELP = "Crypto Currency news!\n" +
        "To view the top 5 news stories of the day use: /cnews\n" +
        "To try to find an article based off of a keyword use: /cnews [keyword]";

    public CryptoNewsCommand(Sia sia)
    {
        super(sia);
        this.name = "cryptonews";
        this.aliases = new String[]{"cn", "cnews", "coinnews"};
        this.help = HELP;
        this.arguments = "[SEARCHWORD]";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.cryptoCompare = new CryptoCompareAPI(sia.getApplicationProperties().getApi().getCryptoCompare());
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void doCommand(CommandEvent event) {
        event.reply("Standby, let me see what I can find . . .");
        try {
            if (event.getArgs().isEmpty() ) {
                List<NewsStory> newsStories = cryptoCompare.news.newsList();
                log.error("{}", newsStories.size());
                for ( int i = 0; i < 3; i++ ){
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setThumbnail(newsStories.get(i).getImageUrl());
                    embedBuilder.setTitle(newsStories.get(i).getTitle());
                    embedBuilder.setDescription(newsStories.get(i).getUrl());

                    event.reply(embedBuilder.build());
                }
            } else {

                List<NewsStory> newsStories = cryptoCompare.news.newsList();
                List<NewsStory> storiesFound = new ArrayList<>();
                for ( NewsStory story: newsStories ) {
                    if ( story.getTitle().contains(event.getArgs()))
                        storiesFound.add(story);
                }

                if ( storiesFound.isEmpty() )
                    event.replyError("Sorry, I could not find any related articles based off of your search!");

                for ( NewsStory story: storiesFound ) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setThumbnail(story.getImageUrl());
                    embedBuilder.setTitle(story.getTitle());
                    embedBuilder.setDescription(story.getUrl());

                    event.reply(embedBuilder.build());
                }
            }

        } catch (IOException | NullPointerException e) {
            log.error("{}", e.getMessage());
            event.replyError("It looks like there was an issue processing your request. Please ensure" +
                "you spelled everything correctly.");
        } catch (OutOfCallsException e){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("Looks like we have reach our maximum call requests for this month.");
            builder.setColor(Color.red);
            event.reply(builder.build());
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
        }
    }
}
