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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the GuildEvent entity. This class is used in GuildEventResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /guild-events?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildEventCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventName;

    private StringFilter eventImageUrl;

    private InstantFilter eventStart;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public StringFilter getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(StringFilter eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public InstantFilter getEventStart() {
        return eventStart;
    }

    public void setEventStart(InstantFilter eventStart) {
        this.eventStart = eventStart;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildEventCriteria that = (GuildEventCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventImageUrl, that.eventImageUrl) &&
            Objects.equals(eventStart, that.eventStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        eventName,
        eventImageUrl,
        eventStart
        );
    }

    @Override
    public String toString() {
        return "GuildEventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (eventName != null ? "eventName=" + eventName + ", " : "") +
                (eventImageUrl != null ? "eventImageUrl=" + eventImageUrl + ", " : "") +
                (eventStart != null ? "eventStart=" + eventStart + ", " : "") +
            "}";
    }

}
