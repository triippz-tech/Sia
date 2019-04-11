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

package com.trievosoftware.application.service;

import com.trievosoftware.application.service.impl.GuildSettingsServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ServiceManagers {
    private ActionsService actionsService;
    private AuditCacheService auditCacheService;
    private AutoModService autoModService;
    private GuildSettingsService guildSettingsService;
    private IgnoredService ignoredService;
    private PremiumService premiumService;
    private StrikesService strikesService;
    private TempBansService tempBansService;
    private TempMutesService tempMutesService;
    private NasaService nasaService;
    private OpenWeatherService openWeatherService;
    private GuildMusicSettingsService guildMusicSettingsService;
    private PlaylistService playlistService;
    private SongService songService;
    private PollService pollService;
    private PollItemsService pollItemsService;
    private DiscordUserService discordUserService;
    private WelcomeMessageService welcomeMessageService;
    private GuildRolesService guildRolesService;
    private CustomCommandService customCommandService;
    private IEXService iexService;
    private GuildEventService guildEventService;

    private GuildSettingsServiceImpl guildSettingsServiceImpl;

    public ServiceManagers(ActionsService actionsService, AuditCacheService auditCacheService,
                           AutoModService autoModService, GuildSettingsService guildSettingsService,
                           IgnoredService ignoredService, PremiumService premiumService, StrikesService strikesService,
                           TempBansService tempBansService, TempMutesService tempMutesService,
                           GuildSettingsServiceImpl guildSettingsServiceImpl, NasaService nasaService,
                           OpenWeatherService openWeatherService, GuildMusicSettingsService guildMusicSettingsService,
                           PlaylistService playlistService, SongService songService, PollService pollService,
                           PollItemsService pollItemsService, DiscordUserService discordUserService,
                           WelcomeMessageService welcomeMessageService, GuildRolesService guildRolesService,
                           CustomCommandService customCommandService, IEXService iexService, GuildEventService guildEventService) {
        this.actionsService = actionsService;
        this.auditCacheService = auditCacheService;
        this.autoModService = autoModService;
        this.guildSettingsService = guildSettingsService;
        this.ignoredService = ignoredService;
        this.premiumService = premiumService;
        this.strikesService = strikesService;
        this.tempBansService = tempBansService;
        this.tempMutesService = tempMutesService;
        this.guildSettingsServiceImpl = guildSettingsServiceImpl;
        this.nasaService = nasaService;
        this.openWeatherService = openWeatherService;
        this.guildMusicSettingsService = guildMusicSettingsService;
        this.playlistService = playlistService;
        this.songService = songService;
        this.pollService = pollService;
        this.pollItemsService = pollItemsService;
        this.discordUserService = discordUserService;
        this.welcomeMessageService = welcomeMessageService;
        this.guildRolesService = guildRolesService;
        this.customCommandService = customCommandService;
        this.iexService = iexService;
        this.guildEventService = guildEventService;
    }

    public ActionsService getActionsService() {
        return actionsService;
    }

    public AuditCacheService getAuditCacheService() {
        return auditCacheService;
    }

    public AutoModService getAutoModService() {
        return autoModService;
    }

    public GuildSettingsService getGuildSettingsService() {
        return guildSettingsService;
    }

    public IgnoredService getIgnoredService() {
        return ignoredService;
    }

    public PremiumService getPremiumService() {
        return premiumService;
    }

    public StrikesService getStrikesService() {
        return strikesService;
    }

    public TempBansService getTempBansService() {
        return tempBansService;
    }

    public TempMutesService getTempMutesService() {
        return tempMutesService;
    }

    public GuildSettingsServiceImpl getGuildSettingsServiceImpl() {
        return guildSettingsServiceImpl;
    }

    public NasaService getNasaService() {
        return nasaService;
    }

    public OpenWeatherService getOpenWeatherService() { return openWeatherService; }

    public GuildMusicSettingsService getGuildMusicSettingsService() {
        return guildMusicSettingsService;
    }

    public PlaylistService getPlaylistService() {
        return playlistService;
    }

    public SongService getSongService() {
        return songService;
    }

    public PollService getPollService() { return pollService; }

    public PollItemsService getPollItemsService() { return pollItemsService; }

    public DiscordUserService getDiscordUserService() { return discordUserService; }

    public  WelcomeMessageService getWelcomeMessageService() { return welcomeMessageService; }

    public CustomCommandService getCustomCommandService() { return customCommandService; }

    public GuildRolesService guildRolesService() { return guildRolesService; }

    public IEXService getIexService() { return iexService; }

    public GuildEventService getGuildEventService() {
        return guildEventService;
    }
}
