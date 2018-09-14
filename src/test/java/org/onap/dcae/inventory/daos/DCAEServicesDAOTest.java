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

package org.onap.dcae.inventory.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.skife.jdbi.v2.DBI;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import io.swagger.model.DCAEServiceRequest;

/**
 * Created by mhwang on 3/25/18.
 */
public class DCAEServicesDAOTest {

    private DCAEServicesDAO fooDao;

    // Learned about the H2 approach from here:
    // https://stackoverflow.com/questions/35825383/how-to-test-jdbi-daos-with-h2-in-memory-database
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
        fooDao = dbi.onDemand(DCAEServicesDAO.class);
    }

    @Test
    public void testSaveAndGet() {
        try {
            fooDao.createTable();
            DCAEServiceRequest request = new DCAEServiceRequest();
            request.setTypeId("some-type-id");
            request.setVnfId("some-vnf-id");
            request.setVnfType("some-vnf-type");
            request.setVnfLocation("some-vnf-location");
            request.setDeploymentRef("some-deployment-ref");
            DCAEServiceObject so = new DCAEServiceObject("some-service-id", request);
            fooDao.insert(so);

            DCAEServiceObject target = fooDao.getByServiceId("some-service-id");
            assertEquals(target.getServiceId(), so.getServiceId());
        } catch (Exception e) {
            fail("Failure at some point in this compound test.");
        }
    }
}
