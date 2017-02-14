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

package io.swagger.api;

import io.swagger.model.DCAEServiceRequest;
import org.joda.time.DateTime;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public abstract class DcaeServicesApiService {
      public abstract Response dcaeServicesGet(String typeId, String vnfId, String vnfType, String vnfLocation,
                                               String componentType, Boolean shareable, DateTime created, Integer offset,
                                               UriInfo uriInfo, SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response dcaeServicesServiceIdGet(String serviceId, UriInfo uriInfo, SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response dcaeServicesServiceIdPut(String serviceId, DCAEServiceRequest request, UriInfo uriInfo,
                                                        SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response dcaeServicesServiceIdDelete(String serviceId, SecurityContext securityContext)
              throws NotFoundException;
}
