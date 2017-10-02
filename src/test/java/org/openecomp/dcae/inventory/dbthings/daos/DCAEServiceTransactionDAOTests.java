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

package org.openecomp.dcae.inventory.dbthings.daos;

import org.joda.time.DateTime;
import org.junit.Test;
import org.openecomp.dcae.inventory.daos.DCAEServiceComponentsDAO;
import org.openecomp.dcae.inventory.daos.DCAEServiceTransactionDAO;
import org.openecomp.dcae.inventory.daos.DCAEServicesComponentsMapsDAO;
import org.openecomp.dcae.inventory.daos.DCAEServicesDAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

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

        DCAEServicesDAO servicesDAO = mock(DCAEServicesDAO.class);
        DCAEServicesComponentsMapsDAO componentsMapsDAO = mock(DCAEServicesComponentsMapsDAO.class);
        DCAEServiceComponentsDAO componentsDAO = mock(DCAEServiceComponentsDAO.class);

        DCAEServiceTransactionDAO transactionDAO = new DCAEServiceTransactionDAO() {

            public DCAEServicesDAO getServicesDAO() {
                return servicesDAO;
            }

            public DCAEServicesComponentsMapsDAO getServicesComponentsMappingDAO() {
                return componentsMapsDAO;
            }

            public DCAEServiceComponentsDAO getComponentsDAO() {
                return componentsDAO;
            }
        };

        try {
            transactionDAO.insert(context);
            assertTrue(true);
        } catch(Exception e) {
            fail("Unexpected error");
        }

    }
}
