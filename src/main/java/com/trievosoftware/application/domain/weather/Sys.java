
package com.trievosoftware.application.domain.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Sys {

    @SerializedName("type")
    @Expose
    private Long type;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private Long sunrise;
    @SerializedName("sunset")
    @Expose
    private Long sunset;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("id", id).append("message", message).append("country", country).append("sunrise", sunrise).append("sunset", sunset).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(message).append(id).append(sunset).append(sunrise).append(type).append(country).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Sys)) {
            return false;
        }
        Sys rhs = ((Sys) other);
        return new EqualsBuilder().append(message, rhs.message).append(id, rhs.id).append(sunset, rhs.sunset).append(sunrise, rhs.sunrise).append(type, rhs.type).append(country, rhs.country).isEquals();
    }

}
