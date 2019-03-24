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
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;
import me.joshmcfarlin.cryptocompareapi.CryptoCompareAPI;
import me.joshmcfarlin.cryptocompareapi.utils.IntervalTypes;
import me.joshmcfarlin.cryptocompareapi.utils.RateLimiting;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class CryptoAdminCommand extends AbstractGenericCommand {

    private final Logger log = LoggerFactory.getLogger(CryptoAdminCommand.class);
    private final static String LINESTART = "\u25AB"; // ▫

    private CryptoCompareAPI cryptoCompare;

    public CryptoAdminCommand(Sia sia){
        super(sia);
        this.name = "cryptoadmin";
        this.aliases = new String[]{"ca"};
        this.help = "Show CryptoCompare API information in debug";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.hidden = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.cryptoCompare = new CryptoCompareAPI(sia.getApplicationProperties().getApi().getCryptoCompare());
    }

    @Override
    public void doCommand(CommandEvent event) {
        log.debug("User={} requesting Debug information on CryptoCompare API", event.getAuthor().getName());
        try {
            RateLimiting.TotalRate rates = RateLimiting.getRates();
            RateLimiting.TotalRate.RateData data = rates.getData();
            RateLimiting.TotalRate.RateData.RatePerInterval callsLeft = data.getCallsLeft();
            RateLimiting.TotalRate.RateData.RatePerInterval callsMade = data.getCallsMade();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                .append("\n**Have service:** ").append(RateLimiting.callable()).append("\n\n")
                .append("**Calls Made**\n")
                    .append(LINESTART).append("Second: ").append(callsMade.getInterval(IntervalTypes.SECOND)).append("\n")
                    .append(LINESTART).append("Minute: ").append(callsMade.getInterval(IntervalTypes.MINUTE)).append("\n")
                    .append(LINESTART).append("Hour: ").append(callsMade.getInterval(IntervalTypes.HOUR))
                .append("\n\n")
                .append("**Calls Left**\n")
                    .append(LINESTART).append("Second: ").append(callsLeft.getInterval(IntervalTypes.SECOND)).append("\n")
                    .append(LINESTART).append("Minute: ").append(callsLeft.getInterval(IntervalTypes.MINUTE)).append("\n")
                    .append(LINESTART).append("Hour: ").append(callsLeft.getInterval(IntervalTypes.HOUR)).append("\n\n");

            embedBuilder.setDescription(stringBuilder.toString());
            embedBuilder.setTitle("CryptoCompare API Stats");
            embedBuilder.setColor(Color.CYAN);

            event.reply(embedBuilder.build());

            log.error("Calls Left: {}\nCalls Made: {}\n", callsLeft.getInterval(IntervalTypes.HOUR), callsMade.getInterval(IntervalTypes.HOUR));
        } catch (IOException e) {
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    event.getGuild().getName(), event.getGuild().getIdLong(), e.getMessage()));
            log.error("Error getting CryptoCompare API information. {}", e.getMessage());
            event.reply("Sorry, I experienced an error attempting to query for data. Please check logs for more info");
        }
    }
}
