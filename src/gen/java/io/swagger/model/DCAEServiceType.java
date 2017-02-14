/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package io.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.core.Link;
import java.util.Date;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public final class DCAEServiceType extends DCAEServiceTypeRequest {

    @NotEmpty
    private String typeId = null;
    @NotEmpty
    private Link selfLink = null;
    @NotEmpty
    private Date created = null;
    private Date deactivated = null;


    @ApiModelProperty(required = true, value = "Unique identifier for this DCAE service type")
    @JsonProperty("typeId")
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }


    @ApiModelProperty(required = true, value = "Link to self where the Link.title is typeName")
    @JsonProperty("selfLink")
    public Link getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(Link selfLink) {
        this.selfLink = selfLink;
    }


    @ApiModelProperty(required = true, value = "Created timestamp for this DCAE service type in epoch time")
    @JsonProperty("created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    @ApiModelProperty(required = false, value = "Deactivated timestamp for this DCAE service type in epoch time")
    @JsonProperty("deactivated")
    public Date getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(Date deactivated) {
        this.deactivated = deactivated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DCAEServiceType dCAEServiceType = (DCAEServiceType) o;
        return Objects.equals(typeId, dCAEServiceType.typeId) &&
                Objects.equals(selfLink, dCAEServiceType.selfLink) &&
                Objects.equals(created, dCAEServiceType.created) &&
                Objects.equals(deactivated, dCAEServiceType.deactivated) &&
                super.equals((DCAEServiceTypeRequest) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, selfLink, created, deactivated) + super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DCAEServiceType {\n");

        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    selfLink: ").append(toIndentedString(selfLink)).append("\n");
        sb.append("    created: ").append(toIndentedString(created)).append("\n");
        sb.append("    deactivated: ").append(toIndentedString(deactivated)).append("\n");
        sb.append(super.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

