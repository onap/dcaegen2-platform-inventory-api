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

import io.swagger.api.factories.DcaeServicesGroupbyApiServiceFactory;

import io.swagger.annotations.ApiParam;

import io.swagger.model.DCAEServiceGroupByResults;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.UriInfo;

@Path("/dcae-services-groupby")
@Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
@Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
@io.swagger.annotations.Api(description = "the dcae-services-groupby API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServicesGroupbyApi {
    private final DcaeServicesGroupbyApiService delegate = DcaeServicesGroupbyApiServiceFactory.getDcaeServicesGroupbyApi();

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{propertyName}")
    @Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "Get a list of unique values for the given `propertyName`", response = DCAEServiceGroupByResults.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of unique property values", response = DCAEServiceGroupByResults.class)})
    public Response dcaeServicesGroupbyPropertyNameGet(
            @ApiParam(value = "Property to find unique values. Restricted to `type`, `vnfType`, `vnfLocation`", required = true) @PathParam("propertyName") String propertyName,
            @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.dcaeServicesGroupbyPropertyNameGet(propertyName, uriInfo, securityContext);
    }
}
