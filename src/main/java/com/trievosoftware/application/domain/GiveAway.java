package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A GiveAway.
 */
@Entity
@Table(name = "give_away")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GiveAway implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "name", length = 250, nullable = false)
    private String name;

    
    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @NotNull
    @Column(name = "text_channel_id", nullable = false)
    private Long textChannelId;

    @NotNull
    @Column(name = "finish", nullable = false)
    private Instant finish;

    @NotNull
    @Column(name = "winner", nullable = false)
    private Long winner;

    @NotNull
    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @ManyToMany( fetch = FetchType.EAGER )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "give_away_discorduser",
        joinColumns = @JoinColumn(name = "give_away_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "discorduser_id", referencedColumnName = "id"))
    private Set<DiscordUser> discordusers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("giveaways")
    private GuildSettings guildsettings;

    @ManyToOne
    @JsonIgnoreProperties("giveAway")
    private DiscordGuild discordGuild;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public GiveAway() {
    }

    public GiveAway(@NotNull @Size(max = 250) String name, String message, @NotNull Instant finish, DiscordGuild discordGuild)
    {
        this.name = name;
        this.message = message;
        this.finish = finish;
        this.discordGuild = discordGuild;

        this.expired = false;
        this.textChannelId = 0L;
        this.messageId = 0L;
        this.winner = 0L;
    }

    public GiveAway(@NotNull @Size(max = 250) String name, String message, @NotNull Long messageId,
                    @NotNull Long textChannelId, @NotNull Instant finish, @NotNull Long winner,
                    @NotNull Boolean expired, Set<DiscordUser> discordusers, DiscordGuild discordGuild) {
        this.name = name;
        this.message = message;
        this.messageId = messageId;
        this.textChannelId = textChannelId;
        this.finish = finish;
        this.winner = winner;
        this.expired = expired;
        this.discordusers = discordusers;
        this.discordGuild = discordGuild;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public GiveAway name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public GiveAway message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public GiveAway messageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getTextChannelId() {
        return textChannelId;
    }

    public GiveAway textChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
        return this;
    }

    public void setTextChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
    }

    public Instant getFinish() {
        return finish;
    }

    public GiveAway finish(Instant finish) {
        this.finish = finish;
        return this;
    }

    public void setFinish(Instant finish) {
        this.finish = finish;
    }

    public Long getWinner() {
        return winner;
    }

    public GiveAway winner(Long winner) {
        this.winner = winner;
        return this;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }

    public Boolean isExpired() {
        return expired;
    }

    public GiveAway expired(Boolean expired) {
        this.expired = expired;
        return this;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Set<DiscordUser> getDiscordusers() {
        return discordusers;
    }

    public GiveAway discordusers(Set<DiscordUser> discordUsers) {
        this.discordusers = discordUsers;
        return this;
    }

    public GiveAway addDiscorduser(DiscordUser discordUser) {
        this.discordusers.add(discordUser);
        discordUser.getGiveAways().add(this);
        return this;
    }

    public GiveAway removeDiscorduser(DiscordUser discordUser) {
        this.discordusers.remove(discordUser);
        discordUser.getGiveAways().remove(this);
        return this;
    }

    public void setDiscordusers(Set<DiscordUser> discordUsers) {
        this.discordusers = discordUsers;
    }

    public GuildSettings getGuildsettings() {
        return guildsettings;
    }

    public GiveAway guildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
        return this;
    }

    public void setGuildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public GiveAway discordGuild(DiscordGuild discordGuild) {
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
        GiveAway giveAway = (GiveAway) o;
        if (giveAway.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), giveAway.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GiveAway{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", message='" + getMessage() + "'" +
            ", messageId=" + getMessageId() +
            ", textChannelId=" + getTextChannelId() +
            ", finish='" + getFinish() + "'" +
            ", winner=" + getWinner() +
            ", expired='" + isExpired() + "'" +
            "}";
    }
}
