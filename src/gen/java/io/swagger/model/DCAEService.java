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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.DCAEServiceComponent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Link;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEService   {
  
  private String serviceId = null;
  private Link selfLink = null;
  private Date created = null;
  private Date modified = null;
  private Link typeLink = null;
  private String vnfId = null;
  private Link vnfLink = null;
  private String vnfType = null;
  private String vnfLocation = null;
  private String deploymentRef = null;
  private List<DCAEServiceComponent> components = new ArrayList<DCAEServiceComponent>();

  /**
   **/
  public DCAEService serviceId(String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  /**
   * Link.title is serviceId
   **/
  public DCAEService selfLink(Link selfLink) {
    this.selfLink = selfLink;
    return this;
  }

  
  @ApiModelProperty(value = "Link.title is serviceId")
  @JsonProperty("selfLink")
  public Link getSelfLink() {
    return selfLink;
  }
  public void setSelfLink(Link selfLink) {
    this.selfLink = selfLink;
  }

  /**
   **/
  public DCAEService created(Date created) {
    this.created = created;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("created")
  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   **/
  public DCAEService modified(Date modified) {
    this.modified = modified;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("modified")
  public Date getModified() {
    return modified;
  }
  public void setModified(Date modified) {
    this.modified = modified;
  }

  /**
   * Link.title is typeId
   **/
  public DCAEService typeLink(Link typeLink) {
    this.typeLink = typeLink;
    return this;
  }

  
  @ApiModelProperty(value = "Link.title is typeId")
  @JsonProperty("typeLink")
  public Link getTypeLink() {
    return typeLink;
  }
  public void setTypeLink(Link typeLink) {
    this.typeLink = typeLink;
  }

  /**
   **/
  public DCAEService vnfId(String vnfId) {
    this.vnfId = vnfId;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("vnfId")
  public String getVnfId() {
    return vnfId;
  }
  public void setVnfId(String vnfId) {
    this.vnfId = vnfId;
  }

  /**
   * Link.title is vnfId
   **/
  public DCAEService vnfLink(Link vnfLink) {
    this.vnfLink = vnfLink;
    return this;
  }

  
  @ApiModelProperty(value = "Link.title is vnfId")
  @JsonProperty("vnfLink")
  public Link getVnfLink() {
    return vnfLink;
  }
  public void setVnfLink(Link vnfLink) {
    this.vnfLink = vnfLink;
  }

  /**
   **/
  public DCAEService vnfType(String vnfType) {
    this.vnfType = vnfType;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("vnfType")
  public String getVnfType() {
    return vnfType;
  }
  public void setVnfType(String vnfType) {
    this.vnfType = vnfType;
  }

  /**
   * Location information of the associated VNF
   **/
  public DCAEService vnfLocation(String vnfLocation) {
    this.vnfLocation = vnfLocation;
    return this;
  }

  
  @ApiModelProperty(value = "Location information of the associated VNF")
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
  public DCAEService deploymentRef(String deploymentRef) {
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
  public DCAEService components(List<DCAEServiceComponent> components) {
    this.components = components;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("components")
  public List<DCAEServiceComponent> getComponents() {
    return components;
  }
  public void setComponents(List<DCAEServiceComponent> components) {
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
    DCAEService dCAEService = (DCAEService) o;
    return Objects.equals(serviceId, dCAEService.serviceId) &&
        Objects.equals(selfLink, dCAEService.selfLink) &&
        Objects.equals(created, dCAEService.created) &&
        Objects.equals(modified, dCAEService.modified) &&
        Objects.equals(typeLink, dCAEService.typeLink) &&
        Objects.equals(vnfId, dCAEService.vnfId) &&
        Objects.equals(vnfLink, dCAEService.vnfLink) &&
        Objects.equals(vnfType, dCAEService.vnfType) &&
        Objects.equals(vnfLocation, dCAEService.vnfLocation) &&
        Objects.equals(deploymentRef, dCAEService.deploymentRef) &&
        Objects.equals(components, dCAEService.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, selfLink, created, modified, typeLink, vnfId, vnfLink, vnfType, vnfLocation, deploymentRef, components);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCAEService {\n");
    
    sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
    sb.append("    selfLink: ").append(toIndentedString(selfLink)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    typeLink: ").append(toIndentedString(typeLink)).append("\n");
    sb.append("    vnfId: ").append(toIndentedString(vnfId)).append("\n");
    sb.append("    vnfLink: ").append(toIndentedString(vnfLink)).append("\n");
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

