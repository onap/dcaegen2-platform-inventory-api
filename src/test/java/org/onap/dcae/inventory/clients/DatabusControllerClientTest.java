/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.dcae.inventory.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.validation.Validator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.inventory.InventoryConfiguration;
import org.onap.dcae.inventory.exceptions.DatabusControllerClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;

/**
 * Created by mhwang on 3/22/18.
 */
public class DatabusControllerClientTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<InventoryConfiguration.DatabusControllerConnectionConfiguration> factory
            = new YamlConfigurationFactory<>(InventoryConfiguration.DatabusControllerConnectionConfiguration.class, 
					     validator, objectMapper, "dw");

    private InventoryConfiguration.DatabusControllerConnectionConfiguration configuration = null;

    @Before
    public void setupClass() throws IOException, ConfigurationException {
        final File yaml = new File(Thread.currentThread().getContextClassLoader().getResource("config-databus.yaml").getPath());
        this.configuration = factory.build(yaml);
    }

    @Test
    public void testConstructResourceURI() {
        DatabusControllerClient client = new DatabusControllerClient(null, this.configuration);
        URI uri = client.constructResourceURI("/some-path");
        assertEquals(uri.toString(), "https://databus-controller-hostname:8443/some-path");
    }

    private DatabusControllerClient setupForIsExists(Response mockResponse) {
        Client mockClient = mock(Client.class);
        WebTarget mockWebTarget = mock(WebTarget.class);
        Invocation.Builder mockBuilder = mock(Invocation.Builder.class);

        try {
            URI uri = new URI("https://databus-controller-hostname:8443/some-component-id");
            when(mockClient.target(uri)).thenReturn(mockWebTarget);
            //when(mockClient.target(new URI(any()))).thenReturn(mockWebTarget);
        } catch (URISyntaxException e) {
            fail("URI syntax error");
        }

        when(mockWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(mockBuilder);
        when(mockBuilder.header(any(), any())).thenReturn(mockBuilder);

        when(mockBuilder.get()).thenReturn(mockResponse);
        return new DatabusControllerClient(mockClient, this.configuration);

    }

    @Test
    public void testIsExists() {
        Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(200);
        InputStream stream = new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8));
        when(mockResponse.getEntity()).thenReturn(stream);

        DatabusControllerClient client = setupForIsExists(mockResponse);

        try {
            assertEquals(client.isExists("some-component-id"), false);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    private void testIsExistsErrors(int statusError) {
        Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(statusError);

        DatabusControllerClient client = setupForIsExists(mockResponse);

        try {
            client.isExists("some-component-id");
            fail("This was supposed to be a fail case. Exception not thrown.");
        } catch (DatabusControllerClientException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testIsExists401() {
        testIsExistsErrors(401);
    }

    @Test
    public void testIsExists403() {
        testIsExistsErrors(403);
    }

    @Test
    public void testIsExists500() {
        testIsExistsErrors(500);
    }

}
