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

import io.dropwizard.jersey.params.DateTimeParam;
import io.swagger.api.factories.DcaeServicesApiServiceFactory;

import io.swagger.annotations.ApiParam;

import io.swagger.model.InlineResponse2001;
import io.swagger.model.DCAEService;
import io.swagger.model.DCAEServiceRequest;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.ws.rs.core.*;
import javax.ws.rs.*;

@Path("/dcae-services")
@Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
@Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
@io.swagger.annotations.Api(description = "the dcae-services API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServicesApi {
    private final DcaeServicesApiService delegate = DcaeServicesApiServiceFactory.getDcaeServicesApi();

    @Context
    UriInfo uriInfo;

    public static Link buildLinkForGetByTypeId(UriInfo uriInfo, String rel, String typeId) {
        return buildLinkForGet(uriInfo, rel, typeId, null, null, null, null, null, null, null);
    }

    public static Link buildLinkForGetByVnfType(UriInfo uriInfo, String rel, String vnfType) {
        return buildLinkForGet(uriInfo, rel, null, null, vnfType, null, null, null, null, null);
    }

    public static Link buildLinkForGetByVnfLocation(UriInfo uriInfo, String rel, String vnfLocation) {
        return buildLinkForGet(uriInfo, rel, null, null, null, vnfLocation, null, null, null, null);
    }

    public static Link buildLinkForGet(UriInfo uriInfo, String rel, String typeId, String vnfId, String vnfType,
                                       String vnfLocation, String componentType, Boolean shareable, DateTime created,
                                       Integer offset) {
        UriBuilder ub = uriInfo.getBaseUriBuilder().path(DcaeServicesApi.class)
                .path(DcaeServicesApi.class, "dcaeServicesGet");

        if (typeId != null) {
            ub.queryParam("typeId", typeId);
        }
        if (vnfId != null) {
            ub.queryParam("vnfId", vnfId);
        }
        if (vnfType != null) {
            ub.queryParam("vnfType", vnfType);
        }
        if (vnfLocation != null) {
            ub.queryParam("vnfLocation", vnfLocation);
        }
        if (componentType != null) {
            ub.queryParam("componentType", componentType);
        }
        if (shareable != null) {
            ub.queryParam("shareable", shareable.toString());
        }
        if (created != null) {
            ub.queryParam("created", created.toString());
        }
        if (offset != null) {
            ub.queryParam("offset", offset);
        }

        Link.Builder lb = Link.fromUri(ub.build());
        lb.rel(rel);
        return lb.build();
    }

    @GET
    @Path("/")
    @Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "Get a list of `DCAEService` objects.", response = InlineResponse2001.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of `DCAEService` objects", response = InlineResponse2001.class),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad response from DCAE controller", response = ApiResponseMessage.class),
            @io.swagger.annotations.ApiResponse(code = 504, message = "Failed to connect with DCAE controller", response = ApiResponseMessage.class)})
    public Response dcaeServicesGet(
            @ApiParam(value = "DCAE service type name") @QueryParam("typeId") String typeId,
            @ApiParam(value = "") @QueryParam("vnfId") String vnfId,
            @ApiParam(value = "Filter by associated vnf type. This field is treated case insensitive.")
                @QueryParam("vnfType") String vnfType,
            @ApiParam(value = "") @QueryParam("vnfLocation") String vnfLocation,
            @ApiParam(value = "Use to filter by a specific DCAE service component type") @QueryParam("componentType") String componentType,
            @ApiParam(value = "Use to filter by DCAE services that have shareable components or not") @QueryParam("shareable") Boolean shareable,
            @ApiParam(value = "Use to filter by created time") @QueryParam("created") DateTimeParam created,
            @ApiParam(value = "Query resultset offset used for pagination (zero-based)") @QueryParam("offset") Integer offset,
            @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.dcaeServicesGet(typeId, vnfId, vnfType, vnfLocation, componentType, shareable,
                (created == null ? null : created.get()), offset, uriInfo, securityContext);
    }

    public static Link buildLinkForGet(UriInfo uriInfo, String rel, String serviceId) {
        // This same method can be used for PUTs as well

        UriBuilder ub = uriInfo.getBaseUriBuilder().path(DcaeServicesApi.class)
                .path(DcaeServicesApi.class, "dcaeServicesServiceIdGet");
        Link.Builder lb = Link.fromUri(ub.build(serviceId));
        lb.rel(rel);
        return lb.build();
    }

    @GET
    @Path("/{serviceId}")
    @Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "Get a `DCAEService` object.", response = DCAEService.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Single `DCAEService` object", response = DCAEService.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "DCAE service not found", response = ApiResponseMessage.class),
            @io.swagger.annotations.ApiResponse(code = 502, message = "Bad response from DCAE controller", response = ApiResponseMessage.class),
            @io.swagger.annotations.ApiResponse(code = 504, message = "Failed to connect with DCAE controller", response = ApiResponseMessage.class)})
    public Response dcaeServicesServiceIdGet(
            @ApiParam(value = "", required = true) @PathParam("serviceId") String serviceId,
            @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.dcaeServicesServiceIdGet(serviceId, uriInfo, securityContext);
    }

    @PUT
    @Path("/{serviceId}")
    @Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "Put a new or update an existing `DCAEService` object.", response = DCAEService.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Single `DCAEService` object", response = DCAEService.class),
            @io.swagger.annotations.ApiResponse(code = 422, message = "Bad request provided", response = ApiResponseMessage.class)})
    public Response dcaeServicesServiceIdPut(
            @ApiParam(value = "", required = true) @PathParam("serviceId") String serviceId,
            @ApiParam(value = "", required = true) @Valid DCAEServiceRequest request,
            @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.dcaeServicesServiceIdPut(serviceId, request, uriInfo, securityContext);
    }

    @DELETE
    @Path("/{serviceId}")
    @Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "Remove an existing `DCAEService` object.", tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "DCAE service has been removed"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Unknown DCAE service", response = ApiResponseMessage.class)})
    public Response dcaeServicesServiceIdDelete(
            @ApiParam(value = "", required = true) @PathParam("serviceId") String serviceId,
            @Context SecurityContext securityContext)
            throws NotFoundException
    {
        return delegate.dcaeServicesServiceIdDelete(serviceId, securityContext);
    }
}
