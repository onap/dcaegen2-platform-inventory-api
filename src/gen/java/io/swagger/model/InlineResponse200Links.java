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



/**
 * Pagination links
 **/

@ApiModel(description = "Pagination links")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class InlineResponse200Links   {
  
  private Link previousLink = null;
  private Link nextLink = null;

  /**
   **/
  public InlineResponse200Links previousLink(Link previousLink) {
    this.previousLink = previousLink;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("previousLink")
  public Link getPreviousLink() {
    return previousLink;
  }
  public void setPreviousLink(Link previousLink) {
    this.previousLink = previousLink;
  }

  /**
   **/
  public InlineResponse200Links nextLink(Link nextLink) {
    this.nextLink = nextLink;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("nextLink")
  public Link getNextLink() {
    return nextLink;
  }
  public void setNextLink(Link nextLink) {
    this.nextLink = nextLink;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse200Links inlineResponse200Links = (InlineResponse200Links) o;
    return Objects.equals(previousLink, inlineResponse200Links.previousLink) &&
        Objects.equals(nextLink, inlineResponse200Links.nextLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previousLink, nextLink);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200Links {\n");
    
    sb.append("    previousLink: ").append(toIndentedString(previousLink)).append("\n");
    sb.append("    nextLink: ").append(toIndentedString(nextLink)).append("\n");
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

