package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DiscordGuild.
 */
@Entity
@Table(name = "discord_guild")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiscordGuild implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @NotNull
    @Column(name = "guild_name", nullable = false)
    private String guildName;

    @Column(name = "invite_link")
    private String inviteLink;

    @OneToOne
    @JoinColumn(unique = true)
    private GuildSettings guildSettings;

    @OneToOne
    @JoinColumn(unique = true)
    private AuditCache auditCache;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoMod autoMod;

    @OneToOne
    @JoinColumn(unique = true)
    private GuildMusicSettings guildMusicSettings;

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DiscordUser> discordUsers = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ignored> ignoreds = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TempMutes> tempMutes = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TempBans> tempBans = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Poll> polls = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GuildRoles> guildRoles = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CustomCommand> customCommands = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Actions> actions = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Strikes> strikes = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WelcomeMessage> welcomeMessages = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GuildEvent> guildEvents = new HashSet<>();

    @OneToMany(mappedBy = "discordGuild")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GiveAway> giveAways = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public DiscordGuild(@NotNull Long guildId, @NotNull String guildName, String inviteLink) {
        this.guildId = guildId;
        this.guildName = guildName;
        this.inviteLink = inviteLink;
    }

    public DiscordGuild(@NotNull Long guildId, @NotNull String guildName) {
        this.guildId = guildId;
        this.guildName = guildName;
    }

    public DiscordGuild() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public DiscordGuild guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public DiscordGuild guildName(String guildName) {
        this.guildName = guildName;
        return this;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getInviteLink() {
        return inviteLink;
    }

    public DiscordGuild inviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
        return this;
    }

    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }

    public GuildSettings getGuildSettings() {
        return guildSettings;
    }

    public DiscordGuild guildSettings(GuildSettings guildSettings) {
        this.guildSettings = guildSettings;
        return this;
    }

    public void setGuildSettings(GuildSettings guildSettings) {
        this.guildSettings = guildSettings;
    }

    public AuditCache getAuditCache() {
        return auditCache;
    }

    public DiscordGuild auditCache(AuditCache auditCache) {
        this.auditCache = auditCache;
        return this;
    }

    public void setAuditCache(AuditCache auditCache) {
        this.auditCache = auditCache;
    }

    public AutoMod getAutoMod() {
        return autoMod;
    }

    public DiscordGuild autoMod(AutoMod autoMod) {
        this.autoMod = autoMod;
        return this;
    }

    public void setAutoMod(AutoMod autoMod) {
        this.autoMod = autoMod;
    }

    public GuildMusicSettings getGuildMusicSettings() {
        return guildMusicSettings;
    }

    public DiscordGuild guildMusicSettings(GuildMusicSettings guildMusicSettings) {
        this.guildMusicSettings = guildMusicSettings;
        return this;
    }

    public void setGuildMusicSettings(GuildMusicSettings guildMusicSettings) {
        this.guildMusicSettings = guildMusicSettings;
    }

    public Set<DiscordUser> getDiscordUsers() {
        return discordUsers;
    }

    public DiscordGuild discordUsers(Set<DiscordUser> discordUsers) {
        this.discordUsers = discordUsers;
        return this;
    }

    public DiscordGuild addDiscordUser(DiscordUser discordUser) {
        this.discordUsers.add(discordUser);
        discordUser.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeDiscordUser(DiscordUser discordUser) {
        this.discordUsers.remove(discordUser);
        discordUser.setDiscordGuild(null);
        return this;
    }

    public void setDiscordUsers(Set<DiscordUser> discordUsers) {
        this.discordUsers = discordUsers;
    }

    public Set<Ignored> getIgnoreds() {
        return ignoreds;
    }

    public DiscordGuild ignoreds(Set<Ignored> ignoreds) {
        this.ignoreds = ignoreds;
        return this;
    }

    public DiscordGuild addIgnored(Ignored ignored) {
        this.ignoreds.add(ignored);
        ignored.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeIgnored(Ignored ignored) {
        this.ignoreds.remove(ignored);
        ignored.setDiscordGuild(null);
        return this;
    }

    public void setIgnoreds(Set<Ignored> ignoreds) {
        this.ignoreds = ignoreds;
    }

    public Set<TempMutes> getTempMutes() {
        return tempMutes;
    }

    public DiscordGuild tempMutes(Set<TempMutes> tempMutes) {
        this.tempMutes = tempMutes;
        return this;
    }

    public DiscordGuild addTempMutes(TempMutes tempMutes) {
        this.tempMutes.add(tempMutes);
        tempMutes.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeTempMutes(TempMutes tempMutes) {
        this.tempMutes.remove(tempMutes);
        tempMutes.setDiscordGuild(null);
        return this;
    }

    public void setTempMutes(Set<TempMutes> tempMutes) {
        this.tempMutes = tempMutes;
    }

    public Set<TempBans> getTempBans() {
        return tempBans;
    }

    public DiscordGuild tempBans(Set<TempBans> tempBans) {
        this.tempBans = tempBans;
        return this;
    }

    public DiscordGuild addTempBans(TempBans tempBans) {
        this.tempBans.add(tempBans);
        tempBans.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeTempBans(TempBans tempBans) {
        this.tempBans.remove(tempBans);
        tempBans.setDiscordGuild(null);
        return this;
    }

    public void setTempBans(Set<TempBans> tempBans) {
        this.tempBans = tempBans;
    }

    public Set<Poll> getPolls() {
        return polls;
    }

    public DiscordGuild polls(Set<Poll> polls) {
        this.polls = polls;
        return this;
    }

    public DiscordGuild addPoll(Poll poll) {
        this.polls.add(poll);
        poll.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removePoll(Poll poll) {
        this.polls.remove(poll);
        poll.setDiscordGuild(null);
        return this;
    }

    public void setPolls(Set<Poll> polls) {
        this.polls = polls;
    }

    public Set<GuildRoles> getGuildRoles() {
        return guildRoles;
    }

    public DiscordGuild guildRoles(Set<GuildRoles> guildRoles) {
        this.guildRoles = guildRoles;
        return this;
    }

    public DiscordGuild addGuildRoles(GuildRoles guildRoles) {
        this.guildRoles.add(guildRoles);
        guildRoles.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeGuildRoles(GuildRoles guildRoles) {
        this.guildRoles.remove(guildRoles);
        guildRoles.setDiscordGuild(null);
        return this;
    }

    public void setGuildRoles(Set<GuildRoles> guildRoles) {
        this.guildRoles = guildRoles;
    }

    public Set<CustomCommand> getCustomCommands() {
        return customCommands;
    }

    public DiscordGuild customCommands(Set<CustomCommand> customCommands) {
        this.customCommands = customCommands;
        return this;
    }

    public DiscordGuild addCustomCommand(CustomCommand customCommand) {
        this.customCommands.add(customCommand);
        customCommand.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeCustomCommand(CustomCommand customCommand) {
        this.customCommands.remove(customCommand);
        customCommand.setDiscordGuild(null);
        return this;
    }

    public void setCustomCommands(Set<CustomCommand> customCommands) {
        this.customCommands = customCommands;
    }

    public Set<Actions> getActions() {
        return actions;
    }

    public DiscordGuild actions(Set<Actions> actions) {
        this.actions = actions;
        return this;
    }

    public DiscordGuild addActions(Actions actions) {
        this.actions.add(actions);
        actions.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeActions(Actions actions) {
        this.actions.remove(actions);
        actions.setDiscordGuild(null);
        return this;
    }

    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }

    public Set<Strikes> getStrikes() {
        return strikes;
    }

    public DiscordGuild strikes(Set<Strikes> strikes) {
        this.strikes = strikes;
        return this;
    }

    public DiscordGuild addStrikes(Strikes strikes) {
        this.strikes.add(strikes);
        strikes.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeStrikes(Strikes strikes) {
        this.strikes.remove(strikes);
        strikes.setDiscordGuild(null);
        return this;
    }

    public void setStrikes(Set<Strikes> strikes) {
        this.strikes = strikes;
    }

    public Set<WelcomeMessage> getWelcomeMessages() {
        return welcomeMessages;
    }

    public DiscordGuild welcomeMessages(Set<WelcomeMessage> welcomeMessages) {
        this.welcomeMessages = welcomeMessages;
        return this;
    }

    public DiscordGuild addWelcomeMessage(WelcomeMessage welcomeMessage) {
        this.welcomeMessages.add(welcomeMessage);
        welcomeMessage.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeWelcomeMessage(WelcomeMessage welcomeMessage) {
        this.welcomeMessages.remove(welcomeMessage);
        welcomeMessage.setDiscordGuild(null);
        return this;
    }

    public void setWelcomeMessages(Set<WelcomeMessage> welcomeMessages) {
        this.welcomeMessages = welcomeMessages;
    }

    public Set<GuildEvent> getGuildEvents() {
        return guildEvents;
    }

    public DiscordGuild guildEvents(Set<GuildEvent> guildEvents) {
        this.guildEvents = guildEvents;
        return this;
    }

    public DiscordGuild addGuildEvent(GuildEvent guildEvent) {
        this.guildEvents.add(guildEvent);
        guildEvent.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeGuildEvent(GuildEvent guildEvent) {
        this.guildEvents.remove(guildEvent);
        guildEvent.setDiscordGuild(null);
        return this;
    }

    public void setGuildEvents(Set<GuildEvent> guildEvents) {
        this.guildEvents = guildEvents;
    }

    public Set<GiveAway> getGiveAways() {
        return giveAways;
    }

    public DiscordGuild giveAways(Set<GiveAway> giveAways) {
        this.giveAways = giveAways;
        return this;
    }

    public DiscordGuild addGiveAway(GiveAway giveAway) {
        this.giveAways.add(giveAway);
        giveAway.setDiscordGuild(this);
        return this;
    }

    public DiscordGuild removeGiveAway(GiveAway giveAway) {
        this.giveAways.remove(giveAway);
        giveAway.setDiscordGuild(null);
        return this;
    }

    public void setGiveAways(Set<GiveAway> giveAways) {
        this.giveAways = giveAways;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscordGuild discordGuild = (DiscordGuild) o;
        if (discordGuild.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), discordGuild.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiscordGuild{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", guildName='" + getGuildName() + "'" +
            ", inviteLink='" + getInviteLink() + "'" +
            "}";
    }
}
