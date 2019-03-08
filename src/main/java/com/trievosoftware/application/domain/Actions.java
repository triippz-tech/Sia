package com.trievosoftware.application.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Actions.
 */
@Entity
@Table(name = "actions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Actions implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @NotNull
    @Column(name = "num_strikes", nullable = false)
    private Integer numStrikes;

    @NotNull
    @Column(name = "action", nullable = false)
    private Integer action;

    @NotNull
    @Column(name = "jhi_time", nullable = false)
    private Integer time;

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

    public Actions guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Integer getNumStrikes() {
        return numStrikes;
    }

    public Actions numStrikes(Integer numStrikes) {
        this.numStrikes = numStrikes;
        return this;
    }

    public void setNumStrikes(Integer numStrikes) {
        this.numStrikes = numStrikes;
    }

    public Integer getAction() {
        return action;
    }

    public Actions action(Integer action) {
        this.action = action;
        return this;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getTime() {
        return time;
    }

    public Actions time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
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
        Actions actions = (Actions) o;
        if (actions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Actions{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", numStrikes=" + getNumStrikes() +
            ", action=" + getAction() +
            ", time=" + getTime() +
            "}";
    }
}
