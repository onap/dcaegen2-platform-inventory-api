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
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEServiceComponentRequest   {

  @NotEmpty
  private String componentId = null;
  @NotEmpty
  private String componentType = null;
  @NotEmpty
  private String componentSource = null;
  @NotNull
  private Integer shareable = null;

  /**
   **/
  public DCAEServiceComponentRequest componentId(String componentId) {
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
   **/
  public DCAEServiceComponentRequest componentType(String componentType) {
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
   * Used to determine if this component can be shared amongst different DCAE services
   **/
  public DCAEServiceComponentRequest shareable(Integer shareable) {
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
    DCAEServiceComponentRequest dCAEServiceComponentRequest = (DCAEServiceComponentRequest) o;
    return Objects.equals(componentId, dCAEServiceComponentRequest.componentId) &&
        Objects.equals(componentType, dCAEServiceComponentRequest.componentType) &&
        Objects.equals(shareable, dCAEServiceComponentRequest.shareable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, componentType, shareable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCAEServiceComponentRequest {\n");
    
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    componentType: ").append(toIndentedString(componentType)).append("\n");
    sb.append("    componentSource: ").append(toIndentedString(componentSource)).append("\n");
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

