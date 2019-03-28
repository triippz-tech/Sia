package com.trievosoftware.application.service.dto;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the WelcomeMessage entity. This class is used in WelcomeMessageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /welcome-messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WelcomeMessageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter messageTitle;

    private StringFilter body;

    private StringFilter footer;

    private StringFilter websiteUrl;

    private StringFilter logoUrl;

    private BooleanFilter active;

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

    public StringFilter getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(StringFilter messageTitle) {
        this.messageTitle = messageTitle;
    }

    public StringFilter getBody() {
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public StringFilter getFooter() {
        return footer;
    }

    public void setFooter(StringFilter footer) {
        this.footer = footer;
    }

    public StringFilter getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(StringFilter websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public StringFilter getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(StringFilter logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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
        final WelcomeMessageCriteria that = (WelcomeMessageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(messageTitle, that.messageTitle) &&
            Objects.equals(body, that.body) &&
            Objects.equals(footer, that.footer) &&
            Objects.equals(websiteUrl, that.websiteUrl) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(active, that.active) &&
            Objects.equals(guildsettingsId, that.guildsettingsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        messageTitle,
        body,
        footer,
        websiteUrl,
        logoUrl,
        active,
        guildsettingsId
        );
    }

    @Override
    public String toString() {
        return "WelcomeMessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (messageTitle != null ? "messageTitle=" + messageTitle + ", " : "") +
                (body != null ? "body=" + body + ", " : "") +
                (footer != null ? "footer=" + footer + ", " : "") +
                (websiteUrl != null ? "websiteUrl=" + websiteUrl + ", " : "") +
                (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (guildsettingsId != null ? "guildsettingsId=" + guildsettingsId + ", " : "") +
            "}";
    }

}
