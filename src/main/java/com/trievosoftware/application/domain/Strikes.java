package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Strikes.
 */
@Entity
@Table(name = "strikes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Strikes implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "strikes", nullable = false)
    private Integer strikes;

    @ManyToOne
    @JsonIgnoreProperties("strikes")
    private DiscordGuild discordGuild;

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

    public Strikes guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getUserId() {
        return userId;
    }

    public Strikes userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public Strikes strikes(Integer strikes) {
        this.strikes = strikes;
        return this;
    }

    public void setStrikes(Integer strikes) {
        this.strikes = strikes;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public Strikes discordGuild(DiscordGuild discordGuild) {
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
        Strikes strikes = (Strikes) o;
        if (strikes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), strikes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Strikes{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", userId=" + getUserId() +
            ", strikes=" + getStrikes() +
            "}";
    }
}
