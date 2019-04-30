package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @ManyToMany(mappedBy = "discordusers", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Poll> polls = new HashSet<>();

    @ManyToMany(mappedBy = "discordusers", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<PollItems> pollitems = new HashSet<>();

    @ManyToMany(mappedBy = "discordusers", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<GiveAway> giveAways = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("discordUser")
    private DiscordGuild discordGuild;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public DiscordUser() {
    }

    public DiscordUser(@NotNull Long userId) {
        this.userId = userId;
        this.commandsIssued = 0;
        this.blacklisted = false;
    }

    public DiscordUser(@NotNull Long userId, @NotNull Boolean blacklisted) {
        this.userId = userId;
        this.blacklisted = blacklisted;
        this.commandsIssued = 0;
    }


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

    public Set<Poll> getPolls() {
        return polls;
    }

    public DiscordUser polls(Set<Poll> polls) {
        this.polls = polls;
        return this;
    }

    public DiscordUser addPoll(Poll poll) {
        this.polls.add(poll);
        poll.getDiscordusers().add(this);
        return this;
    }

    public DiscordUser removePoll(Poll poll) {
        this.polls.remove(poll);
        poll.getDiscordusers().remove(this);
        return this;
    }

    public void setPolls(Set<Poll> polls) {
        this.polls = polls;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public Set<PollItems> getPollitems() {
        return pollitems;
    }

    public DiscordUser pollitems(Set<PollItems> pollItems) {
        this.pollitems = pollItems;
        return this;
    }

    public DiscordUser addPollitems(PollItems pollItems) {
        this.pollitems.add(pollItems);
        pollItems.getDiscordusers().add(this);
        return this;
    }

    public DiscordUser removePollitems(PollItems pollItems) {
        this.pollitems.remove(pollItems);
        pollItems.getDiscordusers().remove(this);
        return this;
    }

    public void setPollitems(Set<PollItems> pollItems) {
        this.pollitems = pollItems;
    }

    public Set<GiveAway> getGiveAways() {
        return giveAways;
    }

    public DiscordUser giveAways(Set<GiveAway> giveAways) {
        this.giveAways = giveAways;
        return this;
    }

    public DiscordUser addGiveAway(GiveAway giveAway) {
        this.giveAways.add(giveAway);
        giveAway.getDiscordusers().add(this);
        return this;
    }

    public DiscordUser removeGiveAway(GiveAway giveAway) {
        this.giveAways.remove(giveAway);
        giveAway.getDiscordusers().remove(this);
        return this;
    }

    public void setGiveAways(Set<GiveAway> giveAways) {
        this.giveAways = giveAways;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public DiscordUser discordGuild(DiscordGuild discordGuild) {
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
