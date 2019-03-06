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
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * A GuildSettings.
 */
@Entity
@Table(name = "guild_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GuildSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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
            ", timezone='" + getTimezoneStr() + "'" +
            ", raidMode=" + getRaidMode() +
            ", muteRole=" + getMuteRole() +
            "}";
    }
}
