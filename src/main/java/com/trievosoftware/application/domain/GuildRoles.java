package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GuildRoles.
 */
@Entity
@Table(name = "guild_roles")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GuildRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "role_name")
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("guildroles")
    private CustomCommand customcommand;

    @ManyToOne
    @JsonIgnoreProperties("guildRoles")
    private DiscordGuild discordGuild;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public GuildRoles(@NotNull Long guildId, @NotNull Long roleId, String roleName) {
        this.guildId = guildId;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public GuildRoles(@NotNull Long guildId, @NotNull Long roleId) {
        this.guildId = guildId;
        this.roleId = roleId;
    }

    public GuildRoles() {
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

    public GuildRoles guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public GuildRoles roleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public GuildRoles roleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public CustomCommand getCustomcommand() {
        return customcommand;
    }

    public GuildRoles customcommand(CustomCommand customCommand) {
        this.customcommand = customCommand;
        return this;
    }

    public void setCustomcommand(CustomCommand customCommand) {
        this.customcommand = customCommand;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public GuildRoles discordGuild(DiscordGuild discordGuild) {
        this.discordGuild = discordGuild;
        return this;
    }

    public void setDiscordGuild(DiscordGuild discordGuild) {
        this.discordGuild = discordGuild;
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
        GuildRoles guildRoles = (GuildRoles) o;
        if (guildRoles.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), guildRoles.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GuildRoles{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", roleId=" + getRoleId() +
            ", roleName='" + getRoleName() + "'" +
            "}";
    }
}
