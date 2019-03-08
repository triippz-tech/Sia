package com.trievosoftware.application.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AutoMod.
 */
@Entity
@Table(name = "auto_mod")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AutoMod implements Serializable {

    private static final long serialVersionUID = 1L;
    public final static int MAX_STRIKES = 100;
    public final static int MENTION_MINIMUM = 4;
    public final static int ROLE_MENTION_MINIMUM = 2;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false, unique = true)
    private Long guildId;

    @NotNull
    @Column(name = "resolve_urls", nullable = false)
    private Boolean resolveUrls;

    @NotNull
    @Column(name = "max_mentions", nullable = false)
    private Integer maxMentions;

    @NotNull
    @Column(name = "max_role_mentions", nullable = false)
    private Integer maxRoleMentions;

    @NotNull
    @Column(name = "max_lines", nullable = false)
    private Integer maxLines;

    @NotNull
    @Column(name = "raid_mode_number", nullable = false)
    private Integer raidModeNumber;

    @NotNull
    @Column(name = "raid_mode_time", nullable = false)
    private Integer raidModeTime;

    @NotNull
    @Column(name = "invite_strikes", nullable = false)
    private Integer inviteStrikes;

    @NotNull
    @Column(name = "ref_strikes", nullable = false)
    private Integer refStrikes;

    @NotNull
    @Column(name = "copy_pasta_strikes", nullable = false)
    private Integer copyPastaStrikes;

    @NotNull
    @Column(name = "everyone_strikes", nullable = false)
    private Integer everyoneStrikes;

    @NotNull
    @Column(name = "dupe_strikes", nullable = false)
    private Integer dupeStrikes;

    @NotNull
    @Column(name = "dupe_delete_thresh", nullable = false)
    private Integer dupeDeleteThresh;

    @NotNull
    @Column(name = "dupe_strikes_thresh", nullable = false)
    private Integer dupeStrikesThresh;

    @NotNull
    @Column(name = "dehoist_char", nullable = false)
    private Integer dehoistChar;

    public AutoMod() {}

    public void setDefaults(Long guildId) {
        this.guildId = guildId;
        this.resolveUrls = false;
        this.maxMentions = 0;
        this.maxRoleMentions = 0;
        this.maxLines = 0;
        this.raidModeNumber = 0;
        this.raidModeTime = 0;
        this.inviteStrikes = 0;
        this.refStrikes = 0;
        this.copyPastaStrikes = 0;
        this.everyoneStrikes = 0;
        this.dupeStrikes = 0;
        this.dupeDeleteThresh = 0;
        this.dupeStrikesThresh = 0;
        this.dehoistChar = 0;
    }

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

    public AutoMod guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Boolean isResolveUrls() {
        return resolveUrls;
    }

    public AutoMod resolveUrls(Boolean resolveUrls) {
        this.resolveUrls = resolveUrls;
        return this;
    }

    public void setResolveUrls(Boolean resolveUrls) {
        this.resolveUrls = resolveUrls;
    }

    public Integer getMaxMentions() {
        return maxMentions;
    }

    public AutoMod maxMentions(Integer maxMentions) {
        this.maxMentions = maxMentions;
        return this;
    }

    public void setMaxMentions(Integer maxMentions) {
        this.maxMentions = maxMentions;
    }

    public Integer getMaxRoleMentions() {
        return maxRoleMentions;
    }

    public AutoMod maxRoleMentions(Integer maxRoleMentions) {
        this.maxRoleMentions = maxRoleMentions;
        return this;
    }

    public void setMaxRoleMentions(Integer maxRoleMentions) {
        this.maxRoleMentions = maxRoleMentions;
    }

    public Integer getMaxLines() {
        return maxLines;
    }

    public AutoMod maxLines(Integer maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    public void setMaxLines(Integer maxLines) {
        this.maxLines = maxLines;
    }

    public Integer getRaidModeNumber() {
        return raidModeNumber;
    }

    public AutoMod raidModeNumber(Integer raidModeNumber) {
        this.raidModeNumber = raidModeNumber;
        return this;
    }

    public void setRaidModeNumber(Integer raidModeNumber) {
        this.raidModeNumber = raidModeNumber;
    }

    public Integer getRaidModeTime() {
        return raidModeTime;
    }

    public AutoMod raidModeTime(Integer raidModeTime) {
        this.raidModeTime = raidModeTime;
        return this;
    }

    public void setRaidModeTime(Integer raidModeTime) {
        this.raidModeTime = raidModeTime;
    }

    public Integer getInviteStrikes() {
        return inviteStrikes;
    }

    public AutoMod inviteStrikes(Integer inviteStrikes) {
        this.inviteStrikes = inviteStrikes;
        return this;
    }

    public void setInviteStrikes(Integer inviteStrikes) {
        this.inviteStrikes = inviteStrikes;
    }

    public Integer getRefStrikes() {
        return refStrikes;
    }

    public AutoMod refStrikes(Integer refStrikes) {
        this.refStrikes = refStrikes;
        return this;
    }

    public void setRefStrikes(Integer refStrikes) {
        this.refStrikes = refStrikes;
    }

    public Integer getCopyPastaStrikes() {
        return copyPastaStrikes;
    }

    public AutoMod copyPastaStrikes(Integer copyPastaStrikes) {
        this.copyPastaStrikes = copyPastaStrikes;
        return this;
    }

    public void setCopyPastaStrikes(Integer copyPastaStrikes) {
        this.copyPastaStrikes = copyPastaStrikes;
    }

    public Integer getEveryoneStrikes() {
        return everyoneStrikes;
    }

    public AutoMod everyoneStrikes(Integer everyoneStrikes) {
        this.everyoneStrikes = everyoneStrikes;
        return this;
    }

    public void setEveryoneStrikes(Integer everyoneStrikes) {
        this.everyoneStrikes = everyoneStrikes;
    }

    public Integer getDupeStrikes() {
        return dupeStrikes;
    }

    public AutoMod dupeStrikes(Integer dupeStrikes) {
        this.dupeStrikes = dupeStrikes;
        return this;
    }

    public void setDupeStrikes(Integer dupeStrikes) {
        this.dupeStrikes = dupeStrikes;
    }

    public Integer getDupeDeleteThresh() {
        return dupeDeleteThresh;
    }

    public AutoMod dupeDeleteThresh(Integer dupeDeleteThresh) {
        this.dupeDeleteThresh = dupeDeleteThresh;
        return this;
    }

    public void setDupeDeleteThresh(Integer dupeDeleteThresh) {
        this.dupeDeleteThresh = dupeDeleteThresh;
    }

    public Integer getDupeStrikesThresh() {
        return dupeStrikesThresh;
    }

    public AutoMod dupeStrikesThresh(Integer dupeStrikesThresh) {
        this.dupeStrikesThresh = dupeStrikesThresh;
        return this;
    }

    public void setDupeStrikesThresh(Integer dupeStrikesThresh) {
        this.dupeStrikesThresh = dupeStrikesThresh;
    }

    public Integer getDehoistChar() {
        return dehoistChar;
    }

    public AutoMod dehoistChar(Integer dehoistChar) {
        this.dehoistChar = dehoistChar;
        return this;
    }

    public void setDehoistChar(Integer dehoistChar) {
        this.dehoistChar = dehoistChar;
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
        AutoMod autoMod = (AutoMod) o;
        if (autoMod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), autoMod.getId());
    }

    public boolean useAutoRaidMode()
    {
        return raidModeNumber>1 && raidModeTime>1;
    }

    public boolean useAntiDuplicate()
    {
        return dupeStrikes>0 && dupeDeleteThresh>0 && dupeStrikesThresh>0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AutoMod{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", resolveUrls='" + isResolveUrls() + "'" +
            ", maxMentions=" + getMaxMentions() +
            ", maxRoleMentions=" + getMaxRoleMentions() +
            ", maxLines=" + getMaxLines() +
            ", raidModeNumber=" + getRaidModeNumber() +
            ", raidModeTime=" + getRaidModeTime() +
            ", inviteStrikes=" + getInviteStrikes() +
            ", refStrikes=" + getRefStrikes() +
            ", copyPastaStrikes=" + getCopyPastaStrikes() +
            ", everyoneStrikes=" + getEveryoneStrikes() +
            ", dupeStrikes=" + getDupeStrikes() +
            ", dupeDeleteThresh=" + getDupeDeleteThresh() +
            ", dupeStrikesThresh=" + getDupeStrikesThresh() +
            ", dehoistChar=" + getDehoistChar() +
            "}";
    }
}
