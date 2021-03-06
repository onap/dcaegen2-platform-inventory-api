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

import io.swagger.model.DCAEServiceTypeRequest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public abstract class DcaeServiceTypesApiService {
      public abstract Response dcaeServiceTypesGet(String typeName, Boolean onlyLatest, Boolean onlyActive,
                                                   String vnfType, String serviceId, String serviceLocation,
                                                   String asdcServiceId, String asdcResourceId,
                                                   Integer offset, UriInfo uriInfo, SecurityContext securityContext, 
                                                   String application, String component, String owner)
      throws NotFoundException;
      public abstract Response dcaeServiceTypesTypeIdGet(String typeId, UriInfo uriInfo, SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response dcaeServiceTypesTypeIdPost(DCAEServiceTypeRequest request, UriInfo uriInfo,
                                                           SecurityContext securityContext)
      throws NotFoundException;
      public abstract Response dcaeServiceTypesTypeIdDelete(String typeId, UriInfo uriInfo, SecurityContext securityContext)
              throws NotFoundException;
}
