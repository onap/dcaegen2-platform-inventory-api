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

package org.onap.dcae.inventory.dbthings.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.swagger.model.DCAEServiceRequest;

/**
 * Created by mhwang on 9/27/17.
 */
public class DCAEServiceObjectTests {

    @Test
    public void testConstructionByRequest() {
        DCAEServiceRequest request = new DCAEServiceRequest();
        request.setTypeId("some-type-id");
        request.setVnfType("foo-vnf-type");
        request.setVnfLocation("san-janero");

        String serviceId = "some-service-id";

        DCAEServiceObject object = new DCAEServiceObject(serviceId, request);
        assertEquals(object.getServiceId(), serviceId);
        assertNotNull(object.getCreated());
        assertNotNull(object.getModified());
    }

    @Test
    public void testConstructionByUpdating() {
        DCAEServiceRequest requestFirst = new DCAEServiceRequest();
        requestFirst.setTypeId("some-type-id");
        requestFirst.setVnfType("foo-vnf-type");
        requestFirst.setVnfLocation("san-janero");

        String serviceId = "some-service-id";

        DCAEServiceObject objectFirst = new DCAEServiceObject(serviceId, requestFirst);

        DCAEServiceRequest requestSecond = new DCAEServiceRequest();
        requestFirst.setTypeId("other-type-id");
        requestFirst.setVnfType("bar-vnf-type");
        requestFirst.setVnfLocation("san-junipero");

        DCAEServiceObject objectUpdated = new DCAEServiceObject(objectFirst, requestSecond);
        assertEquals(objectUpdated.getServiceId(), objectFirst.getServiceId());
        assertEquals(objectUpdated.getTypeId(), requestSecond.getTypeId());
        assertEquals(objectUpdated.getVnfType(), requestSecond.getVnfType());
        assertEquals(objectUpdated.getVnfLocation(), requestSecond.getVnfLocation());
    }

}
