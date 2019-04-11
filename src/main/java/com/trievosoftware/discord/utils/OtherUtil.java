/*
 * Copyright 2018 Mark Tripoli (mark.tripoli@trievosoftware.com).
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
package com.trievosoftware.discord.utils;

import com.trievosoftware.application.exceptions.InvalidTimeUnitException;
import com.trievosoftware.application.exceptions.NonTimeInputException;
import com.trievosoftware.application.exceptions.StringNotIntegerException;
import com.trievosoftware.discord.Sia;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 * @author Mark Tripoli (mark.tripoli@trievosoftware.com)
 */
public class OtherUtil
{
    private final static Logger LOG = LoggerFactory.getLogger(OtherUtil.class);
    
    public final static char[] DEHOIST_ORIGINAL =     {'!',      '"',      '#',      '$',      '%',      
        '&',      '\'',     '(',      ')',      '*',      '+',      ',',      '-',      '.',      '/'};
    public final static char[] DEHOIST_REPLACEMENTS = {'\u01C3', '\u201C', '\u2D4C', '\uFF04', '\u2105',     // visually
        '\u214B', '\u2018', '\u2768', '\u2769', '\u2217', '\u2722', '\u201A', '\u2013', '\uFBB3', '\u2044'}; // similar
    public final static String DEHOIST_JOINED = "`"+ FormatUtil.join("`, `", DEHOIST_ORIGINAL)+"`";
    
    public static boolean dehoist(Member m, char symbol)
    {
        if(!m.getGuild().getSelfMember().canInteract(m))
            return false;
        if(m.getEffectiveName().charAt(0)>symbol)
            return false;
        
        String newname = m.getEffectiveName();
        for(int i=0; i<DEHOIST_ORIGINAL.length; i++)
        {
            if(DEHOIST_ORIGINAL[i] == newname.charAt(0))
            {
                newname = DEHOIST_REPLACEMENTS[i] + (newname.length() == 1 ? "" : newname.substring(1));
                break;
            }
        }
        m.getGuild().getController().setNickname(m, newname).reason("Dehoisting").queue();
        return true;
    }
    
    public static void safeDM(User user, String message, boolean shouldDM, Runnable then)
    {
        if(user==null || !shouldDM)
            then.run();
        else try
        {
            user.openPrivateChannel()
                    .queue(pc -> pc.sendMessage(message).queue(s->then.run(), 
                            f->then.run()), f->then.run());
        }
        catch(Exception ignore) {}
    }
    
    public static Member findMember(String username, String discriminator, Guild guild)
    {
        return guild.getMembers().stream().filter(m -> m.getUser().getName().equals(username) && m.getUser().getDiscriminator().equals(discriminator)).findAny().orElse(null);
    }
    
    public static int parseTime(String timestr)
    {
        timestr = timestr.replaceAll("(?i)(\\s|,|and)","")
                .replaceAll("(?is)(-?\\d+|[a-z]+)", "$1 ")
                .trim();
        String[] vals = timestr.split("\\s+");
        int timeinseconds = 0;
        try
        {
            for(int j=0; j<vals.length; j+=2)
            {
                int num = Integer.parseInt(vals[j]);
                if(vals[j+1].toLowerCase().startsWith("m"))
                    num*=60;
                else if(vals[j+1].toLowerCase().startsWith("h"))
                    num*=60*60;
                else if(vals[j+1].toLowerCase().startsWith("d"))
                    num*=60*60*24;
                timeinseconds+=num;
            }
        }
        catch(Exception ex)
        {
            return 0;
        }
        return timeinseconds;
    }
    
    public static String[] readLines(String filename)
    {
        try
        {
            String resourceLLoc = OtherUtil.class.getClassLoader().getResource("lists" + File.separator + filename + ".txt").getPath();
            List<String> values = Files.readAllLines(Paths.get(resourceLLoc)).stream()
                    .map(str -> str.replace("\uFEFF", "").trim()).filter(str -> !str.isEmpty() && !str.startsWith("//")).collect(Collectors.toList());
            String[] list = new String[values.size()];
            for(int i=0; i<list.length; i++)
                list[i] = values.get(i);
            LOG.info("Successfully read "+list.length+" entries from '"+filename+"'");
            return list;
        }
        catch(NoSuchFileException | NullPointerException ex)
        {

            LOG.error("File does not exists: '"+filename+"':"+ ex);
            return new String[0];
        } catch (IOException e) {
            LOG.error("Failed to read: '"+filename+"':"+ e);
            return new String[0];
        }
    }

    public static List<Role> roleIdToRole(Sia sia, Guild guild, String[] roles)
    {
        List<Role> roleList = new ArrayList<>();

        for ( String roleId : roles )
        {
            if (!roleId.equalsIgnoreCase("@everyone"))
                roleList.add(sia.getJDA(guild.getIdLong()).getRoleById(roleId));
        }
        return roleList;
    }

    @SuppressWarnings("Duplicates")
    public static Instant getTime(String time)
        throws StringNotIntegerException, InvalidTimeUnitException, NonTimeInputException
    {
        int timeLen;
        try {
            timeLen = FormatUtil.getMinutes(time);
            if (timeLen <= 0)
                throw new NonTimeInputException("Time may not be `0` or a `Negative` number");
        } catch (StringNotIntegerException e) {
            LOG.error("{}", e.getMessage());
            throw new StringNotIntegerException(e.getMessage());
        }

        String timeType = time.substring(time.length() - 1);
        switch (timeType) {
            case "S":
                return Instant.now().plus(timeLen, ChronoUnit.SECONDS);
            case "M":
                return Instant.now().plus(timeLen, ChronoUnit.MINUTES);
            case "H":
                return Instant.now().plus(timeLen, ChronoUnit.HOURS);
            case "D":
                return Instant.now().plus(timeLen, ChronoUnit.DAYS);
            default:
                throw new InvalidTimeUnitException("`" + timeType + "`: Is an invalid Time Type. " +
                    "Please only use `S (seconds), M (minutes), H (hours), D (days)`");
        }
    }

    public static String formatDateTime ( Guild guild, Instant time )
    {
        Pair<Locale, ZoneId> localTime = getLocale(guild.getRegion().getName());

        DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( localTime.getKey() )
                .withZone( localTime.getValue() );

        return formatter.format(time) + " " + localTime.getValue().getId();
    }

    public static Pair<Locale, ZoneId> getLocale(String discordRegion)
    {
        Locale local;
        ZoneId dateTime;

        switch (discordRegion.toUpperCase())
        {
            case "AMSTERDAM":
                local = Locale.GERMANY;
                dateTime = ZoneId.of("Europe/Paris");
                break;
            case "EU CENTRAL":
                local = Locale.GERMANY;
                dateTime = ZoneId.of("Europe/Paris");
                break;
            case "EU WEST":
                local = Locale.GERMANY;
                dateTime = ZoneId.of("Europe/Paris");
                break;
            case "FRANKFURT":
                local = Locale.GERMANY;
                dateTime = ZoneId.of("Europe/Paris");
                break;
            case "HONG KONG":
                local = Locale.CHINA;
                dateTime = ZoneId.of("Asia/Shanghai");
                break;
            case "JAPAN":
                local = Locale.JAPAN;
                dateTime = ZoneId.of("Asia/Tokyo");
                break;
            case "LONDON":
                local = Locale.UK;
                dateTime = ZoneId.of("Europe/Paris");
                break;
            default:
                local = Locale.US;
                dateTime = ZoneId.of("America/New_York");
        }
        return new Pair<Locale, ZoneId>(local, dateTime);
    }
}
