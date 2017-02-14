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

package io.swagger.api.impl;

import org.openecomp.dcae.inventory.daos.DCAEServiceTypesDAO;
import org.openecomp.dcae.inventory.daos.DCAEServicesDAO;
import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import org.openecomp.dcae.inventory.dbthings.mappers.DCAEServiceTypeObjectMapper;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceTypeObject;
import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.api.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServiceTypesApiServiceImpl extends DcaeServiceTypesApiService {

    private final static Logger LOG = LoggerFactory.getLogger(DcaeServiceTypesApiServiceImpl.class);
    private static int PAGINATION_PAGE_SIZE = 25;

    private DCAEServiceType createDCAEServiceType(DCAEServiceTypeObject serviceTypeObject, UriInfo uriInfo) {
        DCAEServiceType serviceType = new DCAEServiceType();
        serviceType.setSelfLink(DcaeServiceTypesApi.buildLinkForGet(uriInfo, "self", serviceTypeObject.getTypeId()));
        serviceType.setTypeId(serviceTypeObject.getTypeId());
        serviceType.setTypeName(serviceTypeObject.getTypeName());
        serviceType.setTypeVersion(serviceTypeObject.getTypeVersion());
        serviceType.setOwner(serviceTypeObject.getOwner());
        serviceType.setVnfTypes(serviceTypeObject.getVnfTypes());
        serviceType.setServiceIds(serviceTypeObject.getServiceIds());
        serviceType.setServiceLocations(serviceTypeObject.getServiceLocations());
        serviceType.setBlueprintTemplate(serviceTypeObject.getBlueprintTemplate());
        serviceType.setAsdcServiceId(serviceTypeObject.getAsdcServiceId());
        serviceType.setAsdcResourceId(serviceTypeObject.getAsdcResourceId());
        // TODO: Construct ASDC service URL somehow
        serviceType.setCreated(serviceTypeObject.getCreated().toDate());

        DateTime deactivated = serviceTypeObject.getDeactivated();
        serviceType.setDeactivated(deactivated == null ? null : deactivated.toDate());

        return serviceType;
    }

    @Override
    public Response dcaeServiceTypesGet(String typeName, Boolean onlyLatest, Boolean onlyActive, String vnfType,
                                        String serviceId, String serviceLocation, String asdcServiceId,
                                        String asdcResourceId, Integer offset, UriInfo uriInfo, SecurityContext securityContext)
            throws NotFoundException {
        List<DCAEServiceTypeObject> serviceTypeObjects = new ArrayList<>();

        // TODO: Make this variable also a URL parameter.
        DateTime createdCutoff = DateTime.now(DateTimeZone.UTC);

        try (Handle jdbiHandle = InventoryDAOManager.getInstance().getHandle()) {
            StringBuilder sb = new StringBuilder("select * from ");

            if (onlyLatest) {
                // Use the view which filters types that are of only the latest versions
                sb.append("dcae_service_types_latest");
            } else {
                sb.append("dcae_service_types");
            }

            List<String> whereClauses = new ArrayList<String>();

            if (typeName != null) {
                whereClauses.add(":typeName = type_name");
            }

            if (vnfType != null) {
                whereClauses.add("lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[])");
            }

            if (serviceId != null) {
                whereClauses.add("(:serviceId = any(service_ids) or service_ids = \'{}\' or service_ids is null)");
            }

            if (serviceLocation != null) {
                whereClauses.add("(:serviceLocation = any(service_locations) or service_locations = \'{}\' or service_locations is null)");
            }

            if (asdcServiceId != null) {
                if ("NONE".equals(asdcServiceId.toUpperCase(Locale.ENGLISH))) {
                    whereClauses.add("asdc_service_id is null");
                } else {
                    whereClauses.add(":asdcServiceId = asdc_service_id");
                }
            }

            if (asdcResourceId != null) {
                if ("NONE".equals(asdcResourceId.toUpperCase(Locale.ENGLISH))) {
                    whereClauses.add("asdc_resource_id is null");
                } else {
                    whereClauses.add(":asdcResourceId = asdc_resource_id");
                }
            }

            whereClauses.add("created < :createdCutoff");

            if (onlyActive) {
                whereClauses.add("deactivated is null");
            }

            if (!whereClauses.isEmpty()) {
                sb.append(" where ");
                sb.append(String.join(" and ", whereClauses));
            }

            // Sort by created timestamp - always descending.
            sb.append(" order by created desc");

            Query<DCAEServiceTypeObject> query = jdbiHandle.createQuery(sb.toString()).map(new DCAEServiceTypeObjectMapper());

            if (typeName != null) {
                query.bind("typeName", typeName);
            }

            if (vnfType != null) {
                query.bind("vnfType", vnfType);
            }

            if (serviceId != null) {
                query.bind("serviceId", serviceId);
            }

            if (serviceLocation != null) {
                query.bind("serviceLocation", serviceLocation);
            }

            if (asdcServiceId != null && !"NONE".equals(asdcServiceId.toUpperCase(Locale.ENGLISH))) {
                query.bind("asdcServiceId", asdcServiceId);
            }

            if (asdcResourceId != null && !"NONE".equals(asdcResourceId.toUpperCase(Locale.ENGLISH))) {
                query.bind("asdcResourceId", asdcResourceId);
            }

            query.bind("createdCutoff", createdCutoff);

            serviceTypeObjects = query.list();
        }

        offset = (offset == null) ? 0 : offset;

        Integer totalCount = serviceTypeObjects.size();

        // See comment in DcaeServicesApiServiceImpl.java above similar code.
        Integer endpoint = Math.min(offset + PAGINATION_PAGE_SIZE, totalCount);
        List<DCAEServiceTypeObject> serviceTypeObjectsSliced = serviceTypeObjects.subList(offset, endpoint);

        List<DCAEServiceType> serviceTypes = new ArrayList<>();

        for (DCAEServiceTypeObject serviceTypeObject : serviceTypeObjectsSliced) {
            serviceTypes.add(createDCAEServiceType(serviceTypeObject, uriInfo));
        }

        InlineResponse200 response = new InlineResponse200();
        response.setItems(serviceTypes);
        response.setTotalCount(totalCount);

        InlineResponse200Links navigationLinks = new InlineResponse200Links();
        Integer offsetPrev = offset - PAGINATION_PAGE_SIZE;

        // TODO: MUST UPDATE THIS LINK NAV CODE

        if (offsetPrev >= 0) {
            navigationLinks.setPreviousLink(DcaeServiceTypesApi.buildLinkForGet(uriInfo, "prev", typeName, onlyLatest,
                    onlyActive, vnfType, serviceId, serviceLocation, asdcServiceId, asdcResourceId, offsetPrev));
        }

        Integer offsetNext = offset + PAGINATION_PAGE_SIZE;

        if (offsetNext < totalCount) {
            navigationLinks.setNextLink(DcaeServiceTypesApi.buildLinkForGet(uriInfo, "next", typeName, onlyLatest,
                    onlyActive, vnfType, serviceId, serviceLocation, asdcServiceId, asdcResourceId, offsetNext));
        }

        response.setLinks(navigationLinks);

        return Response.ok().entity(response).build();
    }

    @Override
    public Response dcaeServiceTypesTypeIdGet(String typeId, UriInfo uriInfo, SecurityContext securityContext)
            throws NotFoundException {
        DCAEServiceTypesDAO serviceTypesDAO = InventoryDAOManager.getInstance().getDCAEServiceTypesDAO();
        DCAEServiceTypeObject serviceTypeObject = serviceTypesDAO.getByTypeId(typeId);

        if (serviceTypeObject == null) {
           return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(createDCAEServiceType(serviceTypeObject, uriInfo)).build();
    }

    /**
     * Create a DCAE service type database object
     *
     * Utility method that takes a DCAE service type request body and creates a database object representation
     * to insert.
     *
     * @param typeId
     * @param request
     * @return
     */
    private static DCAEServiceTypeObject createDCAEServiceTypeDBO(String typeId, DCAEServiceTypeRequest request) {
        DCAEServiceTypeObject serviceTypeObject = new DCAEServiceTypeObject();
        serviceTypeObject.setTypeId(typeId);
        serviceTypeObject.setTypeName(request.getTypeName());
        serviceTypeObject.setTypeVersion(request.getTypeVersion());
        serviceTypeObject.setOwner(request.getOwner());
        serviceTypeObject.setBlueprintTemplate(request.getBlueprintTemplate());
        serviceTypeObject.setVnfTypes(request.getVnfTypes());
        serviceTypeObject.setServiceIds(request.getServiceIds());
        serviceTypeObject.setServiceLocations(request.getServiceLocations());
        serviceTypeObject.setAsdcServiceId(request.getAsdcServiceId());
        serviceTypeObject.setAsdcResourceId(request.getAsdcResourceId());
        serviceTypeObject.setCreated(DateTime.now(DateTimeZone.UTC));

        return serviceTypeObject;
    }

    @Override
    public Response dcaeServiceTypesTypeIdPost(DCAEServiceTypeRequest request, UriInfo uriInfo,
                                                SecurityContext securityContext) {
        DCAEServiceTypesDAO serviceTypesDAO = InventoryDAOManager.getInstance().getDCAEServiceTypesDAO();
        // Must query by the implicit composite key: type name, type version, asdc service id, asdc resource id
        // Had to split up the queries into two because in SQL selecting by null has to be `some_field is null`
        //
        // FIXME: There is a race condition here where there could be multiple records with different ids
        // for the same implicit composite key. Maybe the answer is to simply add back in a PUT.
        DCAEServiceTypeObject serviceTypeObject
                = (request.getAsdcServiceId() == null || request.getAsdcResourceId() == null)
                ? serviceTypesDAO.getByRequestFromNotASDC(request) : serviceTypesDAO.getByRequestFromASDC(request);

        if (serviceTypeObject == null) {
            // Generate a new type id
            String typeId = UUID.randomUUID().toString();
            serviceTypeObject = createDCAEServiceTypeDBO(typeId, request);
            serviceTypesDAO.insert(serviceTypeObject);
            return Response.ok().entity(createDCAEServiceType(serviceTypeObject, uriInfo)).build();
        }

        // Service type with same composite key already exists so try to update

        String typeId = serviceTypeObject.getTypeId();
        DCAEServicesDAO servicesDAO = InventoryDAOManager.getInstance().getDCAEServicesDAO();
        Integer count = servicesDAO.countByType(DCAEServiceObject.DCAEServiceStatus.RUNNING, typeId);

        LOG.info(String.format("Checked num DCAE services running: %s, %d", typeId, count));

        // Allow the updating of an existing DCAE service type IFF there are no running DCAE services for this type

        if (count > 0) {
            String message = String.format("DCAE services of type %s are still running: %d", typeId, count);
            ApiResponseMessage entity = new ApiResponseMessage(ApiResponseMessage.ERROR, message);
            return Response.status(Response.Status.CONFLICT).entity(entity).build();
        } else {
            serviceTypeObject = createDCAEServiceTypeDBO(typeId, request);
            serviceTypesDAO.update(serviceTypeObject);
            return Response.ok().entity(serviceTypeObject).build();
        }
    }

    @Override
    public Response dcaeServiceTypesTypeIdDelete(String typeId, UriInfo uriInfo, SecurityContext securityContext)
            throws NotFoundException {
        DCAEServiceTypesDAO serviceTypesDAO = InventoryDAOManager.getInstance().getDCAEServiceTypesDAO();

        if (serviceTypesDAO.getByTypeId(typeId) == null) {
            throw new NotFoundException(1, String.format("DCAE service type not found: %s", typeId));
        } else if (serviceTypesDAO.getByTypeIdActiveOnly(typeId) == null) {
            return Response.status(Response.Status.GONE).build();
        }

        serviceTypesDAO.deactivateExisting(typeId);
        return Response.ok().build();
    }

}
