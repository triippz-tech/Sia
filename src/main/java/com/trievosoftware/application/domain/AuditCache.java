package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AuditCache.
 */
@Entity
@Table(name = "audit_cache")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuditCache implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false, unique = true)
    private Long guildId;

    @NotNull
    @Column(name = "jhi_old", nullable = false)
    private Long old;

    @NotNull
    @Column(name = "older", nullable = false)
    private Long older;

    @NotNull
    @Column(name = "oldest", nullable = false)
    private Long oldest;

    @OneToOne(mappedBy = "auditCache")
    @JsonIgnore
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

    public AuditCache guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getOld() {
        return old;
    }

    public AuditCache old(Long old) {
        this.old = old;
        return this;
    }

    public void setOld(Long old) {
        this.old = old;
    }

    public Long getOlder() {
        return older;
    }

    public AuditCache older(Long older) {
        this.older = older;
        return this;
    }

    public void setOlder(Long older) {
        this.older = older;
    }

    public Long getOldest() {
        return oldest;
    }

    public AuditCache oldest(Long oldest) {
        this.oldest = oldest;
        return this;
    }

    public void setOldest(Long oldest) {
        this.oldest = oldest;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public AuditCache discordGuild(DiscordGuild discordGuild) {
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
        AuditCache auditCache = (AuditCache) o;
        if (auditCache.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), auditCache.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuditCache{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", old=" + getOld() +
            ", older=" + getOlder() +
            ", oldest=" + getOldest() +
            "}";
    }
}
