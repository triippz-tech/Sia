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
 * Criteria class for the DiscordUser entity. This class is used in DiscordUserResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /discord-users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiscordUserCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private IntegerFilter commandsIssued;

    private BooleanFilter blacklisted;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public IntegerFilter getCommandsIssued() {
        return commandsIssued;
    }

    public void setCommandsIssued(IntegerFilter commandsIssued) {
        this.commandsIssued = commandsIssued;
    }

    public BooleanFilter getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(BooleanFilter blacklisted) {
        this.blacklisted = blacklisted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscordUserCriteria that = (DiscordUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(commandsIssued, that.commandsIssued) &&
            Objects.equals(blacklisted, that.blacklisted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userId,
        commandsIssued,
        blacklisted
        );
    }

    @Override
    public String toString() {
        return "DiscordUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (commandsIssued != null ? "commandsIssued=" + commandsIssued + ", " : "") +
                (blacklisted != null ? "blacklisted=" + blacklisted + ", " : "") +
            "}";
    }

}
