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
 * Criteria class for the CustomCommand entity. This class is used in CustomCommandResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /custom-commands?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomCommandCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter guildId;

    private StringFilter commandName;

    private LongFilter guildrolesId;

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

    public StringFilter getCommandName() {
        return commandName;
    }

    public void setCommandName(StringFilter commandName) {
        this.commandName = commandName;
    }

    public LongFilter getGuildrolesId() {
        return guildrolesId;
    }

    public void setGuildrolesId(LongFilter guildrolesId) {
        this.guildrolesId = guildrolesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomCommandCriteria that = (CustomCommandCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(commandName, that.commandName) &&
            Objects.equals(guildrolesId, that.guildrolesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        guildId,
        commandName,
        guildrolesId
        );
    }

    @Override
    public String toString() {
        return "CustomCommandCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (commandName != null ? "commandName=" + commandName + ", " : "") +
                (guildrolesId != null ? "guildrolesId=" + guildrolesId + ", " : "") +
            "}";
    }

}
