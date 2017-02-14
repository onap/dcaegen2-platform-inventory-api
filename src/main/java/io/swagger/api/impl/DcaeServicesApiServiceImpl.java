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

import org.openecomp.dcae.inventory.clients.DCAEControllerClient;
import org.openecomp.dcae.inventory.clients.DatabusControllerClient;
import org.openecomp.dcae.inventory.daos.DCAEServiceComponentsDAO;
import org.openecomp.dcae.inventory.daos.DCAEServiceTransactionDAO;
import org.openecomp.dcae.inventory.daos.DCAEServicesDAO;
import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import org.openecomp.dcae.inventory.dbthings.mappers.DCAEServiceObjectMapper;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceComponentObject;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.openecomp.dcae.inventory.exceptions.DCAEControllerClientException;
import org.openecomp.dcae.inventory.exceptions.DatabusControllerClientException;
import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.api.NotFoundException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Link;
import java.util.*;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServicesApiServiceImpl extends DcaeServicesApiService {

    private final static Logger LOG = LoggerFactory.getLogger(DcaeServicesApiServiceImpl.class);
    private static int PAGINATION_PAGE_SIZE = 25;
    private static String COMPONENT_SOURCE_DCAE_CONTROLLER = "DCAECONTROLLER";
    private static String COMPONENT_SOURCE_DATA_BUS_CONTROLLER = "DMAAPCONTROLLER";

    private final DCAEControllerClient dcaeControllerClient;
    private final DatabusControllerClient databusControllerClient;

    private DCAEService createDCAEService(DCAEServiceObject serviceObject, Collection<DCAEServiceComponentObject> componentObjects,
                                          UriInfo uriInfo) {
        // TODO: Complete links: come back and do links for vnf
        DCAEService service = new DCAEService();
        service.setServiceId(serviceObject.getServiceId());
        service.setSelfLink(DcaeServicesApi.buildLinkForGet(uriInfo, "self", serviceObject.getServiceId()));
        service.setTypeLink(DcaeServiceTypesApi.buildLinkForGet(uriInfo, "type", serviceObject.getTypeId()));
        service.setCreated(serviceObject.getCreated().toDate());
        service.setModified(serviceObject.getModified().toDate());
        service.setVnfId(serviceObject.getVnfId());
        //service.setVnfLink(vnfLink);
        service.setVnfType(serviceObject.getVnfType());
        service.setVnfLocation(serviceObject.getVnfLocation());
        service.setDeploymentRef(serviceObject.getDeploymentRef());

        List<DCAEServiceComponent> serviceComponents = new ArrayList<DCAEServiceComponent>();

        for (DCAEServiceComponentObject sco : componentObjects) {
            DCAEServiceComponent component = new DCAEServiceComponent();
            component.setComponentId(sco.getComponentId());
            component.setComponentType(sco.getComponentType());
            component.setComponentSource(sco.getComponentSource());
            component.setShareable(sco.getShareable());
            component.setCreated(sco.getCreated().toDate());
            component.setModified(sco.getModified().toDate());

            // TODO: When putting together the components fail. Should this be a 500 case?
            // For now, this is just logged as a warning.

            if (COMPONENT_SOURCE_DCAE_CONTROLLER.equals(sco.getComponentSource().toUpperCase(Locale.ENGLISH))) {
                if (this.dcaeControllerClient != null) {
                    try {
                        DCAEControllerClient.ServiceInstance serviceInstance
                                = this.dcaeControllerClient.getServiceInstance(component.getComponentId());
                        component.setStatus(serviceInstance.getStatus());
                        // There's no specific location rather its inferred from the AIC tenant
                        component.setLocation(this.dcaeControllerClient.getLocation(serviceInstance));
                        Link componentLink = Link.fromUri(this.dcaeControllerClient.constructResourceURI(sco.getComponentId()))
                                .rel("component").title(component.getComponentId()).build();
                        component.setComponentLink(componentLink);
                    } catch (DCAEControllerClientException e) {
                        LOG.warn(String.format("%s, %s", e.getMessage(), sco.toString()));
                    }
                }
            } else if (COMPONENT_SOURCE_DATA_BUS_CONTROLLER.equals(sco.getComponentSource().toUpperCase(Locale.ENGLISH))) {
                if (this.databusControllerClient != null) {
                    try {
                        if (this.databusControllerClient.isExists(sco.getComponentId())) {
                            Link componentLink = Link.fromUri(this.databusControllerClient.constructResourceURI(sco.getComponentId()))
                                    .rel("component").title(component.getComponentId()).build();
                            component.setComponentLink(componentLink);
                        } else {
                            LOG.warn(String.format("Feed/topic does not exist: %s", sco.getComponentId()));
                        }
                    } catch (DatabusControllerClientException e) {
                        LOG.warn(String.format("%s, %s", e.getMessage(), sco.toString()));
                    }
                }
            } else {
                LOG.warn(String.format("Handling unknown component source: %s", sco.getComponentSource()));
            }

            serviceComponents.add(component);
        }

        service.components(serviceComponents);

        return service;
    }

    @Override
    public Response dcaeServicesGet(String typeId, String vnfId, String vnfType, String vnfLocation,
                                    String componentType, Boolean shareable, DateTime created, Integer offset,
                                    UriInfo uriInfo, SecurityContext securityContext) {
        List<DCAEServiceObject> serviceObjects = new ArrayList<>();
        DateTime createdCutoff = (created == null ? DateTime.now(DateTimeZone.UTC) : created);

        LOG.info(String.format("Create time upper bound cutoff: %s", createdCutoff.toString()));

        // Offset is zero-based index
        offset = (offset == null) ? 0 : offset;
        LOG.info(String.format("Query offset: %d", offset));

        try (Handle jdbiHandle = InventoryDAOManager.getInstance().getHandle()) {
            // WATCH! There is the use of "distinct" here.
            StringBuilder sb = new StringBuilder("select distinct ds.* from dcae_services ds");
            sb.append(" join dcae_services_components_maps m on ds.service_id = m.service_id ");
            sb.append(" join dcae_service_components dsc on m.component_id = dsc.component_id");

            List<String> whereClauses = new ArrayList<String>();

            if (typeId != null) {
                whereClauses.add("ds.type_id = :typeId");
            }

            if (vnfId != null) {
                whereClauses.add("ds.vnf_id = :vnfId");
            }

            if (vnfType != null) {
                whereClauses.add("lower(ds.vnf_type) = lower(:vnfType)");
            }

            if (vnfLocation != null) {
                whereClauses.add("ds.vnf_location = :vnfLocation");
            }

            if (componentType != null) {
                whereClauses.add("dsc.component_type = :componentType");
            }

            if (shareable != null) {
                whereClauses.add("dsc.shareable = :shareable");
            }

            whereClauses.add("ds.created < :createdCutoff");
            whereClauses.add("ds.status = :serviceStatus");

            if (!whereClauses.isEmpty()) {
                sb.append(" where ");
                sb.append(String.join(" and ", whereClauses));
            }

            // Sort by created timestamp - always descending.
            sb.append(" order by created desc");

            Query<DCAEServiceObject> query = jdbiHandle.createQuery(sb.toString()).map(new DCAEServiceObjectMapper());

            if (typeId != null) {
                query.bind("typeId", typeId);
            }

            if (vnfId != null) {
                query.bind("vnfId", vnfId);
            }

            if (vnfType != null) {
                query.bind("vnfType", vnfType);
            }

            if (vnfLocation != null) {
                query.bind("vnfLocation", vnfLocation);
            }

            if (componentType != null) {
                query.bind("componentType", componentType);
            }

            if (shareable != null) {
                // NOTE: That the shareable field in the database is actually an integer.
                query.bind("shareable", (shareable ? 1 : 0));
            }

            query.bind("createdCutoff", createdCutoff);
            // NOTE: This is hardcoded because service status is only used internally.
            query.bind("serviceStatus", DCAEServiceObject.DCAEServiceStatus.RUNNING);

            serviceObjects = query.list();
        }

        // NOTE: Chose to do the pagination via in code here rather than in SQL using LIMIT and OFFSET constraints
        // because of the need for the global total number of result items. SQL approach would require two queries.
        // Going forward, I think the better approach is using SQL because the resultsets may become very large.
        // For now I think this approach is OK and actually we do less SQL querying.
        Integer endpoint = Math.min(offset + PAGINATION_PAGE_SIZE, serviceObjects.size());
        List<DCAEServiceObject> serviceObjectsSliced = serviceObjects.subList(offset, endpoint);

        DCAEServiceComponentsDAO componentsDAO = InventoryDAOManager.getInstance().getDCAEServiceComponentsDAO();
        List<DCAEService> services = new ArrayList<DCAEService>();

        for (DCAEServiceObject so : serviceObjectsSliced) {
            List<DCAEServiceComponentObject> components = componentsDAO.getByServiceId(so.getServiceId());
            services.add(createDCAEService(so, components, uriInfo));
        }

        InlineResponse2001 response = new InlineResponse2001();
        response.setItems(services);
        response.setTotalCount(serviceObjects.size());
        // TODO: Show the total count of items in this response i.e. local count?

        InlineResponse200Links navigationLinks = new InlineResponse200Links();
        Integer offsetPrev = offset - PAGINATION_PAGE_SIZE;

        if (offsetPrev >= 0) {
            navigationLinks.setPreviousLink(DcaeServicesApi.buildLinkForGet(uriInfo, "prev", typeId, vnfId, vnfType,
                    vnfLocation, componentType, shareable, created, offsetPrev));
        }

        Integer offsetNext = offset + PAGINATION_PAGE_SIZE;

        if (offsetNext < serviceObjects.size()) {
            navigationLinks.setNextLink(DcaeServicesApi.buildLinkForGet(uriInfo, "next", typeId, vnfId, vnfType,
                    vnfLocation, componentType, shareable, created, offsetNext));
        }

        response.setLinks(navigationLinks);

        return Response.ok().entity(response).build();
    }

    @Override
    public Response dcaeServicesServiceIdGet(String serviceId, UriInfo uriInfo, SecurityContext securityContext)
            throws NotFoundException {
        DCAEServicesDAO servicesDAO = InventoryDAOManager.getInstance().getDCAEServicesDAO();
        DCAEServiceComponentsDAO componentsDAO = InventoryDAOManager.getInstance().getDCAEServiceComponentsDAO();

        DCAEServiceObject serviceObject = servicesDAO.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId);

        if (serviceObject == null) {
            throw new NotFoundException(1, String.format("DCAEService not found: %s", serviceId));
        }

        List<DCAEServiceComponentObject> componentObjects = componentsDAO.getByServiceId(serviceId);
        DCAEService service = createDCAEService(serviceObject, componentObjects, uriInfo);

        return Response.ok().entity(service).build();
    }

    @Override
    public Response dcaeServicesServiceIdPut(String serviceId, DCAEServiceRequest request, UriInfo uriInfo,
                                             SecurityContext securityContext) {
        // Check to make sure that the DCAE service type exists
        if (InventoryDAOManager.getInstance().getDCAEServiceTypesDAO().getByTypeIdActiveOnly(request.getTypeId()) == null) {
            String errorMessage = String.format("DCAE service type does not exist: %s", request.getTypeId());
            ApiResponseMessage message = new ApiResponseMessage(ApiResponseMessage.ERROR, errorMessage);
            return Response.status(422).entity(message).build();
        }

        // TODO: Check DCAE service components against source services i.e. DCAE controller and data bus controller
        // Possibly refuse to process if that check fails.

        DCAEServicesDAO servicesDAO = InventoryDAOManager.getInstance().getDCAEServicesDAO();
        DCAEServiceComponentsDAO componentsDAO = InventoryDAOManager.getInstance().getDCAEServiceComponentsDAO();

        // NOTE: 1607 is using Postgres v9.3 which does NOT have the upgrade to the INSERT operation that allows for UPSERTs
        // Challenge here is make this entire PUT operation atomic.
        // TODO: 1607 we are actually using v9.5 which has the UPSERT. Migrate this code to use the UPSERT.

        // Watch! We have to query for services regardless of status because we need to account for "removed" instances
        // that get resurrected.
        final DCAEServiceObject serviceObjectFromStore = servicesDAO.getByServiceId(serviceId);
        final Map<String, DCAEServiceComponentObject> componentObjectsFromStore = new HashMap<String, DCAEServiceComponentObject>();

        for (DCAEServiceComponentObject componentObject : componentsDAO.getByServiceId(serviceId)) {
            componentObjectsFromStore.put(componentObject.getComponentId(), componentObject);
        }

        DateTime modified = DateTime.now(DateTimeZone.UTC);

        DCAEServiceTransactionDAO.DCAEServiceTransactionContext transactionContext
                = new DCAEServiceTransactionDAO.DCAEServiceTransactionContext(serviceId, modified);

        // 1) Insert/update for DCAEServiceObject

        DCAEServiceObject serviceObjectToSendBack = serviceObjectFromStore;

        if (serviceObjectFromStore == null) {
            serviceObjectToSendBack = new DCAEServiceObject(serviceId, request);
            serviceObjectToSendBack.setModified(modified);
            transactionContext.setServiceObjectToInsert(serviceObjectToSendBack);
        } else {
            LOG.info(String.format("DCAEServiceObject already exists - updating: %s, %s",
                    serviceObjectFromStore.getCreated().toString(),
                    serviceObjectFromStore.getModified().toString()));

            serviceObjectToSendBack = new DCAEServiceObject(serviceObjectFromStore, request);
            serviceObjectToSendBack.setModified(modified);
            transactionContext.setServiceObjectToUpdate(serviceObjectToSendBack);
        }

        // 2) Insert/update DCAEServiceComponentObjects. Components exist independent of the associated DCAE service.

        Map<String, DCAEServiceComponentObject> componentObjectsToSendBack = new HashMap<String, DCAEServiceComponentObject>();

        for (DCAEServiceComponentRequest requestComponent : request.getComponents()) {
            // Have to query the database rather than checking the result of getting by service id because of the
            // independence of components and services. A component may already exist even though from a service
            // perspective it is seen as "new".
            final DCAEServiceComponentObject coExisting = componentsDAO.getByComponentId(requestComponent.getComponentId());
            DCAEServiceComponentObject coToSendBack = null;

            if (coExisting == null) {
                // Add new component
                coToSendBack = new DCAEServiceComponentObject(requestComponent);
                coToSendBack.setModified(modified);
                transactionContext.addComponentObjectToInsert(coToSendBack);
            } else {
                // TODO: Check if the mutable fields have changed before doing the update.
                // Update existing component
                coToSendBack = new DCAEServiceComponentObject(coExisting, requestComponent);
                coToSendBack.setModified(modified);
                transactionContext.addComponentObjectToUpdate(coToSendBack);
            }

            if (coToSendBack != null) {
                componentObjectsToSendBack.put(coToSendBack.getComponentId(), coToSendBack);
            }
        }

        // 3) Update relationships: add ones that don't exist, delete ones that do exist but no longer should not

        // Add relationships that didn't exist before
        for (String componentId : componentObjectsToSendBack.keySet()) {
            if (!componentObjectsFromStore.containsKey(componentId)) {
                transactionContext.addMappingsToInsert(componentId);
            }
        }

        // Remove relationships that have been removed
        for (String componentId : componentObjectsFromStore.keySet()) {
            if (!componentObjectsToSendBack.containsKey(componentId)) {
                transactionContext.addMappingsToDelete(componentId);
            }
        }

        DCAEServiceTransactionDAO transactionDAO = InventoryDAOManager.getInstance().getDCAEServiceTransactionDAO();
        transactionDAO.insert(transactionContext);

        DCAEService service = createDCAEService(serviceObjectToSendBack, componentObjectsToSendBack.values(), uriInfo);

        return Response.ok().entity(service).build();
    }

    public Response dcaeServicesServiceIdDelete(String serviceId, SecurityContext securityContext) throws NotFoundException {
        DCAEServicesDAO servicesDAO = InventoryDAOManager.getInstance().getDCAEServicesDAO();

        if (servicesDAO.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId) == null) {
            throw new NotFoundException(ApiResponseMessage.ERROR, String.format("DCAE service not found: %s", serviceId));
        }

        servicesDAO.updateStatusByServiceId(DateTime.now(DateTimeZone.UTC), DCAEServiceObject.DCAEServiceStatus.REMOVED,
                serviceId);

        return Response.ok().build();
    }

    public DcaeServicesApiServiceImpl(DCAEControllerClient dcaeControllerClient, DatabusControllerClient databusControllerClient) {
        this.dcaeControllerClient = dcaeControllerClient;
        this.databusControllerClient = databusControllerClient;
    }

}
