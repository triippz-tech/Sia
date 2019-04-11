package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A GuildEvent.
 */
@Entity
@Table(name = "guild_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GuildEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "event_name", length = 250, nullable = false)
    private String eventName;

    @NotNull
    @Size(max = 250)
    @Column(name = "event_image_url", length = 250, nullable = false)
    private String eventImageUrl;


    @Lob
    @Column(name = "event_message", nullable = false)
    private String eventMessage;

    @NotNull
    @Column(name = "event_start", nullable = false)
    private Instant eventStart;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @ManyToOne
    @JsonIgnoreProperties("guildevents")
    private GuildSettings guildsettings;

    public GuildEvent(@NotNull @Size(max = 250) String eventName, @NotNull @Size(max = 250) String eventImageUrl,
                      String eventMessage, @NotNull Instant eventStart, GuildSettings guildsettings) {
        this.eventName = eventName;
        this.eventImageUrl = eventImageUrl;
        this.eventMessage = eventMessage;
        this.eventStart = eventStart;
        this.guildsettings = guildsettings;
        this.expired = false;
    }

    public GuildEvent(@NotNull @Size(max = 250) String eventName, @NotNull @Size(max = 250) String eventImageUrl,
                      String eventMessage, @NotNull Instant eventStart) {
        this.eventName = eventName;
        this.eventImageUrl = eventImageUrl;
        this.eventMessage = eventMessage;
        this.eventStart = eventStart;
        this.expired = false;
    }

    public GuildEvent() {
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public GuildEvent eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public GuildEvent eventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
        return this;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public GuildEvent eventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
        return this;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public Instant getEventStart() {
        return eventStart;
    }

    public GuildEvent eventStart(Instant eventStart) {
        this.eventStart = eventStart;
        return this;
    }

    public void setEventStart(Instant eventStart) {
        this.eventStart = eventStart;
    }

    public GuildEvent expired(Boolean expired)
    {
        this.expired = expired;
        return this;
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public GuildSettings getGuildsettings() {
        return guildsettings;
    }

    public GuildEvent guildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
        return this;
    }

    public void setGuildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Message getGuildEventMessage(Guild guild, Boolean sample)
    {
        return FormatUtil.formatGuildEvent(guild, this, false, sample);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuildEvent guildEvent = (GuildEvent) o;
        if (guildEvent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), guildEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GuildEvent{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventImageUrl='" + getEventImageUrl() + "'" +
            ", eventMessage='" + getEventMessage() + "'" +
            ", eventStart='" + getEventStart() + "'" +
            "}";
    }
}
