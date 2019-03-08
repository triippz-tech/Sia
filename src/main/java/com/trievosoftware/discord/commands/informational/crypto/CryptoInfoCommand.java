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

import com.beust.jcommander.ParameterException;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.trievosoftware.application.exceptions.NoCoinFoundException;
import com.trievosoftware.discord.Sia;
import me.joshmcfarlin.cryptocompareapi.CryptoCompareAPI;
import me.joshmcfarlin.cryptocompareapi.Exceptions.OutOfCallsException;
import me.joshmcfarlin.cryptocompareapi.models.coin.CoinEntry;
import me.joshmcfarlin.cryptocompareapi.models.coin.CoinSnapshot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

@Component
@Author("Mark Tripoli (Triippz)")
public class CryptoInfoCommand extends Command {

    private final Logger log = LoggerFactory.getLogger(CryptoInfoCommand.class);
    private static final String BASE_URL = "https://www.cryptocompare.com";
    private final static String WEBSITE_EMOJII = "\uD83D\uDDA5";
    private final String TWITTER_EMOJII = "<:twitter:550497668305256450>";
    private final String SCROLL_EMOJII = "<:scroll:550499361134608384>";
    private String LINK_TEMPLATE = "https://%s.com";
    private CryptoCompareAPI cryptoCompare;

    public CryptoInfoCommand(Sia sia)
    {
        this.name = "cryptoinfo";
        this.aliases = new String[]{"coininfo", "ci", "cinfo"};
        this.help = "Searches for information for a particular coin";
        this.arguments = "[Coin_Ticker]";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.cryptoCompare = new CryptoCompareAPI(sia.getApplicationProperties().getApi().getCryptoCompare());
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] arguments = event.getArgs().toUpperCase().split(" ");
        try {
            if (event.getArgs().isEmpty()) {
                event.replyError("Oops, it looks like you forgot your arguments.");
            } else {
                event.reply("Please standby, let me see what I can find . . .");
                log.info("User {}, requested cryptocurrency info", event.getAuthor().getName());
                event.reply(getCoinInformationMessage(arguments[0]).build());
            }
        } catch (IOException | ParameterException e) {
            log.error("Unable to process price request: {}", e.getMessage());
            event.replyError("Sorry, I was unable to perform your request. " +
                "Please ensure you have properly used the command.");
            e.printStackTrace();
        } catch (OutOfCallsException e){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("Looks like we have reached our maximum call requests for this month.");
            builder.setColor(Color.red);
            event.reply(builder.build());
        } catch (NoCoinFoundException e) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("Sorry, I could not find any information for: " + arguments[0].toUpperCase());
            builder.setColor(Color.red);
            event.reply(builder.build());
        }
    }

    @SuppressWarnings("Duplicates")
    private MessageBuilder getCoinInformationMessage(String coinName) throws IOException,
        OutOfCallsException, ParameterException
    {
        Map<String, CoinEntry> coins = cryptoCompare.coins.getCoinList().getCoins();
        try {
            CoinSnapshot coinSnapshot = cryptoCompare.coins.getCoinSnapshot(coins.get(coinName.toUpperCase()).getId());
            return formatSingleCoinData(coinSnapshot);
        } catch (NullPointerException e) {
            throw new NoCoinFoundException(e.getMessage());
        }
    }

    @SuppressWarnings("Duplicates")
    private MessageBuilder formatSingleCoinData(CoinSnapshot coinSnapshot) {
        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        if ( coinSnapshot.getData().getGeneral().getImageUrl() != null)
            builder.setThumbnail(BASE_URL + coinSnapshot.getData().getGeneral().getImageUrl());

        builder.setTitle("**Coin Information For: **" + coinSnapshot.getData().getGeneral().getName());
        builder.addField("Algorithm", coinSnapshot.getData().getGeneral().getAlgorithm(), true);
        builder.addField("ProofType", coinSnapshot.getData().getGeneral().getProofType(), true);
        builder.addField("Total Coin Supply", coinSnapshot.getData().getGeneral().getTotalCoinSupply(), true);
        builder.addField("Total Coins Mined", coinSnapshot.getData().getGeneral().getTotalCoinsMined(), true);
        builder.addField("Block Reward", coinSnapshot.getData().getGeneral().getBlockReward(), true);
        builder.addField("Current Hashes", coinSnapshot.getData().getGeneral().getNetHashesPerSecond(), true);

        Document htmlUrl = Jsoup.parse(coinSnapshot.getData().getGeneral().getWebsite());
        String urlStr = htmlUrl.select("a").attr("href");
        String twitterStr = TWITTER_EMOJII + " **" + coinSnapshot.getData().getGeneral().getTwitter() + "**\n";

        String description =
             Jsoup.parseBodyFragment(coinSnapshot.getData().getGeneral().getDescription()).text() + "\n\n"
                 + WEBSITE_EMOJII + " **[Website](" + urlStr + ")**\n"
                 + twitterStr;
        if (coinSnapshot.getData().getIco() != null ) {
            if ( coinSnapshot.getData().getIco() != null )
                description = description + SCROLL_EMOJII + " **[White Paper](" + coinSnapshot.getData().getIco().getWhitePaper() + ")**\n";
        }
        builder.setDescription(description);

        messageBuilder.setEmbed(builder.build());
        return messageBuilder;
    }
}
