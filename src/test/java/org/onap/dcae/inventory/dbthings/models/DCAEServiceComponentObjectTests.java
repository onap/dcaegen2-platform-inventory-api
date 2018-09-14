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
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import io.swagger.model.DCAEServiceComponentRequest;

/**
 * Created by mhwang on 9/27/17.
 */
public class DCAEServiceComponentObjectTests {

    @Test
    public void testConstructionByRequest() {
        DCAEServiceComponentRequest request = (new DCAEServiceComponentRequest()).componentId("some-component-id")
                .componentType("scary-component-type");
        request.setComponentSource("controller");
        request.setShareable(0);

        DCAEServiceComponentObject object = new DCAEServiceComponentObject(request);
        assertEquals(object.getComponentId(), request.getComponentId());
        assertNotNull(object.getCreated());
        assertNotNull(object.getModified());
    }

    @Test
    public void testConstructionForUpdating() {
        DCAEServiceComponentRequest requestFirst = (new DCAEServiceComponentRequest()).componentId("some-component-id")
                .componentType("scary-component-type");
        requestFirst.setComponentSource("controller");
        requestFirst.setShareable(0);

        DCAEServiceComponentObject objectFirst = new DCAEServiceComponentObject(requestFirst);

        DCAEServiceComponentRequest requestSecond = (new DCAEServiceComponentRequest()).componentId("some-other-component-id")
                .componentType("happy-component-type");
        requestFirst.setComponentSource("controllerless");
        requestFirst.setShareable(1);

        DCAEServiceComponentObject objectUpdated = new DCAEServiceComponentObject(objectFirst, requestSecond);
        assertEquals(objectUpdated.getComponentId(), requestFirst.getComponentId());
        assertEquals(objectUpdated.getCreated(), objectFirst.getCreated());
        assertEquals(objectUpdated.getComponentType(), requestSecond.getComponentType());
        assertEquals(objectUpdated.getComponentSource(), requestSecond.getComponentSource());
        assertEquals(objectUpdated.getShareable(), requestSecond.getShareable());
        assertNotSame(objectUpdated.getModified(), objectFirst.getModified());
    }

}
