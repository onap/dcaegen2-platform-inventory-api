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

import io.swagger.api.factories.DcaeServiceTypesApiServiceFactory;

import io.swagger.annotations.ApiParam;

import io.swagger.model.InlineResponse200;
import io.swagger.model.DCAEServiceType;
import io.swagger.model.DCAEServiceTypeRequest;

import javax.validation.Valid;
import javax.ws.rs.core.*;
import javax.ws.rs.*;

@Path("/servicehealth")
@Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
@Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
@io.swagger.annotations.Api(description = "the dcae-service-types API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class ServiceHealthCheckApi {
    private final DcaeServiceTypesApiService delegate = DcaeServiceTypesApiServiceFactory.getDcaeServiceTypesApi();

    @Context
    UriInfo uriInfo;

    public static Link buildLinkForGet(UriInfo uriInfo, String rel, String typeName, Boolean onlyLatest, Boolean onlyActive,
                                       String vnfType, String serviceId, String serviceLocation, String asdcServiceId,
                                       String asdcResourceId, Integer offset, String application, String component, String owner) {
        UriBuilder ub = uriInfo.getBaseUriBuilder().path(ServiceHealthCheckApi.class)
                .path(ServiceHealthCheckApi.class, "dcaeServiceTypesGet");

        if (typeName != null) {
            ub.queryParam("typeName", typeName);
        }
        if (onlyLatest != null) {
            ub.queryParam("onlyLatest", onlyLatest);
        }
        if (onlyActive != null) {
            ub.queryParam("onlyActive", onlyActive);
        }
        if (vnfType != null) {
            ub.queryParam("vnfType", vnfType);
        }
        if (serviceId != null) {
            ub.queryParam("serviceId", serviceId);
        }
        if (serviceLocation != null) {
            ub.queryParam("serviceLocation", serviceLocation);
        }
        if (asdcServiceId != null) {
            ub.queryParam("asdcServiceId", asdcServiceId);
        }
        if (asdcResourceId != null) {
            ub.queryParam("asdcResourceId", asdcResourceId);
        }
        if (offset != null) {
            ub.queryParam("offset", offset);
        }
        if (application != null) {
            ub.queryParam("application", application);
        }
        if (component != null) {
            ub.queryParam("component", component);
        }
        if (owner != null) {
            ub.queryParam("owner", owner);
        }

        Link.Builder lb = Link.fromUri(ub.build());
        lb.rel(rel);
        return lb.build();
    }

    @GET
    @Path("/")
    @Consumes({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @Produces({"application/json", "application/vnd.dcae.inventory.v1+json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "Get a list of `DCAEServiceType` objects.", response = InlineResponse200.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of `DCAEServiceType` objects", response = InlineResponse200.class)})
    public Response dcaeServiceTypesGet(
            @ApiParam(value = "Filter by service type name. Wildcards matches with asterick(s) allowed.") @QueryParam("typeName") String typeName,
            @ApiParam(value = "If set to true, query returns just the latest versions of DCAE service types. If set to false, then all versions are returned. Default is true")
                @DefaultValue("true") @QueryParam("onlyLatest") Boolean onlyLatest,
            @ApiParam(value = "If set to true, query returns only *active* DCAE service types. If set to false, then all DCAE service types are returned. Default is true")
                @DefaultValue("true") @QueryParam("onlyActive") Boolean onlyActive,
            @ApiParam(value = "Filter by associated vnf type. No wildcards, matches are explicit. This field is treated case insensitive.")
                @QueryParam("vnfType") String vnfType,
            @ApiParam(value = "Filter by assocaited service id. Instances with service id null or empty is always returned.")
                @QueryParam("serviceId") String serviceId,
            @ApiParam(value = "Filter by associated service location. Instances with service location null or empty is always returned.")
                @QueryParam("serviceLocation") String serviceLocation,
            @ApiParam(value = "Filter by associated asdc design service id. Setting this to `NONE` will return instances that have asdc service id set to null")
                @QueryParam("asdcServiceId") String asdcServiceId,
            @ApiParam(value = "Filter by associated asdc design resource id. Setting this to `NONE` will return instances that have asdc resource id set to null")
                @QueryParam("asdcResourceId") String asdcResourceId,
            @ApiParam(value = "Query resultset offset used for pagination (zero-based)") @QueryParam("offset") Integer offset,
            @Context SecurityContext securityContext,
            @ApiParam(value = "Filter by associated application.") @QueryParam("application") String application,    
            @ApiParam(value = "Filter by associated component or sub-application module.") @QueryParam("component") String component,    
            @ApiParam(value = "Filter by associated owner.") @QueryParam("owner") String owner
    		)
            throws NotFoundException {
        return delegate.dcaeServiceTypesGet(typeName, onlyLatest, onlyActive, vnfType, serviceId, serviceLocation,
                asdcServiceId, asdcResourceId, offset, uriInfo, securityContext, application, component, owner);
    }

    public static Link buildLinkForGet(UriInfo uriInfo, String rel, String typeId) {
        // This same method can be used for PUTs as well

        UriBuilder ub = uriInfo.getBaseUriBuilder().path(ServiceHealthCheckApi.class)
                .path(ServiceHealthCheckApi.class, "dcaeServiceTypesTypeIdGet");
        Link.Builder lb = Link.fromUri(ub.build(typeId));
        lb.rel(rel);
        return lb.build();
    }

}
