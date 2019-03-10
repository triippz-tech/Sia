
package com.trievosoftware.application.domain.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Weather {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    private String iconLink;

    public String getIconLink() {
        return "http://openweathermap.org/img/w/" + icon + ".png";
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("main", main).append("description", description).append("icon", icon).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(icon).append(description).append(main).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Weather)) {
            return false;
        }
        Weather rhs = ((Weather) other);
        return new EqualsBuilder().append(id, rhs.id).append(icon, rhs.icon).append(description, rhs.description).append(main, rhs.main).isEquals();
    }

}
