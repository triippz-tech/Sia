
package com.trievosoftware.application.domain.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Rain {

    @SerializedName("1h")
    @Expose
    private Double _1h;

    public Double get1h() {
        return _1h;
    }

    public void set1h(Double _1h) {
        this._1h = _1h;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("_1h", _1h).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_1h).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Rain)) {
            return false;
        }
        Rain rhs = ((Rain) other);
        return new EqualsBuilder().append(_1h, rhs._1h).isEquals();
    }

}
