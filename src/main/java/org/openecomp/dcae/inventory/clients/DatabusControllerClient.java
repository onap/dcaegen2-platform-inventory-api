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

package org.openecomp.dcae.inventory.clients;

import org.openecomp.dcae.inventory.InventoryConfiguration;
import org.openecomp.dcae.inventory.exceptions.DatabusControllerClientException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Created by mhwang on 5/25/16.
 */
public class DatabusControllerClient {

    private final Client client;
    private final InventoryConfiguration.DatabusControllerConnectionConfiguration connectionConfiguration;

    public URI constructResourceURI(String resourcePath) {
        // Make sure that the resource path has a "/" because the UriBuilder sucks and doesn't do it for us.
        if (resourcePath.charAt(0) != '/') {
            resourcePath = (new StringBuilder("/")).append(resourcePath).toString();
        }

        return UriBuilder.fromPath(resourcePath.toString()).scheme("https").host(this.connectionConfiguration.getHost())
                .port(this.connectionConfiguration.getPort()).build();
    }

    // TODO: Actually model the JSON objects so that they can be returned to be used for providing useful information
    // and thus change this to a "get".
    public boolean isExists(String componentId) throws DatabusControllerClientException {
        URI uri = constructResourceURI(componentId);

        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", "application/json").get();

        if (response.getStatus() == 200) {
            ObjectMapper om = new ObjectMapper();

            try {
                Map<String, Object> entity = om.readValue((InputStream) response.getEntity(),
                        new TypeReference<Map<String, Object>>() {});

                return (entity != null && entity.size() > 0) ? true : false;
            } catch (IOException e) {
                throw new DatabusControllerClientException(e);
            }
        } else if (response.getStatus() == 401) {
            // You probably got this because your mech id/password is not authorized
            throw new DatabusControllerClientException(String.format("Check the mech id/password: %d", response.getStatus()));
        } else if (response.getStatus() == 403) {
            throw new DatabusControllerClientException(String.format("Credentials not authorized: %d", response.getStatus()));
        }

        throw new DatabusControllerClientException(String.format("Unexpected error from databus controller: %d",
                response.getStatus()));
    }

    public DatabusControllerClient(Client client,
                                   InventoryConfiguration.DatabusControllerConnectionConfiguration connectionConfiguration) {
        this.client = client;
        this.connectionConfiguration = connectionConfiguration;
    }

}
