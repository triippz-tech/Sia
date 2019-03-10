package com.trievosoftware.application.service;

import com.trievosoftware.application.domain.weather.CurrentWeather;
import com.trievosoftware.application.domain.weather.WeatherForecast;
import com.trievosoftware.application.exceptions.NoWeatherException;
import net.dv8tion.jda.core.entities.MessageEmbed;

public interface OpenWeatherService {

    CurrentWeather getWeatherByCityZipCode(Integer zipCode) throws NoWeatherException;

    WeatherForecast getFiveDayForecastByZipCode(Integer zipCode) throws NoWeatherException;

    // Now we want to create the Discord Embed Responses
    MessageEmbed getWeatherEmbedByZipCode(Integer zipCode, String unit) throws NoWeatherException;

    MessageEmbed getWeatherForecastEmbedByZipCode(Integer zipCode, String unit) throws NoWeatherException;
}
