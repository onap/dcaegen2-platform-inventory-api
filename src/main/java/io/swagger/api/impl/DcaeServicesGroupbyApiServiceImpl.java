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

import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import io.swagger.api.*;
import io.swagger.model.DCAEServiceGroupByResults;
import io.swagger.model.DCAEServiceGroupByResultsPropertyValues;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServicesGroupbyApiServiceImpl extends DcaeServicesGroupbyApiService {

    @Override
    public Response dcaeServicesGroupbyPropertyNameGet(String propertyName, UriInfo uriInfo, SecurityContext securityContext) {
        String columnName = "";

        switch (propertyName) {
            case "type":
                columnName = "type_id";
                break;
            case "vnfLocation":
                columnName = "vnf_location";
                break;
            case "vnfType":
                columnName = "vnf_type";
                break;
            default:
                return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<Map<String, Object>> results = new ArrayList<>();

        try (Handle jdbiHandle = InventoryDAOManager.getInstance().getHandle()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("select %s, count(1) as num ", columnName));
            sb.append(" from dcae_services where status = :serviceStatus");
            sb.append(String.format(" group by %s order by count(1) desc", columnName));
            String queryString = sb.toString();

            // NOTE: This is hardcoded because service status is only used internally.
            results = jdbiHandle.createQuery(queryString).bind("serviceStatus", DCAEServiceObject.DCAEServiceStatus.RUNNING)
                    .list();
        }

        DCAEServiceGroupByResults response = new DCAEServiceGroupByResults();
        response.setPropertyName(propertyName);

        for (Map<String, Object> result : results) {
            DCAEServiceGroupByResultsPropertyValues value = new DCAEServiceGroupByResultsPropertyValues();
            value.setCount(((Long) result.get("num")).intValue());
            String propertyValue = (String) result.get(columnName);
            value.setPropertyValue(propertyValue);

            switch (propertyName) {
                case "type":
                    value.setDcaeServiceQueryLink(DcaeServicesApi.buildLinkForGetByTypeId(uriInfo, "dcae_services",
                            propertyValue));
                    break;
                case "vnfLocation":
                    value.setDcaeServiceQueryLink(DcaeServicesApi.buildLinkForGetByVnfLocation(uriInfo, "dcae_services",
                            propertyValue));
                    break;
                case "vnfType":
                    value.setDcaeServiceQueryLink(DcaeServicesApi.buildLinkForGetByVnfType(uriInfo, "dcae_services",
                            propertyValue));
                    break;
                default:
                    return Response.status(Response.Status.BAD_REQUEST).build();
            }

            response.getPropertyValues().add(value);
        }

        return Response.ok().entity(response).build();
    }
}
