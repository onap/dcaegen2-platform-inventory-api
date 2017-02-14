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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEServiceTypeRequest {

    @NotEmpty
    private String owner = null;
    @NotEmpty
    private String typeName = null;
    @NotNull
    private Integer typeVersion = 1;
    @NotEmpty
    private String blueprintTemplate = null;

    // REVIEW: Look to deprecate these depending upon how the ASDC integration goes
    private List<String> serviceIds = null;
    private List<String> vnfTypes = new ArrayList<String>();

    private List<String> serviceLocations = null;

    // TODO: This should eventually become required fields
    private String asdcServiceId = null;
    private String asdcResourceId = null;
    private String asdcServiceURL = null;


    /**
     **/
    public DCAEServiceTypeRequest owner(String owner) {
        this.owner = owner;
        return this;
    }


    @ApiModelProperty(required = true, value = "")
    @JsonProperty("owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    @ApiModelProperty(required = true, value = "Descriptive name for this DCAE service type")
    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    @ApiModelProperty(required = true, value = "Version number for this DCAE service type")
    @JsonProperty("typeVersion")
    public Integer getTypeVersion() {
        return this.typeVersion;
    }

    public void setTypeVersion(Integer typeVersion) {
        this.typeVersion = typeVersion;
    }

    /**
     **/
    public DCAEServiceTypeRequest vnfTypes(List<String> vnfTypes) {
        this.vnfTypes = vnfTypes;
        return this;
    }


    @ApiModelProperty(required = false, value = "")
    @JsonProperty("vnfTypes")
    public List<String> getVnfTypes() {
        return vnfTypes;
    }

    public void setVnfTypes(List<String> vnfTypes) {
        this.vnfTypes = vnfTypes;
    }

    /**
     * String representation of a Cloudify blueprint with unbound variables
     **/
    public DCAEServiceTypeRequest blueprintTemplate(String blueprintTemplate) {
        this.blueprintTemplate = blueprintTemplate;
        return this;
    }


    @ApiModelProperty(required = true, value = "String representation of a Cloudify blueprint with unbound variables")
    @JsonProperty("blueprintTemplate")
    public String getBlueprintTemplate() {
        return blueprintTemplate;
    }

    public void setBlueprintTemplate(String blueprintTemplate) {
        this.blueprintTemplate = blueprintTemplate;
    }

    @ApiModelProperty(required = false, value = "List of service locations that are used to associate with DCAE service type. DCAE service types with this propery as null or empty means them apply for every service location.")
    @JsonProperty("serviceLocations")
    public List<String> getServiceLocations() {
        return this.serviceLocations;
    }

    public void setServiceLocations(List<String> serviceLocations) {
        this.serviceLocations = serviceLocations;
    }

    @ApiModelProperty(required = false, value = "List of service ids that are used to associate with DCAE service type. DCAE service types with this propery as null or empty means them apply for every service id.")
    @JsonProperty("serviceIds")
    public List<String> getServiceIds() {
        return this.serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
        this.serviceIds = serviceIds;
    }

    @ApiModelProperty(required = false, value = "Id of service this DCAE service type is associated with. Value source is from ASDC's notification event's field `serviceInvariantUUID`.")
    @JsonProperty("asdcServiceId")
    public String getAsdcServiceId() {
        return asdcServiceId;
    }

    public void setAsdcServiceId(String asdcServiceId) {
        this.asdcServiceId = asdcServiceId;
    }

    @ApiModelProperty(required = false, value = "Id of vf/vnf instance this DCAE service type is associated with. Value source is from ASDC's notification event's field `resourceInvariantUUID`.")
    @JsonProperty("asdcResourceId")
    public String getAsdcResourceId() {
        return asdcResourceId;
    }

    public void setAsdcResourceId(String asdcResourceId) {
        this.asdcResourceId = asdcResourceId;
    }

    @ApiModelProperty(required = false, value = "URL to the ASDC service model")
    @JsonProperty("asdcServiceURL")
    public String getAsdcServiceURL() {
        return asdcServiceURL;
    }

    public void setAsdcServiceURL(String asdcServiceURL) {
        this.asdcServiceURL = asdcServiceURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DCAEServiceTypeRequest dCAEServiceTypeRequest = (DCAEServiceTypeRequest) o;
        return Objects.equals(owner, dCAEServiceTypeRequest.owner) &&
                Objects.equals(vnfTypes, dCAEServiceTypeRequest.vnfTypes) &&
                Objects.equals(blueprintTemplate, dCAEServiceTypeRequest.blueprintTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, vnfTypes, blueprintTemplate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DCAEServiceTypeRequest {\n");

        sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
        sb.append("    vnfTypes: ").append(toIndentedString(vnfTypes)).append("\n");
        sb.append("    blueprintTemplate: ").append(toIndentedString(blueprintTemplate)).append("\n");
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

