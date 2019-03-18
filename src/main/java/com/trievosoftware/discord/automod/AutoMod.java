/*
 * Copyright 2016 John Grosh (jagrosh).
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
package com.trievosoftware.discord.automod;

import com.trievosoftware.discord.Constants;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.logging.MessageCache.CachedMessage;
import com.trievosoftware.discord.utils.FixedCache;
import com.trievosoftware.discord.utils.OtherUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Guild.VerificationLevel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class AutoMod
{
    private static final Pattern INVITES = Pattern.compile("discord\\s?(?:(?:\\.|dot|\\(\\.\\)|\\(dot\\))\\s?gg|app\\s?\\.\\s?com\\s?\\/\\s?invite)\\s?\\/\\s?([A-Z0-9-]{2,18})",
            Pattern.CASE_INSENSITIVE);
    
    
    private static final Pattern REF = Pattern.compile("https?:\\/\\/\\S+(?:\\/ref\\/|[?&#]ref(?:errer|erral)?=)\\S+", Pattern.CASE_INSENSITIVE);
    private static final Pattern BASE_URL = Pattern.compile("https?:\\/\\/(?:[^?&:\\/\\s]+\\.)?([^?&:\\/\\s]+\\.\\w+)(?:\\W|$)", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern LINK       = Pattern.compile("https?:\\/\\/\\S+", Pattern.CASE_INSENSITIVE);
    private static final String INVITE_LINK = "https?:\\/\\/discord(?:app\\.com\\/invite|\\.gg)\\/(\\S+)";
    
    private static final String CONDENSER = "(.+?)\\s*(\\1\\s*)+";
    private static final Logger LOG = LoggerFactory.getLogger("AutoMod");
    public  static final String RESTORE_MUTE_ROLE_AUDIT = "Restoring Muted Role";
    
    private final Sia sia;
    
    private String[] refLinkList;
//    private final URLResolver urlResolver;
    private final InviteResolver inviteResolver = new InviteResolver();
    private final CopypastaResolver copypastaResolver = new CopypastaResolver();
    private final FixedCache<String,DupeStatus> spams = new FixedCache<>(3000);
    private final HashMap<Long,OffsetDateTime> latestGuildJoin = new HashMap<>();
    
    public AutoMod(Sia sia)
    {
        this.sia = sia;
//        urlResolver = new URLResolver(config.get(10), config.get(11));
        loadCopypastas();
        loadReferralDomains();
    }
    
    public final void loadCopypastas()
    {
        this.copypastaResolver.load();
    }
    
//    public final void loadSafeDomains()
//    {
//        this.urlResolver.loadSafeDomains();
//    }
    
    public final void loadReferralDomains()
    {
        this.refLinkList = OtherUtil.readLines("referral_domains");
    }
    
    public void enableRaidMode(Guild guild, Member moderator, OffsetDateTime now, String reason)
    {
        sia.getServiceManagers().getGuildSettingsService().enableRaidMode(guild);
        if(guild.getVerificationLevel().getKey()<VerificationLevel.HIGH.getKey())
        {
            try
            {
                guild.getManager().setVerificationLevel(VerificationLevel.HIGH).reason("Enabling Anti-Raid Mode").queue();
            } catch(PermissionException ignore)
            {
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        guild.getName(), guild.getIdLong(), ignore.getMessage()));
            }
        }
        sia.getModLogger().postRaidmodeCase(moderator, now, true, reason);
    }
    
    public void disableRaidMode(Guild guild, Member moderator, OffsetDateTime now, String reason)
    {
        VerificationLevel last = sia.getServiceManagers().getGuildSettingsService().disableRaidMode(guild);
        if(guild.getVerificationLevel()!=last)
        {
            try
            {
                guild.getManager().setVerificationLevel(last).reason("Disabling Anti-Raid Mode").queue();
            } catch(PermissionException ignore)
            {
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        guild.getName(), guild.getIdLong(), ignore.getMessage()));
            }
        }
        sia.getModLogger().postRaidmodeCase(moderator, now, false, reason);
    }
    
    public void memberJoin(GuildMemberJoinEvent event)
    {
        // completely ignore bots for raidmode
        if(event.getMember().getUser().isBot())
            return;
        
        boolean inRaidMode = sia.getServiceManagers().getGuildSettingsService().getSettings(event.getGuild()).isInRaidMode();
        com.trievosoftware.application.domain.AutoMod ams = sia.getServiceManagers().getAutoModService().getSettings(event.getGuild());
        OffsetDateTime now = OffsetDateTime.now();
        boolean kicking = false;
        
        // if we're in raid mode...
        if(inRaidMode)
        {
            // ...and this server uses auto raid mode, check if we should be turning it off automatically
            // this means that we should turn it off if the latest attempted join was more than 2 minutes ago
            if(ams.useAutoRaidMode() 
                    && latestGuildJoin.containsKey(event.getGuild().getIdLong()) 
                    && latestGuildJoin.get(event.getGuild().getIdLong()).until(now, ChronoUnit.SECONDS)>120)
            {
                disableRaidMode(event.getGuild(), event.getGuild().getSelfMember(), now, "No recent join attempts");
            }
            // otherwise, boot 'em
            else if(event.getGuild().getSelfMember().hasPermission(Permission.KICK_MEMBERS))
            {
                kicking = true;
            }
        }
        
        // now, if we're not in raid mode, and auto mode is enabled
        else if(ams.useAutoRaidMode())
        {
            // find the time that we should be looking after, and count the number of people that joined after that
            OffsetDateTime min = event.getMember().getJoinDate().minusSeconds(ams.getRaidModeTime());
            long recent = event.getGuild().getMemberCache().stream().filter(m -> !m.getUser().isBot() && m.getJoinDate().isAfter(min)).count();
            if(recent>=ams.getRaidModeNumber())
            {
                enableRaidMode(event.getGuild(), event.getGuild().getSelfMember(), now,
                    "Maximum join rate exceeded ("+ams.getRaidModeNumber()+"/"+ams.getRaidModeTime()+"s)");
                kicking = true;
            }
        }
        
        if(kicking)
        {
            OtherUtil.safeDM(event.getUser(), "Sorry, **"+event.getGuild().getName()+"** is currently under lockdown. "
                    + "Please try joining again later. Sorry for the inconvenience.", true, () -> 
                    {
                        try
                        {
                            event.getGuild().getController().kick(event.getMember(), "Anti-Raid Mode").queue();
                        }catch(Exception ignore){}
                    });
        }
        else
        {
            if(sia.getServiceManagers().getTempMutesService().isMuted(event.getMember()))
            {
                try
                {
                    event.getGuild().getController()
                            .addSingleRoleToMember(event.getMember(), sia.getServiceManagers().getGuildSettingsService()
                                .getSettings(event.getGuild()).getMutedRole(event.getGuild()))
                            .reason(RESTORE_MUTE_ROLE_AUDIT).queue();
                } catch(Exception ignore)
                {
                    if ( sia.isDebugMode() )
                        sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                            event.getGuild().getName(), event.getGuild().getIdLong(), ignore.getMessage()));
                }
            }
            dehoist(event.getMember());
        }
        
        latestGuildJoin.put(event.getGuild().getIdLong(), now);
    }
    
    
    private boolean shouldPerformAutomod(Member member, TextChannel channel)
    {
        // ignore users not in the guild
        if(member==null || member.getGuild()==null)
            return false;
        
        // ignore broken guilds
        if(member.getGuild().getSelfMember()==null || member.getGuild().getOwner()==null)
            return false;
        
        // ignore bots
        if(member.getUser().isBot())
            return false;
        
        // ignore users sia cant interact with
        if(!member.getGuild().getSelfMember().canInteract(member))
            return false;
        
        // ignore users that can kick
        if(member.hasPermission(Permission.KICK_MEMBERS))
            return false;
        
        // ignore users that can ban
        if(member.hasPermission(Permission.BAN_MEMBERS))
            return false;
        
        // ignore users that can manage server
        if(member.hasPermission(Permission.MANAGE_SERVER))
            return false;
        
        // if a channel is specified, ignore users that can manage messages in that channel
        if(channel!=null && (member.hasPermission(channel, Permission.MESSAGE_MANAGE) || sia.getServiceManagers().getIgnoredService().isIgnored(channel)))
            return false;

        return !sia.getServiceManagers().getIgnoredService().isIgnored(member);
    }
    
    public void dehoist(Member member)
    {
        if(!member.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_MANAGE))
            return;
        
        if(!shouldPerformAutomod(member, null))
            return;
        
        com.trievosoftware.application.domain.AutoMod settings = sia.getServiceManagers().getAutoModService().getSettings(member.getGuild());
        if(settings==null || settings.getDehoistChar()==(char)0 || member.getEffectiveName().charAt(0)>settings.getDehoistChar())
            return;
        
        try
        {
            OtherUtil.dehoist(member, (char) settings.getDehoistChar().intValue() );
        }
        catch(Exception ignore)
        {
            if ( sia.isDebugMode() )
                sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                    member.getGuild().getName(), member.getGuild().getIdLong(), ignore.getMessage()));
        }
    }
    
    public void performAutomod(Message message) 
    {
        //ignore users with Manage Messages, Kick Members, Ban Members, Manage Server, or anyone the bot can't interact with
        if(!shouldPerformAutomod(message.getMember(), message.getTextChannel()))
            return;
        
        //get the settings
        com.trievosoftware.application.domain.AutoMod settings =
            sia.getServiceManagers().getAutoModService().getSettings(message.getGuild());
        if(settings==null)
            return;
        
        // check the channel for channel-specific settings
        boolean preventSpam = message.getTextChannel().getTopic()==null || !message.getTextChannel().getTopic().toLowerCase().contains("{spam}");
        boolean preventInvites = message.getTextChannel().getTopic()==null || !message.getTextChannel().getTopic().toLowerCase().contains("{invites}");
        
        boolean shouldDelete = false;
        String shouldChannelMute = null;
        int strikeTotal = 0;
        StringBuilder reason = new StringBuilder();
        
        // anti-duplicate
        if(settings.useAntiDuplicate() && preventSpam)
        {
            String key = message.getAuthor().getId()+"|"+message.getGuild().getId();
            String content = condensedContent(message);
            DupeStatus status = spams.get(key);
            if(status==null)
            {
                spams.put(key, new DupeStatus(content, latestTime(message)));
            }
            else
            {
                OffsetDateTime now = latestTime(message);
                int offenses = status.update(content, now);
                
                if(offenses==settings.getDupeDeleteThresh())
                {
                    shouldChannelMute = "Please stop spamming.";
                    purgeMessages(message.getGuild(), m -> m.getAuthorId()==message.getAuthor().getIdLong() && m.getCreationTime().plusMinutes(2).isAfter(now));
                }
                else if(offenses>settings.getDupeDeleteThresh())
                    shouldDelete = true;
                
                if(offenses >= settings.getDupeStrikesThresh())
                {
                    strikeTotal += settings.getDupeStrikes();
                    reason.append(", Duplicate messages");
                }
            }
        }
        
        // anti-mention (users)
        if(settings.getMaxMentions() >= com.trievosoftware.application.domain.AutoMod.MENTION_MINIMUM)
        {
            
            long mentions = message.getMentionedUsers().stream().filter(u -> !u.isBot() && !u.equals(message.getAuthor())).distinct().count();
            if(mentions > settings.getMaxMentions())
            {
                strikeTotal += (int)(mentions-settings.getMaxMentions());
                reason.append(", Mentioning ").append(mentions).append(" users");
                shouldDelete = true;
            }
        }
        
        // max newlines
        if(settings.getMaxLines() >0 && preventSpam)
        {
            int count = message.getContentRaw().split("\n").length;
            if(count > settings.getMaxLines())
            {
                strikeTotal += Math.ceil((double)(count-settings.getMaxLines())/settings.getMaxLines());
                reason.append(", Message contained ").append(count).append(" newlines");
                shouldDelete = true;
            }
        }
        
        // anti-mention (roles)
        if(settings.getMaxRoleMentions() >= com.trievosoftware.application.domain.AutoMod.ROLE_MENTION_MINIMUM)
        {
            long mentions = message.getMentionedRoles().stream().distinct().count();
            if(mentions > settings.getMaxRoleMentions())
            {
                strikeTotal += (int)(mentions-settings.getMaxRoleMentions());
                reason.append(", Mentioning ").append(mentions).append(" roles");
                shouldDelete = true;
            }
        }
        
        // prevent referral links
        if(settings.getRefStrikes() > 0)
        {
            Matcher m = REF.matcher(message.getContentRaw());
            if(m.find())
            {
                strikeTotal += settings.getRefStrikes();
                reason.append(", Referral link");
                shouldDelete = true;
            }
            else
            {
                m = BASE_URL.matcher(message.getContentRaw().toLowerCase());
                while(m.find())
                {
                    if(isReferralUrl(m.group(1)))
                    {
                        strikeTotal += settings.getRefStrikes();
                        reason.append(", Referral link");
                        shouldDelete = true;
                        break;
                    }
                }
            }
        }
        
        // prevent copypastas
        if(settings.getCopyPastaStrikes() > 0 && preventSpam)
        {
            String copypastaName = copypastaResolver.getCopypasta(message.getContentRaw());
            if(copypastaName!=null)
            {
                strikeTotal += settings.getCopyPastaStrikes();
                reason.append(", ").append(copypastaName).append(" copypasta");
                shouldDelete = true;
            }
        }
        
        if(settings.getEveryoneStrikes() > 0 && preventSpam && !message.getMember().hasPermission(message.getTextChannel(), Permission.MESSAGE_MENTION_EVERYONE))
        {
            String filtered = message.getContentRaw().replace("`@everyone`", "").replace("`@here`", "");
            if(filtered.contains("@everyone") || filtered.contains("@here")
                    || message.getMentionedRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("everyone") || role.getName().equalsIgnoreCase("here")))
            {
                strikeTotal += settings.getEveryoneStrikes();
                reason.append(", ").append("Attempted @\u0435veryone/here"); // cyrillic e
                shouldDelete = true;
            }
        }
        
        // anti-invite
        if(settings.getInviteStrikes() > 0 && preventInvites)
        {
            List<String> invites = new ArrayList<>();
            Matcher m = INVITES.matcher(message.getContentRaw());
            while(m.find())
                invites.add(m.group(1));
            LOG.trace("Found "+invites.size()+" invites.");
            for(String inviteCode : invites)
            {
                long gid = inviteResolver.resolve(message.getJDA(), inviteCode);
                if(gid != message.getGuild().getIdLong())
                {
                    strikeTotal += settings.getInviteStrikes();
                    reason.append(", Advertising");
                    shouldDelete = true;
                    break;
                }
            }
        }
        
        
        // delete the message if applicable
        if(shouldDelete)
        {
            try
            {
                message.delete().reason("Automod").queue(v->{}, f->{});
            }catch(PermissionException ignore)
            {
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        message.getGuild().getName(), message.getGuild().getIdLong(), ignore.getMessage()));
            }
        }
        
        // channel mute if applicable (prevent sending messages in that channel for a short time as a 'warning'
        if(shouldChannelMute!=null && message.getGuild().getSelfMember().hasPermission(message.getTextChannel(),
            Permission.MANAGE_PERMISSIONS, Permission.MESSAGE_WRITE))
        {
            message.getChannel().sendMessage(message.getAuthor().getAsMention() + Constants.WARNING + " " + shouldChannelMute).queue(m -> 
            {
                m.delete().queueAfter(1500, TimeUnit.MILLISECONDS, s->{}, f->{});
                try
                {
                    PermissionOverride po = message.getTextChannel().getPermissionOverride(message.getMember());
                    if(po==null)
                    {

                        message.getTextChannel().createPermissionOverride(message.getMember()).setDeny(Permission.MESSAGE_READ).queue(p -> 
                        {
                            try
                            {
                                p.delete().queueAfter(3, TimeUnit.SECONDS, s->{}, f->{});
                            } catch(Exception ignore)
                            {
                                if ( sia.isDebugMode() )
                                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                                        message.getGuild().getName(), message.getGuild().getIdLong(), ignore.getMessage()));
                            }
                        }, f->{});
                    }
                    else
                    {
                        long existingDenied = po.getDeniedRaw();
                        po.getManager().deny(Permission.MESSAGE_READ).queue(su -> 
                        {
                            try
                            {
                                po.getManager().deny(existingDenied).queueAfter(3, TimeUnit.SECONDS, s->{}, f->{});
                            } catch(Exception ignore)
                            {
                                if ( sia.isDebugMode() )
                                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                                        message.getGuild().getName(), message.getGuild().getIdLong(), ignore.getMessage()));
                            }
                        }, f->{});
                    }
                } catch(Exception ignore) {}
            }, f->{});
        }
        
        // assign strikes if necessary
        if(strikeTotal>0)
        {
            sia.getStrikeHandler().applyStrikes(message.getGuild().getSelfMember(),
                    latestTime(message), message.getAuthor(), strikeTotal, reason.toString().substring(2));
        }
        
        // now, lets resolve links, but async
//        if(!shouldDelete && settings.resolveUrls && (settings.inviteStrikes>0 || settings.refStrikes>0))
//        {
//            List<String> links = new LinkedList<>();
//            Matcher m = LINK.matcher(message.getContentRaw());
//            while(m.find())
//                links.add(m.group().endsWith(">") ? m.group().substring(0,m.group().length()-1) : m.group());
//            if(!links.isEmpty())
//                sia.getThreadpool().execute(() ->
//                {
//                    boolean containsInvite = false;
//                    boolean containsRef = false;
//                    String llink = null;
//                    List<String> redirects = null;
//                    for(String link: links)
//                    {
//                        llink = link;
//                        redirects = urlResolver.findRedirects(link);
//                        for(String resolved: redirects)
//                        {
//                            if(settings.inviteStrikes>0 && resolved.matches(INVITE_LINK))
//                            {
//                                if(inviteResolver.resolve(message.getJDA(), resolved.replaceAll(INVITE_LINK, "$1")) != message.getGuild().getIdLong())
//                                    containsInvite = true;
//                            }
//                            if(settings.refStrikes>0)
//                            {
//                                if(resolved.matches(REF.pattern()) || isReferralUrl(resolved.replaceAll(BASE_URL.pattern(), "$1")))
//                                    containsRef = true;
//                            }
//
//                        }
//                        if((containsInvite || settings.inviteStrikes<1) && (containsRef || settings.refStrikes<1))
//                            break;
//                    }
//                    int rstrikeTotal = (containsInvite ? settings.inviteStrikes : 0) + (containsRef ? settings.refStrikes : 0);
//                    if(rstrikeTotal > 0)
//                    {
//                        sia.getBasicLogger().logRedirectPath(message, llink, redirects);
//                        String rreason = ((containsInvite ? ", Advertising (Resolved Link)" : "") + (containsRef ? ", Referral Link (Resolved Link)" : "")).substring(2);
//                        try
//                        {
//                            message.delete().reason("Automod").queue(v->{}, f->{});
//                        }catch(PermissionException ignore){}
//                        sia.getStrikeHandler().applyStrikes(message.getGuild().getSelfMember(),
//                            latestTime(message), message.getAuthor(), rstrikeTotal, rreason);
//                    }
//                });
//        }
    }
    
    private void purgeMessages(Guild guild, Predicate<CachedMessage> predicate)
    {
        sia.getMessageCache().getMessages(guild, predicate).forEach(m ->
        {
            try
            {
                TextChannel mtc = m.getTextChannel(guild);
                if(mtc!=null)
                    mtc.deleteMessageById(m.getIdLong()).queue(s->{}, f->{});
            }
            catch(PermissionException ignore)
            {
                if ( sia.isDebugMode() )
                    sia.getLogWebhook().send(String.format("Exception encountered in GUILD=%s/%d. %s",
                        guild.getName(), guild.getIdLong(), ignore.getMessage()));
            }
        });
    }
    
    private boolean isReferralUrl(String url)
    {
        for(String reflink: refLinkList)
            if(reflink.equalsIgnoreCase(url))
                return true;
        return false;
    }
    
    private static String condensedContent(Message m)
    {
        StringBuilder sb = new StringBuilder(m.getContentRaw());
        m.getAttachments().forEach(at -> sb.append("\n").append(at.getFileName()));
        try
        {
            return sb.toString().trim().replaceAll(CONDENSER, "$1");
        }
        catch(Exception ex)
        {
            return sb.toString().trim();
        }
    }
    
    private static OffsetDateTime latestTime(Message m)
    {
        return m.isEdited() ? m.getEditedTime() : m.getCreationTime();
    }
    
    private class DupeStatus
    {
        private String content;
        private OffsetDateTime time;
        private int count;
        
        private DupeStatus(String content, OffsetDateTime time)
        {
            this.content = content;
            this.time = time;
            count = 0;
        }
        
        private int update(String nextcontent, OffsetDateTime nexttime)
        {
            if(nextcontent.equals(content) && this.time.plusSeconds(30).isAfter(nexttime))
            {
                count++;
                this.time = nexttime;
                return count;
            }
            else
            {
                this.content = nextcontent;
                this.time = nexttime;
                count = 0;
                return count;
            }
        }
    }
}
