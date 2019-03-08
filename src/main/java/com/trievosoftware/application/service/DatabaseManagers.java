package com.trievosoftware.application.service;

import com.trievosoftware.application.service.impl.GuildSettingsServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DatabaseManagers {
    private ActionsService actionsService;
    private AuditCacheService auditCacheService;
    private AutoModService autoModService;
    private GuildSettingsService guildSettingsService;
    private IgnoredService ignoredService;
    private PremiumService premiumService;
    private StrikesService strikesService;
    private TempBansService tempBansService;
    private TempMutesService tempMutesService;

    private GuildSettingsServiceImpl guildSettingsServiceImpl;

    public DatabaseManagers(ActionsService actionsService, AuditCacheService auditCacheService,
                            AutoModService autoModService, GuildSettingsService guildSettingsService,
                            IgnoredService ignoredService, PremiumService premiumService, StrikesService strikesService,
                            TempBansService tempBansService, TempMutesService tempMutesService,
                            GuildSettingsServiceImpl guildSettingsServiceImpl) {
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
}
