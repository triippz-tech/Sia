package com.trievosoftware.application.service.dto;

import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the GiveAway entity. This class is used in GiveAwayResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /give-aways?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GiveAwayCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter messageId;

    private LongFilter textChannelId;

    private InstantFilter finish;

    private LongFilter winner;

    private BooleanFilter expired;

    private LongFilter discordUserId;

    private LongFilter guildsettingsId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getMessageId() {
        return messageId;
    }

    public void setMessageId(LongFilter messageId) {
        this.messageId = messageId;
    }

    public LongFilter getTextChannelId() {
        return textChannelId;
    }

    public void setTextChannelId(LongFilter textChannelId) {
        this.textChannelId = textChannelId;
    }

    public InstantFilter getFinish() {
        return finish;
    }

    public void setFinish(InstantFilter finish) {
        this.finish = finish;
    }

    public LongFilter getWinner() {
        return winner;
    }

    public void setWinner(LongFilter winner) {
        this.winner = winner;
    }

    public BooleanFilter getExpired() {
        return expired;
    }

    public void setExpired(BooleanFilter expired) {
        this.expired = expired;
    }

    public LongFilter getDiscordUserId() {
        return discordUserId;
    }

    public void setDiscordUserId(LongFilter discordUserId) {
        this.discordUserId = discordUserId;
    }

    public LongFilter getGuildsettingsId() {
        return guildsettingsId;
    }

    public void setGuildsettingsId(LongFilter guildsettingsId) {
        this.guildsettingsId = guildsettingsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GiveAwayCriteria that = (GiveAwayCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(messageId, that.messageId) &&
            Objects.equals(textChannelId, that.textChannelId) &&
            Objects.equals(finish, that.finish) &&
            Objects.equals(winner, that.winner) &&
            Objects.equals(expired, that.expired) &&
            Objects.equals(discordUserId, that.discordUserId) &&
            Objects.equals(guildsettingsId, that.guildsettingsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        messageId,
        textChannelId,
        finish,
        winner,
        expired,
        discordUserId,
        guildsettingsId
        );
    }

    @Override
    public String toString() {
        return "GiveAwayCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (messageId != null ? "messageId=" + messageId + ", " : "") +
                (textChannelId != null ? "textChannelId=" + textChannelId + ", " : "") +
                (finish != null ? "finish=" + finish + ", " : "") +
                (winner != null ? "winner=" + winner + ", " : "") +
                (expired != null ? "expired=" + expired + ", " : "") +
                (discordUserId != null ? "discordUserId=" + discordUserId + ", " : "") +
                (guildsettingsId != null ? "guildsettingsId=" + guildsettingsId + ", " : "") +
            "}";
    }

}
