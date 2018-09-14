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

package io.swagger.api.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.dcae.inventory.clients.DatabusControllerClient;
import org.onap.dcae.inventory.daos.DCAEServiceComponentsDAO;
import org.onap.dcae.inventory.daos.DCAEServicesComponentsMapsDAO;
import org.onap.dcae.inventory.daos.DCAEServicesDAO;
import org.onap.dcae.inventory.daos.InventoryDAOManager;
import org.onap.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import io.swagger.api.Util;
import io.swagger.model.DCAEServiceRequest;

/**
 * Created by mhwang on 9/25/17.
 *
 * This test covers the gap in DcaeServicesApiServiceImplTests - testing the GET query interface
 * The approach taken here is standing up an H2 in-memory database which is different from the
 * original test which made an attempt to mock the database calls but ran into an impasse. The
 * test here was not included in the original class because of conflicting setup operations.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class DcaeServicesApiServiceImplH2Tests {

    private static final Logger log = LoggerFactory.getLogger(DcaeServicesApiServiceImplH2Tests.class);

    private DCAEServicesDAO mockServicesDao = null;
    private DCAEServiceComponentsDAO mockComponentsDao = null;
    private DCAEServicesComponentsMapsDAO mockComponentsMapsDao = null;

    // https://stackoverflow.com/questions/35825383/how-to-test-jdbi-daos-with-h2-in-memory-database
    // Caused by: java.lang.ClassNotFoundException: Unable to load class: org.h2.Driver from
    // ClassLoader:sun.misc.Launcher$AppClassLoader@18b4aac2;ClassLoader:sun.misc.Launcher$AppClassLoader@18b4aac2
    protected DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass( "org.h2.Driver" );
        dataSourceFactory.setUrl( "jdbc:h2:mem:testDb" );
        dataSourceFactory.setUser( "sa" );
        dataSourceFactory.setPassword( "sa" );

        return dataSourceFactory;
    }

    @Before
    public void setUp() {
        Environment env = new Environment( "test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null );
        DBI dbi = new DBIFactory().build( env, getDataSourceFactory(), "test" );
        mockServicesDao = dbi.onDemand(DCAEServicesDAO.class);
        mockComponentsDao = dbi.onDemand(DCAEServiceComponentsDAO.class);
        mockComponentsMapsDao = dbi.onDemand(DCAEServicesComponentsMapsDAO.class);

        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDaoManager = mock(InventoryDAOManager.class);

        when(InventoryDAOManager.getInstance()).thenReturn(mockDaoManager);
        when(mockDaoManager.getHandle()).thenReturn(dbi.open());
    }

    @Test
    public void testDcaeServicesGet() {
        mockServicesDao.createTable();
        mockComponentsDao.createTable();
        mockComponentsMapsDao.createTable();

        DCAEServiceRequest request = new DCAEServiceRequest();
        request.setTypeId("some-type-id");
        request.setVnfId("some-vnf-id");
        request.setVnfType("some-vnf-type");
        request.setVnfLocation("some-vnf-location");
        request.setDeploymentRef("some-deployment-ref");
        DCAEServiceObject so = new DCAEServiceObject("some-service-id", request);
        mockServicesDao.insert(so);

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);
        String typeId = "some-type-id";
        String vnfId = "some-vnf-id";
        String vnfType = "some-vnf-type";
        String vnfLocation = "some-vnf-location";
        String componentType = "some-component-type";
        Boolean shareable = Boolean.TRUE;
        DateTime created = null;
        Integer offset = 0;
        UriInfo uriInfo = new Util.FakeUriInfo();
        SecurityContext securityContext = null;
        Response response = api.dcaeServicesGet(typeId, vnfId, vnfType, vnfLocation, componentType, shareable, created,
                offset, uriInfo, securityContext);
        assertEquals(response.getStatus(), 200);
    }

}
