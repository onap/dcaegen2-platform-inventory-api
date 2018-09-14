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

package org.onap.dcae.inventory.dbthings.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.joda.time.DateTime;
import org.junit.Test;
import org.onap.dcae.inventory.daos.DCAEServiceComponentsDAO;
import org.onap.dcae.inventory.daos.DCAEServiceTransactionDAO;
import org.onap.dcae.inventory.daos.DCAEServicesComponentsMapsDAO;
import org.onap.dcae.inventory.daos.DCAEServicesDAO;

/**
 * Created by mhwang on 10/2/17.
 */
public class DCAEServiceTransactionDAOTests {

    @Test
    public void testCreateDCAEServiceTransactionContext() {
        String serviceId = "service-foo";
        DateTime modifiedTime = new DateTime();
        DCAEServiceTransactionDAO.DCAEServiceTransactionContext context
                = new DCAEServiceTransactionDAO.DCAEServiceTransactionContext(serviceId, modifiedTime);

        assertEquals(context.getServiceId(), serviceId);
        assertEquals(context.getModified(), modifiedTime);
    }

    @Test
    public void testInsert() {
        String serviceId = "service-foo";
        DateTime modifiedTime = new DateTime();
        DCAEServiceTransactionDAO.DCAEServiceTransactionContext context
                = new DCAEServiceTransactionDAO.DCAEServiceTransactionContext(serviceId, modifiedTime);

        DCAEServicesDAO servicesDao = mock(DCAEServicesDAO.class);
        DCAEServicesComponentsMapsDAO componentsMapsDao = mock(DCAEServicesComponentsMapsDAO.class);
        DCAEServiceComponentsDAO componentsDao = mock(DCAEServiceComponentsDAO.class);

        DCAEServiceTransactionDAO transactionDao = new DCAEServiceTransactionDAO() {

            public DCAEServicesDAO getServicesDAO() {
                return servicesDao;
            }

            public DCAEServicesComponentsMapsDAO getServicesComponentsMappingDAO() {
                return componentsMapsDao;
            }

            public DCAEServiceComponentsDAO getComponentsDAO() {
                return componentsDao;
            }
        };

        try {
            transactionDao.insert(context);
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected error");
        }

    }
}
