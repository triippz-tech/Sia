package com.trievosoftware.application.domain;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.*;

/**
 * A GuildSettings.
 */
@Entity
@Table(name = "guild_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GuildSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    public final static int PREFIX_MAX_LENGTH = 40;
    private static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("GMT-4");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false, unique = true)
    private Long guildId;

    @NotNull
    @Column(name = "mod_role_id", nullable = false)
    private Long modRoleId;

    @NotNull
    @Column(name = "mod_log_id", nullable = false)
    private Long modLogId;

    @NotNull
    @Column(name = "server_log_id", nullable = false)
    private Long serverLogId;

    @NotNull
    @Column(name = "message_log_id", nullable = false)
    private Long messageLogId;

    @NotNull
    @Column(name = "voice_log_id", nullable = false)
    private Long voiceLogId;

    @NotNull
    @Column(name = "avatar_log_id", nullable = false)
    private Long avatarLogId;

    @Size(max = 40)
    @Column(name = "prefix", length = 40)
    private String prefix;

    @Size(max = 32)
    @Column(name = "timezone", length = 32)
    private String timezone;

    @NotNull
    @Column(name = "raid_mode", nullable = false)
    private Integer raidMode;

    @NotNull
    @Column(name = "mute_role", nullable = false)
    private Long muteRole;

    @OneToMany(mappedBy = "guildsettings", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WelcomeMessage> welcomemessages = new HashSet<>();

    @OneToMany(mappedBy = "guildsettings", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CustomCommand> customcommands = new HashSet<>();

    @OneToMany(mappedBy = "guildsettings", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GuildEvent> guildevents = new HashSet<>();

    @OneToMany(mappedBy = "guildsettings", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GiveAway> giveaways = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public GuildSettings () {}

    public void setDefaults(Long guildId) {
        this.guildId = guildId;
        this.modRoleId = 0L;
        this.modLogId = 0L;
        this.muteRole = 0L;
        this.serverLogId = 0L;
        this.messageLogId = 0L;
        this.voiceLogId = 0L;
        this.avatarLogId = 0L;
        this.prefix = "";
        this.timezone = DEFAULT_TIMEZONE.toString();
        this.raidMode = -2;
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

    public GuildSettings guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getModRoleId() {
        return modRoleId;
    }

    public GuildSettings modRoleId(Long modRoleId) {
        this.modRoleId = modRoleId;
        return this;
    }

    public void setModRoleId(Long modRoleId) {
        this.modRoleId = modRoleId;
    }

    public Long getModLogId() {
        return modLogId;
    }

    public GuildSettings modLogId(Long modLogId) {
        this.modLogId = modLogId;
        return this;
    }

    public void setModLogId(Long modLogId) {
        this.modLogId = modLogId;
    }

    public Long getServerLogId() {
        return serverLogId;
    }

    public GuildSettings serverLogId(Long serverLogId) {
        this.serverLogId = serverLogId;
        return this;
    }

    public void setServerLogId(Long serverLogId) {
        this.serverLogId = serverLogId;
    }

    public Long getMessageLogId() {
        return messageLogId;
    }

    public GuildSettings messageLogId(Long messageLogId) {
        this.messageLogId = messageLogId;
        return this;
    }

    public void setMessageLogId(Long messageLogId) {
        this.messageLogId = messageLogId;
    }

    public Long getVoiceLogId() {
        return voiceLogId;
    }

    public GuildSettings voiceLogId(Long voiceLogId) {
        this.voiceLogId = voiceLogId;
        return this;
    }

    public void setVoiceLogId(Long voiceLogId) {
        this.voiceLogId = voiceLogId;
    }

    public Long getAvatarLogId() {
        return avatarLogId;
    }

    public GuildSettings avatarLogId(Long avatarLogId) {
        this.avatarLogId = avatarLogId;
        return this;
    }

    public void setAvatarLogId(Long avatarLogId) {
        this.avatarLogId = avatarLogId;
    }

    public String getPrefix() {
        return prefix;
    }

    public GuildSettings prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTimezoneStr() {
        return timezone;
    }

    public GuildSettings timezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getRaidMode() {
        return raidMode;
    }

    public GuildSettings raidMode(Integer raidMode) {
        this.raidMode = raidMode;
        return this;
    }

    public void setRaidMode(Integer raidMode) {
        this.raidMode = raidMode;
    }

    public Long getMuteRole() {
        return muteRole;
    }

    public GuildSettings muteRole(Long muteRole) {
        this.muteRole = muteRole;
        return this;
    }

    public void setMuteRole(Long muteRole) {
        this.muteRole = muteRole;
    }

    public Set<WelcomeMessage> getWelcomemessages() {
        return welcomemessages;
    }

    public GuildSettings welcomemessages(Set<WelcomeMessage> welcomeMessages) {
        this.welcomemessages = welcomeMessages;
        return this;
    }

    public GuildSettings addWelcomemessage(WelcomeMessage welcomeMessage) {
        this.welcomemessages.add(welcomeMessage);
        welcomeMessage.setGuildsettings(this);
        return this;
    }

    public GuildSettings removeWelcomemessage(WelcomeMessage welcomeMessage) {
        this.welcomemessages.remove(welcomeMessage);
        welcomeMessage.setGuildsettings(null);
        return this;
    }

    public void setWelcomemessages(Set<WelcomeMessage> welcomeMessages) {
        this.welcomemessages = welcomeMessages;
    }

    public Set<CustomCommand> getCustomcommands() {
        return customcommands;
    }

    public GuildSettings customcommands(Set<CustomCommand> customCommands) {
        this.customcommands = customCommands;
        return this;
    }

    public GuildSettings addCustomcommand(CustomCommand customCommand) {
        this.customcommands.add(customCommand);
        customCommand.setGuildsettings(this);
        return this;
    }

    public GuildSettings removeCustomcommand(CustomCommand customCommand) {
        this.customcommands.remove(customCommand);
        customCommand.setGuildsettings(null);
        return this;
    }

    public Set<GuildEvent> getGuildevents() {
        return guildevents;
    }

    public GuildSettings guildevents(Set<GuildEvent> guildEvents) {
        this.guildevents = guildEvents;
        return this;
    }

    public GuildSettings addGuildevent(GuildEvent guildEvent) {
        this.guildevents.add(guildEvent);
        guildEvent.setGuildsettings(this);
        return this;
    }

    public GuildSettings removeGuildevent(GuildEvent guildEvent) {
        this.guildevents.remove(guildEvent);
        guildEvent.setGuildsettings(null);
        return this;
    }

    public void setGuildevents(Set<GuildEvent> guildEvents) {
        this.guildevents = guildEvents;
    }

    public Set<GiveAway> getGiveaways() {
        return giveaways;
    }

    public GuildSettings giveaways(Set<GiveAway> giveAways) {
        this.giveaways = giveAways;
        return this;
    }

    public GuildSettings addGiveaway(GiveAway giveAway) {
        this.giveaways.add(giveAway);
        giveAway.setGuildsettings(this);
        return this;
    }

    public GuildSettings removeGiveaway(GiveAway giveAway) {
        this.giveaways.remove(giveAway);
        giveAway.setGuildsettings(null);
        return this;
    }

    public void setGiveaways(Set<GiveAway> giveAways) {
        this.giveaways = giveAways;
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
        GuildSettings guildSettings = (GuildSettings) o;
        if (guildSettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), guildSettings.getId());
    }

    public Role getModeratorRole(Guild guild)
    {
        return guild.getRoleById(modRoleId);
    }

    public Role getMutedRole(Guild guild)
    {
        Role rid = guild.getRoleById(muteRole);
        if(rid!=null)
            return rid;
        return guild.getRoles().stream().filter(r -> r.getName().equalsIgnoreCase("Muted")).findFirst().orElse(null);
    }

    public TextChannel getModLogChannel(Guild guild)
    {
        return guild.getTextChannelById(modLogId);
    }

    public TextChannel getServerLogChannel(Guild guild)
    {
        return guild.getTextChannelById(serverLogId);
    }

    public TextChannel getMessageLogChannel(Guild guild)
    {
        return guild.getTextChannelById(messageLogId);
    }

    public TextChannel getVoiceLogChannel(Guild guild)
    {
        return guild.getTextChannelById(voiceLogId);
    }

    public TextChannel getAvatarLogChannel(Guild guild)
    {
        return guild.getTextChannelById(avatarLogId);
    }

    public ZoneId getTimezone()
    {
        return ZoneId.of(timezone);
    }

    public Collection<String> getPrefixes()
    {
        if(prefix==null || prefix.isEmpty())
            return null;
        return Collections.singleton(prefix);
    }

    public boolean isInRaidMode()
    {
        return raidMode != -2;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GuildSettings{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", modRoleId=" + getModRoleId() +
            ", modLogId=" + getModLogId() +
            ", serverLogId=" + getServerLogId() +
            ", messageLogId=" + getMessageLogId() +
            ", voiceLogId=" + getVoiceLogId() +
            ", avatarLogId=" + getAvatarLogId() +
            ", prefix='" + getPrefix() + "'" +
            ", timezone='" + getTimezone() + "'" +
            ", raidMode=" + getRaidMode() +
            ", muteRole=" + getMuteRole() +
            "}";
    }
}
