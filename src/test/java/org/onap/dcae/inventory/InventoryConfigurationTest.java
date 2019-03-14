/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2018-2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.dcae.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import javax.validation.Validator;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;

/**
 * Created by mhwang on 3/22/18.
 */
public class InventoryConfigurationTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<InventoryConfiguration> factory
            = new YamlConfigurationFactory<>(InventoryConfiguration.class, validator, objectMapper, "dw");

    @Test
    public void testInventoryConfigurationLoad() {
        try {
            final File yaml = new File(Thread.currentThread().getContextClassLoader().
				       getResource("config-inventory.yaml").getPath());
            InventoryConfiguration configuration = factory.build(yaml);

            assertEquals(configuration.getDatabusControllerConnection().getHost(), "databus-controller-hostname");
            assertEquals((long) configuration.getDatabusControllerConnection().getPort(), 8443);
            assertEquals(configuration.getDatabusControllerConnection().getRequired(), true);
            assertEquals(configuration.getDatabusControllerConnection().getMechId(), "some-mech-id");
            assertEquals(configuration.getDatabusControllerConnection().getPassword(), "some-password");

            configuration.setDefaultName("foo-config");
            assertEquals(configuration.getDefaultName(), "foo-config");
            assertNotNull(configuration.getJerseyClientConfiguration());

            assertEquals(configuration.getDataSourceFactory().getUrl(), "jdbc:postgresql://127.0.0.1:5432/dcae_inv");
        } catch (Exception e) {
            fail("Failed to load config-inventory");
        }
    }

}
