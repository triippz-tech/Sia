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
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.utils.ArgsUtil;
import me.joshmcfarlin.cryptocompareapi.CryptoCompareAPI;
import me.joshmcfarlin.cryptocompareapi.Exceptions.InvalidParameterException;
import me.joshmcfarlin.cryptocompareapi.Exceptions.OutOfCallsException;
import me.joshmcfarlin.cryptocompareapi.models.coin.CoinEntry;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@CommandInfo(
    description = "Look up cryptocurrency prices:\n" +
        "Single Coin, Single Price: /crypto -p -coin XLM -cur USD\n" +
        "Single Coin, Multiple Prices: /crypto -p -coin XLM -cur USD -cur BTC -cur EUR\n"
//        "Multiple Coins, Single Price: /crypto -p -coin XLM -coin BTC -cur USD\n" +
//        "Multiple Coins, Multiple Prices: /crypto -p -coin XLM -coin BTC -cur USD -cur EUR"
)
@Author("Mark Tripoli (Triippz)")
public class CryptoCoinCommand extends Command {

    private final Logger log = LoggerFactory.getLogger(CryptoCoinCommand.class);

    private CryptoCompareAPI cryptoCompare;
    private static final String BASE_URL = "https://www.cryptocompare.com";
    private static final String HELP = "Look up crypto specific information (Must use ticker):\n" +
        "Single Coin, Single Price: /crypto -coin XLM -cur USD\n" +
        "Single Coin, Multiple Prices: /crypto -coin XLM -cur USD -cur BTC -cur EUR\n";
//        "Multiple Coins, Single Price: /crypto -coin XLM -coin BTC -cur USD\n" +
//        "Multiple Coins, Multiple Prices: /crypto -coin XLM -coin BTC -cur USD -cur EUR";


    public CryptoCoinCommand(Sia sia)
    {
        this.name = "crypto";
        this.aliases = new String[]{"coin"};
        this.help = HELP;
        this.arguments = "-coin [XLM] -cur [BTC] -cur [USD]";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.cryptoCompare = new CryptoCompareAPI(sia.getApplicationProperties().getApi().getCryptoCompare());
    }

    @Override
    @SuppressWarnings("Duplicates")
    protected void execute(CommandEvent event) {
        if(event.getArgs().isEmpty()) {
            event.reply("Stand-by, let me look it up . . .");
            try {
                event.replyError("It looks like you forgot your commands! So here is my personal favorite");

                Map<String, Double> coinPrices = cryptoCompare.market.getPrice("XLM", "USD,EUR,BTC,ETH");
                Map<String, CoinEntry> entries = cryptoCompare.coins.getCoinList().getCoins();
                MessageBuilder builder = formatSingleCoinPrice(entries.get("XLM"), coinPrices);

                event.reply(builder.build());

            } catch (IOException | NullPointerException | InvalidParameterException e) {
                log.error("{}", e.getMessage());
                event.replyError("It looks like there was an issue processing your request. Please ensure" +
                    "you spelled everything correctly, and only used an approved coin ticker.");
            } catch (OutOfCallsException e){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("Looks like we have reach our maximum call requests for this month.");
                builder.setColor(Color.red);
                event.reply(builder.build());
            }
        }
        else {
            String[] arguments = event.getArgs().toUpperCase().split(" ");

            try {
                log.info("User {}, requested crypto prices", event.getAuthor().getName());
                event.reply(getPriceMessage(arguments).build());

            } catch (InvalidParameterException | IOException | ParameterException | NullPointerException e) {
                log.error("Unable to process price request: {}", e.getMessage());
                event.replyError("Sorry, I was unable to perform your request. " +
                    "Please ensure you have properly used the command.");
                e.printStackTrace();
            } catch (OutOfCallsException e){
                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("Looks like we have reach our maximum call requests for this month.");
                builder.setColor(Color.red);
                event.reply(builder.build());
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private MessageBuilder getPriceMessage(String[] args) throws InvalidParameterException, IOException,
        OutOfCallsException, ParameterException {
        ArgsUtil.CryptoArgs cryptoArgs = new ArgsUtil.CryptoArgs(args);
        List<String> coinsList = cryptoArgs.getCoins();
        List<String> curenciesList = cryptoArgs.getCurrencies();

//        String coins = StringUtils.join(coinsList, ",");
        String currencies = StringUtils.join(curenciesList, ",");

        Map<String, Double> coinPrices = cryptoCompare.market.getPrice(coinsList.get(0), currencies);
        Map<String, CoinEntry> entries = cryptoCompare.coins.getCoinList().getCoins();

        log.info("Building Price Message. Coins: {} Currencies: {}", coinsList, curenciesList);

        return formatSingleCoinPrice(entries.get(coinsList.get(0)), coinPrices);
    }

    private Message formatCoinPrice(Map<String, CoinEntry> entries, Map<String, Double> prices) {
        MessageBuilder messageBuilder = new MessageBuilder();

        for ( Map.Entry<String, Double> price: prices.entrySet() ) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            CoinEntry coinEntry = entries.get(price.getKey());

            messageBuilder.append("Price(s) for: **").append(coinEntry.getFullName()).append("**\n");
            embedBuilder.setThumbnail(BASE_URL + coinEntry.getImageUrl());
            stringBuilder.append(price.getValue().doubleValue()).append(" **").append(price.getKey()).append("**\n");
            embedBuilder.setDescription(stringBuilder.toString());
            messageBuilder.setEmbed(embedBuilder.build());
        }
        return messageBuilder.build();
    }

    @SuppressWarnings("Duplicates")
    private MessageBuilder formatSingleCoinPrice(CoinEntry coin, Map<String, Double> prices) {
        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder builder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        if ( coin.getImageUrl() != null)
            builder.setThumbnail(BASE_URL + coin.getImageUrl());

        for ( Map.Entry<String, Double> price : prices.entrySet() ) {
            stringBuilder.append(String.format("%f", price.getValue()))
                .append(" **").append(price.getKey()).append("**\n");
        }
         builder.setDescription(stringBuilder.toString());

        messageBuilder.append("Price(s) for: **").append(coin.getFullName()).append("**")
            .setEmbed(builder.build());
        return messageBuilder;
    }
}
