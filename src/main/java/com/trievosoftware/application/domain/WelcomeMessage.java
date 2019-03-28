package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trievosoftware.discord.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WelcomeMessage.
 */
@Entity
@Table(name = "welcome_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WelcomeMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "message_title", nullable = false)
    private String messageTitle;

    @Lob
    @NotNull
    @Column(name = "jhi_body", nullable = false)
    private String body;

    @NotNull
    @Column(name = "footer", nullable = false)
    private String footer;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "logo_url")
    private String logoUrl;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties("welcomemessages")
    private GuildSettings guildsettings;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WelcomeMessage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public WelcomeMessage messageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
        return this;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getBody() {
        return body;
    }

    public WelcomeMessage body(String body) {
        this.body = body;
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public WelcomeMessage footer(String footer) {
        this.footer = footer;
        return this;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public WelcomeMessage websiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public WelcomeMessage logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public WelcomeMessage active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public GuildSettings getGuildsettings() {
        return guildsettings;
    }

    public WelcomeMessage guildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
        return this;
    }

    public void setGuildsettings(GuildSettings guildSettings) {
        this.guildsettings = guildSettings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Message getWelcomeMessage(Guild guild, User user)
    {
        return FormatUtil.formatWelcomeMessage(this, guild, user);
    }

    public Message getWelcomeMessage(GuildMemberJoinEvent event)
    {
        return FormatUtil.formatWelcomeMessage(this, event.getGuild(), event.getUser());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WelcomeMessage welcomeMessage = (WelcomeMessage) o;
        if (welcomeMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), welcomeMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WelcomeMessage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", messageTitle='" + getMessageTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", footer='" + getFooter() + "'" +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }

    public static class WelcomeMessageBuilder
    {
        private String name;
        private String messageTitle;
        private String body;
        private String footer;
        private String websiteUrl;
        private String logoUrl;
        private Boolean active;
        private GuildSettings guildsettings;

        public WelcomeMessageBuilder()
        {
            this.active = false;
            this.websiteUrl = "";
            this.logoUrl = "";
        }

        public WelcomeMessageBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public WelcomeMessageBuilder messageTitle(String messageTitle)
        {
            this.messageTitle = messageTitle;
            return this;
        }

        public WelcomeMessageBuilder body(String body)
        {
            this.body = body;
            return this;
        }

        public WelcomeMessageBuilder footer(String footer)
        {
            this.footer = footer;
            return this;
        }

        public WelcomeMessageBuilder websiteUrl(String websiteUrl)
        {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public WelcomeMessageBuilder logoUrl(String logoUrl)
        {
            this.logoUrl = logoUrl;
            return this;
        }

        public WelcomeMessageBuilder isActive(Boolean active)
        {
            this.active = active;
            return this;
        }

        public WelcomeMessageBuilder guildSettings(GuildSettings guildSettings)
        {
            this.guildsettings = guildSettings;
            return this;
        }

        public WelcomeMessage build()
        {
            WelcomeMessage welcomeMessage = new WelcomeMessage();
            welcomeMessage.active = active;
            welcomeMessage.name = name;
            welcomeMessage.messageTitle = messageTitle;
            welcomeMessage.body = body;
            welcomeMessage.footer = footer;
            welcomeMessage.websiteUrl = websiteUrl;
            welcomeMessage.logoUrl = logoUrl;
            welcomeMessage.guildsettings = guildsettings;

            return welcomeMessage;
        }
    }
}
