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
import java.util.Date;
import javax.ws.rs.core.Link;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEServiceComponent   {
  
  private String componentId = null;
  private Link componentLink = null;
  private Date created = null;
  private Date modified = null;
  private String componentType = null;
  private String componentSource = null;
  private String status = null;
  private String location = null;
  private Integer shareable = null;

  /**
   **/
  public DCAEServiceComponent componentId(String componentId) {
    this.componentId = componentId;
    return this;
  }

  
  @ApiModelProperty(value = "The id format is unique to the source", required = true)
  @JsonProperty("componentId")
  public String getComponentId() {
    return componentId;
  }
  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  /**
   * Link.title is componentId
   **/
  public DCAEServiceComponent componentLink(Link componentLink) {
    this.componentLink = componentLink;
    return this;
  }

  
  @ApiModelProperty(value = "Link to the underlying resource of this component", required = true)
  @JsonProperty("componentLink")
  public Link getComponentLink() {
    return componentLink;
  }
  public void setComponentLink(Link componentLink) {
    this.componentLink = componentLink;
  }

  /**
   **/
  public DCAEServiceComponent created(Date created) {
    this.created = created;
    return this;
  }

  
  @ApiModelProperty(value = "", required = true)
  @JsonProperty("created")
  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   **/
  public DCAEServiceComponent modified(Date modified) {
    this.modified = modified;
    return this;
  }

  
  @ApiModelProperty(value = "", required = true)
  @JsonProperty("modified")
  public Date getModified() {
    return modified;
  }
  public void setModified(Date modified) {
    this.modified = modified;
  }

  /**
   **/
  public DCAEServiceComponent componentType(String componentType) {
    this.componentType = componentType;
    return this;
  }

  
  @ApiModelProperty(value = "", required = true)
  @JsonProperty("componentType")
  public String getComponentType() {
    return componentType;
  }
  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }


  @ApiModelProperty(value = "Specifies the name of the underying source service that is responsible for this components", required = true,
          allowableValues = "DCAEController, DMaaPController")
  @JsonProperty("componentSource")
  public String getComponentSource() {
    return componentSource;
  }
  public void setComponentSource(String componentSource) {
    this.componentSource = componentSource;
  }

  /**
   **/
  public DCAEServiceComponent status(String status) {
    this.status = status;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Location information of the component
   **/
  public DCAEServiceComponent location(String location) {
    this.location = location;
    return this;
  }

  
  @ApiModelProperty(value = "Location information of the component")
  @JsonProperty("location")
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Used to determine if this component can be shared amongst different DCAE services
   **/
  public DCAEServiceComponent shareable(Integer shareable) {
    this.shareable = shareable;
    return this;
  }

  
  @ApiModelProperty(value = "Used to determine if this component can be shared amongst different DCAE services", required = true)
  @JsonProperty("shareable")
  public Integer getShareable() {
    return shareable;
  }
  public void setShareable(Integer shareable) {
    this.shareable = shareable;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCAEServiceComponent dCAEServiceComponent = (DCAEServiceComponent) o;
    return Objects.equals(componentId, dCAEServiceComponent.componentId) &&
        Objects.equals(componentLink, dCAEServiceComponent.componentLink) &&
        Objects.equals(created, dCAEServiceComponent.created) &&
        Objects.equals(modified, dCAEServiceComponent.modified) &&
        Objects.equals(componentType, dCAEServiceComponent.componentType) &&
        Objects.equals(status, dCAEServiceComponent.status) &&
        Objects.equals(location, dCAEServiceComponent.location) &&
        Objects.equals(shareable, dCAEServiceComponent.shareable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, componentLink, created, modified, componentType, status, location, shareable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCAEServiceComponent {\n");
    
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    componentLink: ").append(toIndentedString(componentLink)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    componentType: ").append(toIndentedString(componentType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    shareable: ").append(toIndentedString(shareable)).append("\n");
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

