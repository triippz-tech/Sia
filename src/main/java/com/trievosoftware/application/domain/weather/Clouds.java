
/*
 *    Copyright 2019 Mark Tripoli
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.trievosoftware.application.domain.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Clouds {

    @SerializedName("all")
    @Expose
    private Long all;

    public Long getAll() {
        return all;
    }

    public void setAll(Long all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("all", all).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(all).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Clouds)) {
            return false;
        }
        Clouds rhs = ((Clouds) other);
        return new EqualsBuilder().append(all, rhs.all).isEquals();
    }

}
