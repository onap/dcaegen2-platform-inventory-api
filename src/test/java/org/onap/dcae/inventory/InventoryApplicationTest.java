/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Filter;
import org.onap.dcae.inventory.daos.InventoryDAOManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.FilterRegistration;
import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mhwang on 3/14/19.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class InventoryApplicationTest {

    @Test
    public void testProcessArgs() {
        String userArgs[] = {"server"};
        assertEquals(InventoryApplication.processArgs(userArgs).length, 2);

        userArgs = new String[] {"server", "some-junit-yaml.yaml"};
        assertArrayEquals(InventoryApplication.processArgs(userArgs), userArgs);
    }

    @Test
    public void testAppInit() {
        InventoryApplication app = new InventoryApplication();
        assertEquals(app.getName(), "dcae-inventory");

        Bootstrap<InventoryConfiguration> mockBootstrap = mock(Bootstrap.class);
        app.initialize(mockBootstrap);

        InventoryApplication.shouldRemoteFetchConfig = !InventoryApplication.shouldRemoteFetchConfig;
        app.initialize(mockBootstrap);
    }

    @Test
    public void testAppRun() {
        InventoryApplication app = new InventoryApplication();

        ServletEnvironment mockEnvServlet = mock(ServletEnvironment.class);
        FilterRegistration.Dynamic mockFilters = mock(FilterRegistration.Dynamic.class);
        when(mockEnvServlet.addFilter("CORSFilter", CrossOriginFilter.class)).thenReturn(mockFilters);
        Environment mockEnv = mock(Environment.class);
        when(mockEnv.servlets()).thenReturn(mockEnvServlet);
        when(mockEnv.getObjectMapper()).thenReturn(new ObjectMapper());
        JerseyEnvironment mockEnvJersey = mock(JerseyEnvironment.class);
        when(mockEnv.jersey()).thenReturn(mockEnvJersey);

        DataSourceFactory mockDSF = mock(DataSourceFactory.class);
        when(mockDSF.getProperties()).thenReturn(new HashMap<String, String>());
        InventoryConfiguration.DatabusControllerConnectionConfiguration mockConfigBus = mock(InventoryConfiguration.DatabusControllerConnectionConfiguration.class);
        InventoryConfiguration mockConfig = mock(InventoryConfiguration.class);
        when(mockConfig.getDataSourceFactory()).thenReturn(mockDSF);
        when(mockConfig.getDatabusControllerConnection()).thenReturn(mockConfigBus);
        when(mockConfigBus.getRequired()).thenReturn(Boolean.FALSE);

        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDaoManager = mock(InventoryDAOManager.class);

        when(InventoryDAOManager.getInstance()).thenReturn(mockDaoManager);
        doAnswer((a) -> {return null;}).when(mockDaoManager).initialize();

        app.run(mockConfig, mockEnv);
    }

}
