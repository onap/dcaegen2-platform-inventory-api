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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEServiceRequest   {

  @NotEmpty
  private String typeId = null;
  @NotEmpty
  private String vnfId = null;
  @NotEmpty
  private String vnfType = null;
  @NotEmpty
  private String vnfLocation = null;
  private String deploymentRef = null;
  @NotEmpty
  @Valid
  private List<DCAEServiceComponentRequest> components = new ArrayList<DCAEServiceComponentRequest>();

  /**
   **/
  public DCAEServiceRequest typeId(String typeId) {
    this.typeId = typeId;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Id of the associated DCAE service type")
  @JsonProperty("typeId")
  public String getTypeId() {
    return typeId;
  }
  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  /**
   **/
  public DCAEServiceRequest vnfId(String vnfId) {
    this.vnfId = vnfId;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Id of the associated VNF that this service is monitoring")
  @JsonProperty("vnfId")
  public String getVnfId() {
    return vnfId;
  }
  public void setVnfId(String vnfId) {
    this.vnfId = vnfId;
  }

  /**
   **/
  public DCAEServiceRequest vnfType(String vnfType) {
    this.vnfType = vnfType;
    return this;
  }


  @ApiModelProperty(required = true, value = "The type of the associated VNF that this service is monitoring")
  @JsonProperty("vnfType")
  public String getVnfType() {
    return vnfType;
  }
  public void setVnfType(String vnfType) {
    this.vnfType = vnfType;
  }

  /**
   **/
  public DCAEServiceRequest vnfLocation(String vnfLocation) {
    this.vnfLocation = vnfLocation;
    return this;
  }


  @ApiModelProperty(required = true, value = "Location identifier of the associated VNF that this service is monitoring")
  @JsonProperty("vnfLocation")
  public String getVnfLocation() {
    return vnfLocation;
  }
  public void setVnfLocation(String vnfLocation) {
    this.vnfLocation = vnfLocation;
  }

  /**
   * Reference to a Cloudify deployment
   **/
  public DCAEServiceRequest deploymentRef(String deploymentRef) {
    this.deploymentRef = deploymentRef;
    return this;
  }

  
  @ApiModelProperty(value = "Reference to a Cloudify deployment")
  @JsonProperty("deploymentRef")
  public String getDeploymentRef() {
    return deploymentRef;
  }
  public void setDeploymentRef(String deploymentRef) {
    this.deploymentRef = deploymentRef;
  }

  /**
   **/
  public DCAEServiceRequest components(List<DCAEServiceComponentRequest> components) {
    this.components = components;
    return this;
  }


  @ApiModelProperty(required = true, value = "List of DCAE service components that this service is composed of")
  @JsonProperty("components")
  public List<DCAEServiceComponentRequest> getComponents() {
    return components;
  }
  public void setComponents(List<DCAEServiceComponentRequest> components) {
    this.components = components;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCAEServiceRequest dCAEServiceRequest = (DCAEServiceRequest) o;
    return Objects.equals(typeId, dCAEServiceRequest.typeId) &&
        Objects.equals(vnfId, dCAEServiceRequest.vnfId) &&
        Objects.equals(vnfType, dCAEServiceRequest.vnfType) &&
        Objects.equals(vnfLocation, dCAEServiceRequest.vnfLocation) &&
        Objects.equals(deploymentRef, dCAEServiceRequest.deploymentRef) &&
        Objects.equals(components, dCAEServiceRequest.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeId, vnfId, vnfType, vnfLocation, deploymentRef, components);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCAEServiceRequest {\n");
    
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    vnfId: ").append(toIndentedString(vnfId)).append("\n");
    sb.append("    vnfType: ").append(toIndentedString(vnfType)).append("\n");
    sb.append("    vnfLocation: ").append(toIndentedString(vnfLocation)).append("\n");
    sb.append("    deploymentRef: ").append(toIndentedString(deploymentRef)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
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

