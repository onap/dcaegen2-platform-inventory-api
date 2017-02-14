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
import javax.ws.rs.core.Link;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DCAEServiceGroupByResultsPropertyValues   {
  
  private Integer count = null;
  private String propertyValue = null;
  private Link dcaeServiceQueryLink = null;

  /**
   **/
  public DCAEServiceGroupByResultsPropertyValues count(Integer count) {
    this.count = count;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("count")
  public Integer getCount() {
    return count;
  }
  public void setCount(Integer count) {
    this.count = count;
  }

  /**
   **/
  public DCAEServiceGroupByResultsPropertyValues propertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("propertyValue")
  public String getPropertyValue() {
    return propertyValue;
  }
  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

  /**
   * Link.title is the DCAE service property value. Following this link will provide a list of DCAE services that all have this property value.
   **/
  public DCAEServiceGroupByResultsPropertyValues dcaeServiceQueryLink(Link dcaeServiceQueryLink) {
    this.dcaeServiceQueryLink = dcaeServiceQueryLink;
    return this;
  }

  
  @ApiModelProperty(value = "Link.title is the DCAE service property value. Following this link will provide a list of DCAE services that all have this property value.")
  @JsonProperty("dcaeServiceQueryLink")
  public Link getDcaeServiceQueryLink() {
    return dcaeServiceQueryLink;
  }
  public void setDcaeServiceQueryLink(Link dcaeServiceQueryLink) {
    this.dcaeServiceQueryLink = dcaeServiceQueryLink;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCAEServiceGroupByResultsPropertyValues dCAEServiceGroupByResultsPropertyValues = (DCAEServiceGroupByResultsPropertyValues) o;
    return Objects.equals(count, dCAEServiceGroupByResultsPropertyValues.count) &&
        Objects.equals(propertyValue, dCAEServiceGroupByResultsPropertyValues.propertyValue) &&
        Objects.equals(dcaeServiceQueryLink, dCAEServiceGroupByResultsPropertyValues.dcaeServiceQueryLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(count, propertyValue, dcaeServiceQueryLink);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCAEServiceGroupByResultsPropertyValues {\n");
    
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    propertyValue: ").append(toIndentedString(propertyValue)).append("\n");
    sb.append("    dcaeServiceQueryLink: ").append(toIndentedString(dcaeServiceQueryLink)).append("\n");
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

