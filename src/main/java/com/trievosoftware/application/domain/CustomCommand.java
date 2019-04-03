package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Message;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CustomCommand.
 */
@Entity
@Table(name = "custom_command")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @NotNull
    @Column(name = "command_name", nullable = false)
    private String commandName;

    
    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @OneToMany(mappedBy = "customcommand", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GuildRoles> guildroles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("customcommands")
    private GuildSettings guildsettings;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public CustomCommand(@NotNull Long guildId, @NotNull String commandName, String message, Set<GuildRoles> guildroles, GuildSettings guildsettings) {
        this.guildId = guildId;
        this.commandName = commandName;
        this.message = message;
        this.guildroles = guildroles;
        this.guildsettings = guildsettings;
    }

    public CustomCommand(@NotNull Long guildId, @NotNull String commandName, String message, GuildSettings guildsettings) {
        this.guildId = guildId;
        this.commandName = commandName;
        this.message = message;
        this.guildsettings = guildsettings;
    }

    public CustomCommand() {
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

    public CustomCommand guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getCommandName() {
        return commandName;
    }

    public CustomCommand commandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getMessage() {
        return message;
    }

    public CustomCommand message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<GuildRoles> getGuildroles() {
        return guildroles;
    }

    public CustomCommand guildroles(Set<GuildRoles> guildRoles) {
        this.guildroles = guildRoles;
        return this;
    }

    public CustomCommand addGuildroles(GuildRoles guildRoles) {
        this.guildroles.add(guildRoles);
        guildRoles.setCustomcommand(this);
        return this;
    }

    public CustomCommand removeGuildroles(GuildRoles guildRoles) {
        this.guildroles.remove(guildRoles);
        guildRoles.setCustomcommand(null);
        return this;
    }

    public void setGuildroles(Set<GuildRoles> guildRoles) {
        this.guildroles = guildRoles;
    }

    public GuildSettings getGuildsettings() {
        return guildsettings;
    }

    public CustomCommand guildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
        return this;
    }

    public void setGuildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

//    public Message getFormattedMessage()
//    {
//        return FormatUtil.formatCustomCommand(this);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomCommand customCommand = (CustomCommand) o;
        if (customCommand.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customCommand.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomCommand{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", commandName='" + getCommandName() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
