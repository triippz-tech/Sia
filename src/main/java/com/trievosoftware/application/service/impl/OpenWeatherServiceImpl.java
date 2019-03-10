package com.trievosoftware.application.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.trievosoftware.application.config.ApplicationProperties;
import com.trievosoftware.application.domain.weather.CurrentWeather;
import com.trievosoftware.application.domain.weather.List;
import com.trievosoftware.application.domain.weather.WeatherForecast;
import com.trievosoftware.application.exceptions.NoWeatherException;
import com.trievosoftware.application.rest.dao.HttpRequestSender;
import com.trievosoftware.application.rest.dao.RequestSender;
import com.trievosoftware.application.rest.http.Request;
import com.trievosoftware.application.rest.http.Response;
import com.trievosoftware.application.rest.util.UrlUtil;
import com.trievosoftware.application.service.OpenWeatherService;
import com.trievosoftware.discord.utils.Conversions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;

@Service
@Transactional
@SuppressWarnings("Duplicates")
public class OpenWeatherServiceImpl implements OpenWeatherService {

    private final Logger log = LoggerFactory.getLogger(OpenWeatherServiceImpl.class);
    private final String CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
    private final String WEATHER_FORECAST = "https://api.openweathermap.org/data/2.5/forecast";
    private final String OPENWEATHER_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/1/15/OpenWeatherMap_logo.png";

    private ApplicationProperties applicationProperties;
    private String apiKey = "&appid=";
    private RequestSender sender;
    private Gson gson;

    public OpenWeatherServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        apiKey = apiKey + this.applicationProperties.getApi().getOpenweathermap();

        sender = new HttpRequestSender();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Gets the current weather of an area by zip code
     *
     * @param zipCode the zipcodee of the area
     * @return The current weather
     * @throws NoWeatherException exceptions
     */
    @Override
    public CurrentWeather getWeatherByCityZipCode(Integer zipCode) throws NoWeatherException {
        log.debug("Request to get Weather for zipCode={}", zipCode);
        String url = CURRENT_WEATHER
            + "?zip=" + zipCode
            + apiKey;
        HashMap<String, String> params = new HashMap<>();

        Request request = new Request(UrlUtil.buildUrlQuery(url, params));
        try {
            Response response = sender.sendRequest(request);
            return gson.fromJson(response.getBody(), CurrentWeather.class);
        } catch (JsonSyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            throw new NoWeatherException(e.getMessage());
        }
    }

    /**
     * Gets the current 5 day forecast of an area by its zipcode
     *
     * @param zipCode The zip code of the area
     * @return The WeatherForecast of an area
     * @throws NoWeatherException exceptions
     */
    @Override
    public WeatherForecast getFiveDayForecastByZipCode(Integer zipCode) throws NoWeatherException {
        log.debug("Request to get WeatherForecast for zipCode={}", zipCode);
        String url = WEATHER_FORECAST
            + "?zip=" + zipCode
            + apiKey;
        HashMap<String, String> params = new HashMap<>();

        Request request = new Request(UrlUtil.buildUrlQuery(url, params));
        try {
            Response response = sender.sendRequest(request);
            return gson.fromJson(response.getBody(), WeatherForecast.class);
        } catch (JsonSyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            throw new NoWeatherException(e.getMessage());
        }
    }

    // Now we want to create the Discord Embed Responses

    /**
     * Gets and Formats the CUrrent weather of an area
     *
     * @param zipCode The zipcode of the area
     * @param unit The unit of measurement (F/C)
     * @return A MessageEmbed for Discord
     * @throws NoWeatherException exceptions
     */
    @Override
    public MessageEmbed getWeatherEmbedByZipCode(Integer zipCode, String unit) throws NoWeatherException {
        CurrentWeather currentWeather = this.getWeatherByCityZipCode(zipCode);
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String currentTemp = Conversions.convertTemp(currentWeather.getMain().getTemp(), unit);
        String main = currentWeather.getWeather().get(0).getDescription();

        MessageEmbed.Field field = new MessageEmbed.Field(
            "*** Weather for: " + currentWeather.getName() + " ***",
            "Current Temp: " + currentTemp + "\n" +
                "Description: " + main,
            true
        );
        String resultUrl = "https://openweathermap.org/city/" + currentWeather.getId();
        embedBuilder.setTitle("** Current Weather For: " + currentWeather.getName() + " **");
        embedBuilder.setThumbnail(currentWeather.getWeather().get(0).getIconLink());
        embedBuilder.setDescription(field.getValue());
        embedBuilder.appendDescription("\n\n[View on OpenWeather](" + resultUrl + ")");
        return embedBuilder.build();
    }

    /**
     * Finds the weather forecast for a certain area and returns a response.
     *
     * @param zipCode zip code of area
     * @param unit unit of measurement
     * @return MessageEmbed for Discord Reply
     * @throws NoWeatherException exceptions
     */
    @Override
    public MessageEmbed getWeatherForecastEmbedByZipCode(Integer zipCode, String unit) throws NoWeatherException {
        WeatherForecast currentWeather = this.getFiveDayForecastByZipCode(zipCode);
        EmbedBuilder embedBuilder = new EmbedBuilder();

        for ( List list : currentWeather.getList() )
        {
            if ( list.getDtTxt().contains("12:00:00"))
            {
                String currentTemp = Conversions.convertTemp(list.getMain().getTemp(), unit);
                String main = list.getWeather().get(0).getDescription();
                MessageEmbed.Field field = new MessageEmbed.Field(
                    "***" + list.getDtTxt().replace("12:00:00", "") + " ***",
                    "Temperature: " + currentTemp + "\n" +
                        "Description: " + main,
                    false
                );
                embedBuilder.addField(field);
            }
        }
        String resultUrl = "https://openweathermap.org/city/" + currentWeather.getCity().getId();
        embedBuilder.setTitle("** Forecast for: " + currentWeather.getCity().getName() + ", " + currentWeather.getCity().getCountry() + "  **");
        embedBuilder.setDescription("[View on OpenWeather](" + resultUrl + ")");
        embedBuilder.setThumbnail(OPENWEATHER_IMAGE);
        return embedBuilder.build();
    }

}
