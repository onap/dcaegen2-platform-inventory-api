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

package org.onap.dcae.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by mhwang on 3/22/18.
 */
public class InventoryConfigurationTest {

    final private ObjectMapper objectMapper = Jackson.newObjectMapper();
    final private Validator validator = Validators.newValidator();
    final private YamlConfigurationFactory<InventoryConfiguration> factory
            = new YamlConfigurationFactory<>(InventoryConfiguration.class, validator, objectMapper, "dw");

    @Test
    public void testInventoryConfigurationLoad() {
        try {
            final File yaml = new File(Thread.currentThread().getContextClassLoader().getResource("config-inventory.yaml").getPath());
            InventoryConfiguration configuration = factory.build(yaml);

            assertEquals(configuration.getDatabusControllerConnection().getHost(), "databus-controller-hostname");
            assertEquals((long) configuration.getDatabusControllerConnection().getPort(), 8443);
            assertEquals(configuration.getDatabusControllerConnection().getRequired(), true);

            assertEquals(configuration.getDataSourceFactory().getUrl(), "jdbc:postgresql://127.0.0.1:5432/dcae_inv");
        } catch(Exception e) {
            fail("Failed to load config-inventory");
        }
    }

}
