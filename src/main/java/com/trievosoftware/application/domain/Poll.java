package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Poll.
 */
@Entity
@Table(name = "poll")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Poll implements Serializable {

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

    @Column(name = "text_channel_id")
    private Long textChannelId;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "finish_time", nullable = false)
    private Instant finishTime;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PollItems> pollitems = new HashSet<>();

    @ManyToMany( fetch = FetchType.EAGER )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "poll_discorduser",
        joinColumns = @JoinColumn(name = "poll_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "discorduser_id", referencedColumnName = "id"))
    private Set<DiscordUser> discordusers = new HashSet<>();

    public Poll() {
    }

    public Poll(@NotNull Long guildId, @NotNull Long userId, @NotNull String title, @NotNull Instant finishTime) {
        this.guildId = guildId;
        this.userId = userId;
        this.title = title;
        this.finishTime = finishTime;
        this.textChannelId = 0L;
        this.messageId = 0L;
        this.expired = false;
    }

    public Poll(@NotNull Long guildId, @NotNull Long userId, Long textChannelId,@NotNull Long messageId, @NotNull String title, @NotNull Instant finishTime) {
        this.guildId = guildId;
        this.userId = userId;
        this.textChannelId = textChannelId;
        this.messageId = messageId;
        this.title = title;
        this.finishTime = finishTime;
        this.expired = false;
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

    public Poll guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getUserId() {
        return userId;
    }

    public Poll userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTextChannelId() {
        return textChannelId;
    }

    public Poll textChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
        return this;
    }

    public void setTextChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
    }

    public String getTitle() {
        return title;
    }

    public Poll title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public Poll finishTime(Instant finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public Poll messageId(Long messageId)
    {
        this.messageId = messageId;
        return this;
    }

    public Poll expired(Boolean expired)
    {
        this.expired = expired;
        return this;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isPollActive()
    {
        return this.finishTime.isAfter(Instant.now());
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Set<PollItems> getPollitems() {
        return pollitems;
    }

    public Poll pollitems(Set<PollItems> pollItems) {
        this.pollitems = pollItems;
        return this;
    }

    public Poll addPollitems(PollItems pollItems) {
        this.pollitems.add(pollItems);
        pollItems.setPoll(this);
        return this;
    }

    public Poll removePollitems(PollItems pollItems) {
        this.pollitems.remove(pollItems);
        pollItems.setPoll(null);
        return this;
    }

    public void setPollitems(Set<PollItems> pollItems) {
        this.pollitems = pollItems;
    }

    public Set<DiscordUser> getDiscordusers() {
        return discordusers;
    }

    public Poll discordusers(Set<DiscordUser> discordUsers) {
        this.discordusers = discordUsers;
        return this;
    }

    public Poll addDiscorduser(DiscordUser discordUser) {
        this.discordusers.add(discordUser);
        discordUser.getPolls().add(this);
        return this;
    }

    public Poll removeDiscorduser(DiscordUser discordUser) {
        this.discordusers.remove(discordUser);
        discordUser.getPolls().remove(this);
        return this;
    }

    public void setDiscordusers(Set<DiscordUser> discordUsers) {
        this.discordusers = discordUsers;
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
        Poll poll = (Poll) o;
        if (poll.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), poll.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Poll{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", userId=" + getUserId() +
            ", textChannelId=" + getTextChannelId() +
            ", title='" + getTitle() + "'" +
            ", finishTime='" + getFinishTime() + "'" +
            "}";
    }
}
