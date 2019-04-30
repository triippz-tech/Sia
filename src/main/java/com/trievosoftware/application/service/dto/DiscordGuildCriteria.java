package com.trievosoftware.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the DiscordGuild entity. This class is used in DiscordGuildResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /discord-guilds?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiscordGuildCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter guildId;

    private StringFilter guildName;

    private StringFilter inviteLink;

    private LongFilter guildSettingsId;

    private LongFilter auditCacheId;

    private LongFilter autoModId;

    private LongFilter guildMusicSettingsId;

    private LongFilter discordUserId;

    private LongFilter ignoredId;

    private LongFilter tempMutesId;

    private LongFilter tempBansId;

    private LongFilter pollId;

    private LongFilter guildRolesId;

    private LongFilter customCommandId;

    private LongFilter actionsId;

    private LongFilter strikesId;

    private LongFilter welcomeMessageId;

    private LongFilter guildEventId;

    private LongFilter giveAwayId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public StringFilter getGuildName() {
        return guildName;
    }

    public void setGuildName(StringFilter guildName) {
        this.guildName = guildName;
    }

    public StringFilter getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(StringFilter inviteLink) {
        this.inviteLink = inviteLink;
    }

    public LongFilter getGuildSettingsId() {
        return guildSettingsId;
    }

    public void setGuildSettingsId(LongFilter guildSettingsId) {
        this.guildSettingsId = guildSettingsId;
    }

    public LongFilter getAuditCacheId() {
        return auditCacheId;
    }

    public void setAuditCacheId(LongFilter auditCacheId) {
        this.auditCacheId = auditCacheId;
    }

    public LongFilter getAutoModId() {
        return autoModId;
    }

    public void setAutoModId(LongFilter autoModId) {
        this.autoModId = autoModId;
    }

    public LongFilter getGuildMusicSettingsId() {
        return guildMusicSettingsId;
    }

    public void setGuildMusicSettingsId(LongFilter guildMusicSettingsId) {
        this.guildMusicSettingsId = guildMusicSettingsId;
    }

    public LongFilter getDiscordUserId() {
        return discordUserId;
    }

    public void setDiscordUserId(LongFilter discordUserId) {
        this.discordUserId = discordUserId;
    }

    public LongFilter getIgnoredId() {
        return ignoredId;
    }

    public void setIgnoredId(LongFilter ignoredId) {
        this.ignoredId = ignoredId;
    }

    public LongFilter getTempMutesId() {
        return tempMutesId;
    }

    public void setTempMutesId(LongFilter tempMutesId) {
        this.tempMutesId = tempMutesId;
    }

    public LongFilter getTempBansId() {
        return tempBansId;
    }

    public void setTempBansId(LongFilter tempBansId) {
        this.tempBansId = tempBansId;
    }

    public LongFilter getPollId() {
        return pollId;
    }

    public void setPollId(LongFilter pollId) {
        this.pollId = pollId;
    }

    public LongFilter getGuildRolesId() {
        return guildRolesId;
    }

    public void setGuildRolesId(LongFilter guildRolesId) {
        this.guildRolesId = guildRolesId;
    }

    public LongFilter getCustomCommandId() {
        return customCommandId;
    }

    public void setCustomCommandId(LongFilter customCommandId) {
        this.customCommandId = customCommandId;
    }

    public LongFilter getActionsId() {
        return actionsId;
    }

    public void setActionsId(LongFilter actionsId) {
        this.actionsId = actionsId;
    }

    public LongFilter getStrikesId() {
        return strikesId;
    }

    public void setStrikesId(LongFilter strikesId) {
        this.strikesId = strikesId;
    }

    public LongFilter getWelcomeMessageId() {
        return welcomeMessageId;
    }

    public void setWelcomeMessageId(LongFilter welcomeMessageId) {
        this.welcomeMessageId = welcomeMessageId;
    }

    public LongFilter getGuildEventId() {
        return guildEventId;
    }

    public void setGuildEventId(LongFilter guildEventId) {
        this.guildEventId = guildEventId;
    }

    public LongFilter getGiveAwayId() {
        return giveAwayId;
    }

    public void setGiveAwayId(LongFilter giveAwayId) {
        this.giveAwayId = giveAwayId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscordGuildCriteria that = (DiscordGuildCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(guildName, that.guildName) &&
            Objects.equals(inviteLink, that.inviteLink) &&
            Objects.equals(guildSettingsId, that.guildSettingsId) &&
            Objects.equals(auditCacheId, that.auditCacheId) &&
            Objects.equals(autoModId, that.autoModId) &&
            Objects.equals(guildMusicSettingsId, that.guildMusicSettingsId) &&
            Objects.equals(discordUserId, that.discordUserId) &&
            Objects.equals(ignoredId, that.ignoredId) &&
            Objects.equals(tempMutesId, that.tempMutesId) &&
            Objects.equals(tempBansId, that.tempBansId) &&
            Objects.equals(pollId, that.pollId) &&
            Objects.equals(guildRolesId, that.guildRolesId) &&
            Objects.equals(customCommandId, that.customCommandId) &&
            Objects.equals(actionsId, that.actionsId) &&
            Objects.equals(strikesId, that.strikesId) &&
            Objects.equals(welcomeMessageId, that.welcomeMessageId) &&
            Objects.equals(guildEventId, that.guildEventId) &&
            Objects.equals(giveAwayId, that.giveAwayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        guildId,
        guildName,
        inviteLink,
        guildSettingsId,
        auditCacheId,
        autoModId,
        guildMusicSettingsId,
        discordUserId,
        ignoredId,
        tempMutesId,
        tempBansId,
        pollId,
        guildRolesId,
        customCommandId,
        actionsId,
        strikesId,
        welcomeMessageId,
        guildEventId,
        giveAwayId
        );
    }

    @Override
    public String toString() {
        return "DiscordGuildCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (guildName != null ? "guildName=" + guildName + ", " : "") +
                (inviteLink != null ? "inviteLink=" + inviteLink + ", " : "") +
                (guildSettingsId != null ? "guildSettingsId=" + guildSettingsId + ", " : "") +
                (auditCacheId != null ? "auditCacheId=" + auditCacheId + ", " : "") +
                (autoModId != null ? "autoModId=" + autoModId + ", " : "") +
                (guildMusicSettingsId != null ? "guildMusicSettingsId=" + guildMusicSettingsId + ", " : "") +
                (discordUserId != null ? "discordUserId=" + discordUserId + ", " : "") +
                (ignoredId != null ? "ignoredId=" + ignoredId + ", " : "") +
                (tempMutesId != null ? "tempMutesId=" + tempMutesId + ", " : "") +
                (tempBansId != null ? "tempBansId=" + tempBansId + ", " : "") +
                (pollId != null ? "pollId=" + pollId + ", " : "") +
                (guildRolesId != null ? "guildRolesId=" + guildRolesId + ", " : "") +
                (customCommandId != null ? "customCommandId=" + customCommandId + ", " : "") +
                (actionsId != null ? "actionsId=" + actionsId + ", " : "") +
                (strikesId != null ? "strikesId=" + strikesId + ", " : "") +
                (welcomeMessageId != null ? "welcomeMessageId=" + welcomeMessageId + ", " : "") +
                (guildEventId != null ? "guildEventId=" + guildEventId + ", " : "") +
                (giveAwayId != null ? "giveAwayId=" + giveAwayId + ", " : "") +
            "}";
    }

}
