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
import org.openecomp.dcae.inventory.exceptions.DCAEControllerClientException;
import org.openecomp.dcae.inventory.exceptions.DCAEControllerConnectionException;
import org.openecomp.dcae.inventory.exceptions.DCAEControllerTimeoutException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.*;

/**
 * Created by mhwang on 5/12/16.
 */
public class DCAEControllerClient {

    /**
     * Used for JSON objects of the form:
     * "hostService": {"$ref": "/services/vm-docker-host-2/instances/mtl2"}
     */
    public static class Reference {

        @JsonProperty("$ref")
        private String ref;

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

    }

    /**
     * Used for the JSON objects returned from /services/{service id}/instances/{instance id}
     * calls.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceInstance {

        @JsonProperty("status")
        private String status;

        /**
         * Property points to the Docker host this Docker container runs on.
         * This service instance is an application to be run as a Docker container if this value is not null.
         */
        @JsonProperty("hostService")
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        private Reference hostService;

        /**
         * Property points to the CDAP cluster this CDAP application is deployed on.
         * This service instance is a CDAP application if this value is not null.
         */
        @JsonProperty("clusterService")
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        private Reference clusterService;

        /**
         * Property points to the location resource that this instance is associated with.
         * This property is not null when the service instance is a "pure" VM.
         */
        @JsonProperty("location")
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        private Reference location;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Reference getHostService() {
            return hostService;
        }

        public void setHostService(Reference hostService) {
            this.hostService = hostService;
        }

        public Reference getClusterService() {
            return clusterService;
        }

        public void setClusterService(Reference clusterService) {
            this.clusterService = clusterService;
        }

        public Reference getLocation() {
            return location;
        }

        public void setLocation(Reference location) {
            this.location = location;
        }

    }

    private final static Logger LOG = LoggerFactory.getLogger(DCAEControllerClient.class);

    private final Client client;
    private final InventoryConfiguration.DCAEControllerConnectionConfiguration connectionConfiguration;

    public URI constructResourceURI(String resourcePath) {
        // TODO: Better way to construct this?

        // Make sure that the resource path has a "/" because the UriBuilder sucks and doesn't do it for us.
        if (resourcePath.charAt(0) != '/') {
            resourcePath = (new StringBuilder("/")).append(resourcePath).toString();
        }

        StringBuilder actualPath = new StringBuilder("/");
        actualPath.append(this.connectionConfiguration.getBasePath());
        actualPath.append(resourcePath);

        return UriBuilder.fromPath(actualPath.toString()).scheme("http").host(this.connectionConfiguration.getHost())
                .port(this.connectionConfiguration.getPort()).build();
    }

    public ServiceInstance getServiceInstance(String componentId) throws DCAEControllerClientException {
        URI uri = constructResourceURI(componentId);
        Response response = null;

        try {
            response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .property(HTTP_AUTHENTICATION_BASIC_USERNAME, connectionConfiguration.getUser())
                    .property(HTTP_AUTHENTICATION_BASIC_PASSWORD, connectionConfiguration.getPassword()).get();
        } catch (ProcessingException e) {
            // Apparently the exceptions are wrapped which is not ideal because many different types of errors are embedded
            // in single exception. TODO: May want to come back to split up the errors.
            // Example:
            // javax.ws.rs.ProcessingException: org.apache.http.conn.ConnectTimeoutException: Connect to <dcae controller domain name>:9998
            String message = "Connecting with DCAE controller probably timed out";
            LOG.error(message, e);
            String exceptionMessage  = String.format("%s: %s", message, e.getMessage());
            throw new DCAEControllerTimeoutException(exceptionMessage);
        } catch (Exception e) {
            String message = "Unexpected connection issue with DCAE controller";
            LOG.error(message, e);
            String exceptionMessage  = String.format("%s: %s", message, e.getMessage());
            throw new DCAEControllerConnectionException(exceptionMessage);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Received response from DCAE controller: %d", response.getStatus()));
        }

        if (response.getStatus() == 200) {
            ObjectMapper om = new ObjectMapper();

            try {
                return om.readValue((InputStream) response.getEntity(), ServiceInstance.class);
            } catch (IOException e) {
                throw new DCAEControllerClientException(e);
            }
        }

        throw new DCAEControllerClientException(String.format("Unexpected error from DCAE controller: %d", response.getStatus()));
    }

    public String getLocation(ServiceInstance serviceInstance) {
        if (serviceInstance.getLocation() != null) {
            return serviceInstance.getLocation().getRef();
        } else if (serviceInstance.getClusterService() != null) {
            // Drill down: Location is on the underlying CDAP cluster service instance
            String cdapClusterRef = serviceInstance.getClusterService().getRef();
            return getLocation(getServiceInstance(cdapClusterRef));
        } else if (serviceInstance.getHostService() != null) {
            // Drill down: Location is on the underlying Docker host service instance
            String dockerHostRef = serviceInstance.getHostService().getRef();
            return getLocation(getServiceInstance(dockerHostRef));
        } else {
            throw new DCAEControllerClientException("No valid location for service instance");
        }
    }

    public DCAEControllerClient(Client client,
                                InventoryConfiguration.DCAEControllerConnectionConfiguration connectionConfiguration) {
        this.client = client;
        this.connectionConfiguration = connectionConfiguration;
    }

}
