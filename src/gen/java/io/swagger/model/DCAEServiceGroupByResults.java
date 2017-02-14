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
import io.swagger.model.DCAEServiceGroupByResultsPropertyValues;
import java.util.ArrayList;
import java.util.List;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEServiceGroupByResults   {
  
  private String propertyName = null;
  private List<DCAEServiceGroupByResultsPropertyValues> propertyValues = new ArrayList<DCAEServiceGroupByResultsPropertyValues>();

  /**
   * Property name of DCAE service that the group by operation was performed on
   **/
  public DCAEServiceGroupByResults propertyName(String propertyName) {
    this.propertyName = propertyName;
    return this;
  }

  
  @ApiModelProperty(value = "Property name of DCAE service that the group by operation was performed on")
  @JsonProperty("propertyName")
  public String getPropertyName() {
    return propertyName;
  }
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   **/
  public DCAEServiceGroupByResults propertyValues(List<DCAEServiceGroupByResultsPropertyValues> propertyValues) {
    this.propertyValues = propertyValues;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("propertyValues")
  public List<DCAEServiceGroupByResultsPropertyValues> getPropertyValues() {
    return propertyValues;
  }
  public void setPropertyValues(List<DCAEServiceGroupByResultsPropertyValues> propertyValues) {
    this.propertyValues = propertyValues;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCAEServiceGroupByResults dCAEServiceGroupByResults = (DCAEServiceGroupByResults) o;
    return Objects.equals(propertyName, dCAEServiceGroupByResults.propertyName) &&
        Objects.equals(propertyValues, dCAEServiceGroupByResults.propertyValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(propertyName, propertyValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCAEServiceGroupByResults {\n");
    
    sb.append("    propertyName: ").append(toIndentedString(propertyName)).append("\n");
    sb.append("    propertyValues: ").append(toIndentedString(propertyValues)).append("\n");
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

