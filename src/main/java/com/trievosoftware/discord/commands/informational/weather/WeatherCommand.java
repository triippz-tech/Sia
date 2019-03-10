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

package com.trievosoftware.discord.commands.informational.weather;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.trievosoftware.application.exceptions.NoWeatherException;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Author("Mark Tripoli (triippz)")
public class WeatherCommand extends Command {
    private final Logger log = LoggerFactory.getLogger(WeatherCommand.class);
    private final String zipRegex = "\\d+";

    private Sia sia;


    public WeatherCommand (Sia sia)
    {
        this.name = "weather";
        this.help = "Finds the current or 5 day forecast by a zip code.";
        this.arguments = "[now/forecast] [zip code] [C/F]";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.sia = sia;
    }


    @Override
    protected void execute(CommandEvent event)
    {
        if (event.getArgs().isEmpty())
        {
            event.replyError("Must provide a valid query type and zipcode");
            return;
        }

        String[] arguments = event.getArgs().split(" ");
        if ( arguments.length < 2 )
        {
            event.replyError("Must provide a valid query type and zipcode");
            return;
        }
        if ( !arguments[1].matches(zipRegex) )
        {
            event.replyError("Zip Code may only contain numeric values");
            return;
        }
        String unit = "F"; //default value
        if ( arguments.length > 2 )
        {
            switch ( arguments[2].toUpperCase() )
            {
                case "C":
                    unit = "C";
                    break;
                case "F":
                    unit =  "F";
                    break;
                case "CELSIUS":
                    unit = "C";
                    break;
                case "FAHRENHIET":
                    unit = "F";
                    break;
                default: // sort of redundant
                    unit = "F";
                    break;
            }
        }
        try {
            Integer zipCode = Integer.parseInt(arguments[1]);
            switch (arguments[0].toUpperCase()) {
                case "NOW":
                    event.reply(sia.getServiceManagers().getOpenWeatherService().getWeatherEmbedByZipCode(zipCode, unit));
                    break;
                case "FORECAST":
                    event.reply(sia.getServiceManagers().getOpenWeatherService().getWeatherForecastEmbedByZipCode(zipCode, unit));
                    break;
                default:
                    event.replyError("Must provide an argument of \"Now\" or \"Forecast\" ");
            }
        } catch (NoWeatherException e) {
            log.error("Error retrieving weather: {}", e.getMessage());
            event.replyError("Encountered and error retrieving the weather. Please try again later.");
        }
    }
}
