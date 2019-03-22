package com.trievosoftware.application.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DiscordUser.
 */
@Entity
@Table(name = "discord_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiscordUser implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @NotNull
    @Column(name = "commands_issued", nullable = false)
    private Integer commandsIssued;

    @NotNull
    @Column(name = "blacklisted", nullable = false)
    private Boolean blacklisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public DiscordUser userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCommandsIssued() {
        return commandsIssued;
    }

    public DiscordUser commandsIssued(Integer commandsIssued) {
        this.commandsIssued = commandsIssued;
        return this;
    }

    public void setCommandsIssued(Integer commandsIssued) {
        this.commandsIssued = commandsIssued;
    }

    public Boolean isBlacklisted() {
        return blacklisted;
    }

    public DiscordUser blacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
        return this;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
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
        DiscordUser discordUser = (DiscordUser) o;
        if (discordUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), discordUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", commandsIssued=" + getCommandsIssued() +
            ", blacklisted='" + isBlacklisted() + "'" +
            "}";
    }
}
