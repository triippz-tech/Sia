package com.trievosoftware.application.service;

import com.trievosoftware.discord.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;
import pl.zankowski.iextrading4j.api.stocks.Company;
import pl.zankowski.iextrading4j.api.stocks.Logo;
import pl.zankowski.iextrading4j.api.stocks.News;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.CompanyRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.LogoRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.NewsRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class IEXService {

    private final Logger log = LoggerFactory.getLogger(IEXService.class);


    public Message getQuote(String ticker) {

        final IEXTradingClient iexTradingClient = IEXTradingClient.create();
        log.debug("Request to get Stock Quote for {}", ticker);

        try {
            final Quote quote = iexTradingClient.executeRequest(new QuoteRequestBuilder()
                .withSymbol(ticker.toUpperCase())
                .build());

            final Logo logo = iexTradingClient.executeRequest(new LogoRequestBuilder()
                .withSymbol(ticker.toUpperCase())
                .build());

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String marketCap = numberFormat.format(quote.getMarketCap());

            MessageBuilder builder = new MessageBuilder();
            builder.setEmbed(new EmbedBuilder()
                .setThumbnail(logo.getUrl())
                .setTitle("**Quote Information For: **" + quote.getCompanyName())
                .addField("Ticker", quote.getSymbol(), true)
                .addField("Sector", quote.getSector(), true)
                .addField("Ask", "$" + quote.getLatestPrice().toPlainString(), true)
                .addField("High", "$" + quote.getHigh().toPlainString(), true)
                .addField("Low", "$" + quote.getLow().toPlainString(), true)
                .addField("Market Cap", "$" + marketCap, true)
                .addField("% Change 24H", quote.getChangePercent().toPlainString() + "%", true)
                .build()
            );

            return builder.build();
        } catch (IEXTradingException e)
        {
            return new MessageBuilder()
                .setContent(Constants.ERROR + e.getMessage().replace("Message received from IEX Trading: ", ""))
                .build();
        }
    }

    public Message getInfo(String ticker)
    {
        log.debug("Request to get Stock Info for {}", ticker);

        final IEXTradingClient iexTradingClient = IEXTradingClient.create();
        try {
            final Company company = iexTradingClient.executeRequest(new CompanyRequestBuilder()
                .withSymbol(ticker.toUpperCase())
                .build());

            final Logo logo = iexTradingClient.executeRequest(new LogoRequestBuilder()
                .withSymbol(ticker.toUpperCase())
                .build());

            MessageBuilder builder = new MessageBuilder();
            builder.setEmbed(new EmbedBuilder()
                .setThumbnail(logo.getUrl())
                .setTitle("**Quote Information For: **" + company.getCompanyName())
                .addField("Ticker", company.getSymbol(), true)
                .addField("Sector", company.getSector(), true)
                .addField("Industry", company.getIndustry(), true)
                .addField("Exchange", company.getExchange(), true)
                .addField("CEO", company.getCEO(), false)
                .addField("\uD83D\uDCBB", "[Website](" + company.getWebsite() + ")", false)
                .addField("Description", company.getDescription(), false)
                .build()
            );

            return builder.build();
        } catch (IEXTradingException e)
        {
            return new MessageBuilder()
                .setContent(Constants.ERROR + e.getMessage().replace("Message received from IEX Trading: ", ""))
                .build();
        }
    }

    public List<Message> getNews(String ticker)
    {
        log.debug("Request to get News for {}", ticker);

        final IEXTradingClient iexTradingClient = IEXTradingClient.create();
        List<Message> messages = new ArrayList<>();

        try {
            final List<News> newsList = iexTradingClient.executeRequest(new NewsRequestBuilder()
                .withSymbol(ticker.toUpperCase())
                .withWorldNews()
                .build());

            if (newsList.isEmpty())
                messages.add(new MessageBuilder()
                    .setContent(Constants.ERROR + " Could not find any news for `" + ticker + "`").build());


            for ( News news : newsList )
            {
                MessageBuilder builder = new MessageBuilder();
                builder.setEmbed(new EmbedBuilder()
                    .setTitle(news.getHeadline().substring(0, Math.min(news.getHeadline().length(), 256)))
                    .setThumbnail(news.getImage())
                    .setDescription(news.getSummary())
                    .setAuthor(news.getSource(), news.getUrl())
                    .build()
                );
                messages.add(builder.build());
            }

        } catch (IEXTradingException e)
        {
            messages.add(new MessageBuilder()
                .setContent(Constants.ERROR + e.getMessage().replace("Message received from IEX Trading: ", ""))
                .build());
        }
        return messages;
    }
}
