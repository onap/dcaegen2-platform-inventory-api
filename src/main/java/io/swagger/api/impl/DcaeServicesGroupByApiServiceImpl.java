/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2019 AT&T and Nokia Intellectual Property. All rights reserved.
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

import io.swagger.api.DcaeServicesApi;
import io.swagger.api.DcaeServicesGroupbyApiService;
import org.onap.dcae.inventory.daos.InventoryDataAccessManager;
import org.onap.dcae.inventory.dbthings.models.DCAEServiceObject;
import io.swagger.model.DCAEServiceGroupByResults;
import io.swagger.model.DCAEServiceGroupByResultsPropertyValues;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServicesGroupByApiServiceImpl extends DcaeServicesGroupbyApiService {

    public static final String PROPERTY_NAME_TYPE = "type";
    public static final String PROPERTY_NAME_VNF_LOCATION = "vnfLocation";
    public static final String PROPERTY_NAME_VNF_TYPE = "vnfType";
    private static final String UNKNOWN_PROPERTY_NAME = "n/a";

    private final RunningDcaeServicesProvider runningDcaeServicesProvider;
    private final DcaeServicesLinkResolver dcaeServicesLinkResolver;

    public DcaeServicesGroupByApiServiceImpl(
            RunningDcaeServicesProvider runningDcaeServicesProvider,
            DcaeServicesLinkResolver dcaeServicesLinkResolver) {
        this.runningDcaeServicesProvider = runningDcaeServicesProvider;
        this.dcaeServicesLinkResolver = dcaeServicesLinkResolver;
    }

    @Override
    public Response dcaeServicesGroupbyPropertyNameGet(
            String propertyName, UriInfo uriInfo, SecurityContext securityContext) {

        String columnName = resolveColumnName(propertyName);
        if(columnName.equals(UNKNOWN_PROPERTY_NAME)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<Map<String, Object>> runningDcaeServices = runningDcaeServicesProvider.fetch(columnName);
        DCAEServiceGroupByResults response = generateResponse(propertyName, uriInfo, columnName, runningDcaeServices);

        return Response.ok().entity(response).build();
    }

    DCAEServiceGroupByResults generateResponse(
            String propertyName, UriInfo uriInfo, String columnName, List<Map<String, Object>> runningDcaeServices) {

        DCAEServiceGroupByResults response = new DCAEServiceGroupByResults();
        response.setPropertyName(propertyName);

        for (Map<String, Object> result : runningDcaeServices) {
            DCAEServiceGroupByResultsPropertyValues value = new DCAEServiceGroupByResultsPropertyValues();
            value.setCount(((Long) result.get("num")).intValue());

            String propertyValue = (String) result.get(columnName);
            value.setPropertyValue(propertyValue);

            Link dcaeServicesLink = dcaeServicesLinkResolver.resolveLink(propertyName, uriInfo, propertyValue);
            value.setDcaeServiceQueryLink(dcaeServicesLink);

            response.getPropertyValues().add(value);
        }
        return response;
    }

    String resolveColumnName(String propertyName) {
        String columnName;

        switch (propertyName) {
            case PROPERTY_NAME_TYPE:
                columnName = "type_id";
                break;
            case PROPERTY_NAME_VNF_LOCATION:
                columnName = "vnf_location";
                break;
            case PROPERTY_NAME_VNF_TYPE:
                columnName = "vnf_type";
                break;
            default:
                columnName = UNKNOWN_PROPERTY_NAME;
        }
        return columnName;
    }

    public static class DcaeServicesLinkResolver {

        private static final String RELATION = "dcae_services";

        Link resolveLink(String propertyName, UriInfo uriInfo, String propertyValue) {
            Link dcaeServices = null;
            switch (propertyName) {
                case PROPERTY_NAME_TYPE:
                    dcaeServices = DcaeServicesApi.buildLinkForGetByTypeId(uriInfo, RELATION,
                            propertyValue);
                    break;
                case PROPERTY_NAME_VNF_LOCATION:
                    dcaeServices = DcaeServicesApi.buildLinkForGetByVnfLocation(uriInfo, RELATION,
                            propertyValue);
                    break;
                case PROPERTY_NAME_VNF_TYPE:
                    dcaeServices = DcaeServicesApi.buildLinkForGetByVnfType(uriInfo, RELATION,
                            propertyValue);
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("Unsupported '%s' property name!", propertyName));
            }
            return dcaeServices;
        }
    }


    public static class RunningDcaeServicesProvider {

        private static final String SERVICE_STATUS_PLACE_HOLDER = "serviceStatus";

        public RunningDcaeServicesProvider(InventoryDataAccessManager inventoryDataAccessManager) {
            this.inventoryDataAccessManager = inventoryDataAccessManager;
        }

        private final InventoryDataAccessManager inventoryDataAccessManager;

        public List<Map<String, Object>> fetch(String columnName) {

            String queryString = createQuery(columnName);
            try (Handle jdbiHandle = inventoryDataAccessManager.getHandle()) {
                return executeQuery(queryString, jdbiHandle);
            }
        }

        List<Map<String, Object>> executeQuery(String queryString, Handle jdbiHandle) {
            // NOTE: This is hardcoded because service status is only used internally.
            final Query<Map<String, Object>> query = jdbiHandle.createQuery(queryString);
            final Query<Map<String, Object>> bind = query
                    .bind(SERVICE_STATUS_PLACE_HOLDER, DCAEServiceObject.DCAEServiceStatus.RUNNING);
            return bind.list();
        }

        static String createQuery(String columnName) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("select %s, count(1) as num ", columnName));
            sb.append(String.format(" from dcae_services where status = :%s",SERVICE_STATUS_PLACE_HOLDER));
            sb.append(String.format(" group by %s order by count(1) desc", columnName));
            return sb.toString();
        }
    }
}
