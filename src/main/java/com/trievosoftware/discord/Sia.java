/*
 * Copyright 2019 Mark Tripoli (triippz).
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
package com.trievosoftware.discord;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.*;
import com.trievosoftware.application.config.ApplicationProperties;
import com.trievosoftware.application.service.ServiceManagers;
import com.trievosoftware.discord.automod.AutoMod;
import com.trievosoftware.discord.automod.StrikeHandler;
import com.trievosoftware.discord.commands.CommandExceptionListener;
import com.trievosoftware.discord.commands.automod.*;
import com.trievosoftware.discord.commands.entertainment.GiphyCommand;
import com.trievosoftware.discord.commands.general.*;
import com.trievosoftware.discord.commands.informational.crypto.CryptoAdminCommand;
import com.trievosoftware.discord.commands.informational.crypto.CryptoCoinCommand;
import com.trievosoftware.discord.commands.informational.crypto.CryptoInfoCommand;
import com.trievosoftware.discord.commands.informational.crypto.CryptoNewsCommand;
import com.trievosoftware.discord.commands.informational.weather.WeatherCommand;
import com.trievosoftware.discord.commands.moderation.*;
import com.trievosoftware.discord.commands.music.dj.*;
import com.trievosoftware.discord.commands.music.generic.*;
import com.trievosoftware.discord.commands.music.moderator.MusicSettingsCommand;
import com.trievosoftware.discord.commands.music.moderator.*;
import com.trievosoftware.discord.commands.owner.*;
import com.trievosoftware.discord.commands.settings.*;
import com.trievosoftware.discord.commands.tools.*;
import com.trievosoftware.discord.commands.tools.LookupCmd;
import com.trievosoftware.discord.logging.BasicLogger;
import com.trievosoftware.discord.logging.MessageCache;
import com.trievosoftware.discord.logging.ModLogger;
import com.trievosoftware.discord.logging.TextUploader;
import com.trievosoftware.discord.music.audio.NowplayingHandler;
import com.trievosoftware.discord.music.audio.PlayerManager;
import com.trievosoftware.discord.music.playlist.PlaylistLoader;
import com.trievosoftware.discord.utils.BlockingSessionController;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.utils.cache.CacheFlag;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Sia
{
    private final Logger log = LoggerFactory.getLogger(Sia.class);
    private boolean debugMode = false;

    private final EventWaiter waiter;
    private final ScheduledExecutorService threadpool;
    private final TextUploader uploader;
    private final ShardManager shards;
    private final ModLogger modlog;
    private final BasicLogger basiclog;
    private final MessageCache messages;
    private final WebhookClient logwebhook;
    private final AutoMod automod;
    private final StrikeHandler strikehandler;
    private final ServiceManagers serviceManagers;
    private final ApplicationProperties applicationProperties;
    private final PlayerManager players;
    private final PlaylistLoader playlists;
    private final NowplayingHandler nowplaying;


    public Sia(ServiceManagers databaseManagers, ApplicationProperties applicationProperties) throws Exception
    {
        this.serviceManagers = databaseManagers;
        this.applicationProperties = applicationProperties;
        this.playlists = new PlaylistLoader(this);
        this.players = new PlayerManager(this);
        this.nowplaying = new NowplayingHandler(this);

        waiter = new EventWaiter(Executors.newSingleThreadScheduledExecutor(), false);
        threadpool = Executors.newScheduledThreadPool(50);
        uploader = new TextUploader(this,
            Long.parseLong(this.applicationProperties.getDiscord().getGuildId()),
            Long.parseLong(this.applicationProperties.getDiscord().getCategoryId()));
        modlog = new ModLogger(this);
        basiclog = new BasicLogger(this);
        messages = new MessageCache();
        logwebhook = new WebhookClientBuilder(this.applicationProperties.getDiscord().getLogWebookUrl()).build();
        automod = new AutoMod(this);
        strikehandler = new StrikeHandler(this);

        this.players.init();
        this.nowplaying.init();

        CommandClient client = new CommandClientBuilder()
                        .setPrefix(this.applicationProperties.getDiscord().getPrefix())
                        //.setGame(Game.watching("Type "+Constants.PREFIX+"help"))
                        .setGame(Game.playing(Constants.Wiki.SHORT_WIKI))
                        .setOwnerId(Constants.OWNER_ID)
                        .setServerInvite(Constants.SERVER_INVITE)
                        .setEmojis(Constants.SUCCESS, Constants.WARNING, Constants.ERROR)
                        .setLinkedCacheSize(0)
                        .setGuildSettingsManager(databaseManagers.getGuildSettingsServiceImpl())
                        .setListener(new CommandExceptionListener())
                        .setScheduleExecutor(threadpool)
//                        .setShutdownAutomatically(false)
                        .addCommands(// General
                            //new AboutCommand(Color.CYAN, "and I'm here to keep your Discord server safe and make moderating easy!", 
                            //                            new String[]{"Moderation commands","Configurable automoderation","Very easy setup"},Constants.PERMISSIONS),
                            new AboutCmd(),
                            new InviteCmd(),
                            new PingCommand(),
                            new RoleinfoCmd(),
                            new ServerinfoCmd(),
                            new UserinfoCmd(),

                            // Moderation
                            new KickCmd(this),
                            new BanCmd(this),
                            new SoftbanCmd(this),
                            new UnbanCmd(this),
                            new CleanCmd(this),
                            new VoicemoveCmd(this),
                            new VoicekickCmd(this),
                            new MuteCmd(this),
                            new UnmuteCmd(this),
                            new RaidCmd(this),
                            new StrikeCmd(this),
                            new PardonCmd(this),
                            new CheckCmd(this),
                            new ReasonCmd(this),
                            new SetGameCommand(this),


                            // Settings
                            new SetupCmd(this),
                            new PunishmentCmd(this),
                            new MessagelogCmd(this),
                            new ModlogCmd(this),
                            new ServerlogCmd(this),
                            new VoicelogCmd(this),
                            new AvatarlogCmd(this),
                            new TimezoneCmd(this),
                            new ModroleCmd(this),
                            new PrefixCmd(this),
                            new SettingsCmd(this),

                            // Automoderation
                            new AntiinviteCmd(this),
                            new AnticopypastaCmd(this),
                            new AntieveryoneCmd(this),
                            new AntirefCmd(this),
                            new MaxlinesCmd(this),
                            new MaxmentionsCmd(this),
                            new AntiduplicateCmd(this),
                            new AutodehoistCmd(this),
                            new ResolvelinksCmd(this),
                            new AutoraidmodeCmd(this),
                            new IgnoreCmd(this),
                            new UnignoreCmd(this),
                            
                            // Tools
                            new AnnounceCmd(),
                            new AuditCmd(),
                            new DehoistCmd(),
                            new InvitepruneCmd(this),
                            new LookupCmd(this),
                            new EnableDebugCommand(this),

                            // Owner
                            new EvalCmd(this),
                            new DebugCmd(this),
                            new ReloadCmd(this),
                            new TransferCmd(this),
                            new SendGuildNotification(this),
                            new SetBotStatus(this),

                            // Informational
                            new CryptoCoinCommand(this),
                            new CryptoAdminCommand(this),
                            new CryptoNewsCommand(this),
                            new CryptoInfoCommand(this),
                            new WeatherCommand(this),

                            // Entertainment
                            new GiphyCommand(this),

                            // MUSIC Moderation
                            new MusicSettingsCommand(this),
                            new DjRoleCommand(this),
                            new GuildPlaylistCommand(this),
                            new SetMusicVcCommand(this),
                            new SetMusicTcCommand(this),
                            new SetupMusicCommand(this),

                            // MUSIC DJ Commands
                            new StopCommand(this),
                            new VolumeCommand(this),
                            new PauseCommand(this),
                            new SkipToCommand(this),
                            new ForceSkipCommand(this),
                            new PlayNextCommand(this, Constants.LOADING),
                            new RepeatCommand(this),

                            // MUSIC Generic
                            new UserPlaylistCommand(this, Constants.LOADING),
                            new NowPlayingCommand(this),
                            new UserPlayCommand(this, Constants.LOADING),
                            new QueueCommand(this),
                            new RemoveCommand(this),
                            new SearchCommand(this, Constants.SEARCHING),
                            new SCSearchCommand(this, Constants.SEARCHING),
                            new ShuffleCommand(this),
                            new LyricsCommand(this),
                            new SkipCommand(this)
                        )
                        .setHelpConsumer(event -> event.replyInDm(FormatUtil.formatHelp(event, this), m -> 
                        {
                            if(event.isFromType(ChannelType.TEXT))
                                try
                                {
                                    event.getMessage().addReaction(Constants.HELP_REACTION).queue(s->{}, f->{});
                                } catch(PermissionException ignore) {}
                        }, t -> event.replyWarning("Help cannot be sent because you are blocking Direct Messages.")))
                        .setDiscordBotsKey(this.applicationProperties.getDiscord().getToken())
                        //.setCarbonitexKey(tokens.get(2))
                        //.setDiscordBotListKey(tokens.get(3))
                        .build();
        shards = new DefaultShardManagerBuilder()
                .setShardsTotal(Integer.parseInt(this.applicationProperties.getDiscord().getShards()))
                .setToken(this.applicationProperties.getDiscord().getToken())
                .addEventListeners(new Listener(this), client, waiter)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing("loading..."))
                .setBulkDeleteSplittingEnabled(false)
                .setRequestTimeoutRetry(true)
                .setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.GAME)) //TODO: dont disable GAME
                .setSessionController(new BlockingSessionController())
                .build();
        
        modlog.start();
        
        threadpool.scheduleWithFixedDelay(this::cleanPremium, 0, 2, TimeUnit.HOURS);
        threadpool.scheduleWithFixedDelay(this::leavePointlessGuilds, 5, 30, TimeUnit.MINUTES);
        threadpool.scheduleWithFixedDelay(System::gc, 12, 6, TimeUnit.HOURS);
    }
    
    public EventWaiter getEventWaiter()
    {
        return waiter;
    }

    public ScheduledExecutorService getThreadpool()
    {
        return threadpool;
    }
    
    public TextUploader getTextUploader()
    {
        return uploader;
    }
    
    public ShardManager getShardManager()
    {
        return shards;
    }
    
    public ModLogger getModLogger()
    {
        return modlog;
    }
    
    public BasicLogger getBasicLogger()
    {
        return basiclog;
    }
    
    public MessageCache getMessageCache()
    {
        return messages;
    }
    
    public WebhookClient getLogWebhook()
    {
        return logwebhook;
    }
    
    public AutoMod getAutoMod()
    {
        return automod;
    }
    
    public StrikeHandler getStrikeHandler()
    {
        return strikehandler;
    }
    
    public void cleanPremium()
    {
        serviceManagers.getPremiumService().cleanPremiumList().forEach((gid) ->
        {
            serviceManagers.getAutoModService().setResolveUrls(gid, false);
            serviceManagers.getGuildSettingsService().setAvatarLogChannel(gid, null);
        });
    }
    
    public void leavePointlessGuilds()
    {
        shards.getGuilds().stream().filter(g -> 
        {
            if(!g.isAvailable())
                return false;
            int botcount = (int)g.getMemberCache().stream().filter(m -> m.getUser().isBot()).count();
            if(g.getMemberCache().size()-botcount<15 || (botcount>20 && ((double)botcount/g.getMemberCache().size())>0.65))
            {
                if(serviceManagers.getGuildSettingsService().hasSettings(g))
                    return false;
                if(serviceManagers.getAutoModService().hasSettings(g))
                    return false;
                return true;
            }
            return false;
        }).forEach(g -> g.leave().queue());
    }

    public ServiceManagers getServiceManagers() {
        return serviceManagers;
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    public void closeAudioConnection(long guildId)
    {
        Guild guild = shards.getGuildById(guildId);
        if(guild!=null)
            threadpool.submit(() -> guild.getAudioManager().closeAudioConnection());
    }

    public JDA getJDA(Long guildId) { return shards.getGuildById(guildId).getJDA(); }

    public PlayerManager getPlayerManager() {
        return players;
    }

    public PlaylistLoader getPlaylistLoader() {
        return playlists;
    }

    public NowplayingHandler getNowplayingHandler() {
        return nowplaying;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
