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

package org.onap.dcae.inventory.dbthings.models;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import io.swagger.model.DCAEServiceTypeRequest;

/**
 * Created by sh1986 on 2/28/19.
 */
public class DCAEServiceTypeObjectTests {

    @Test
    public void testConstructionByRequest() {
    	DCAEServiceTypeObject object = new DCAEServiceTypeObject();
        object.setTypeVersion(1);
        object.setTypeName("abc"); 
        object.setOwner("tester");
        object.setBlueprintTemplate("{ blueprint template goes here }");
        object.setTypeId("some-type-id"); 

        String typeId = "some-type-id";

        assertEquals(object.getTypeId(), typeId);
    }
	
    @Test
    public void testConstructionByUpdating() {
    	
    	DCAEServiceTypeObject objectFirst = new DCAEServiceTypeObject();
        
    	objectFirst.setTypeVersion(1);
    	objectFirst.setTypeName("abc"); 
    	objectFirst.setOwner("tester");
    	objectFirst.setBlueprintTemplate("{ blueprint template goes here }");
    	objectFirst.setTypeId("some-type-id");
    	
        DCAEServiceTypeObject objectUpdated = new DCAEServiceTypeObject();
        
        objectUpdated.setTypeVersion(1);
        objectUpdated.setTypeName("abc"); 
        objectUpdated.setOwner("tester");
        objectUpdated.setBlueprintTemplate("{ blueprint template goes here }");
        objectUpdated.setTypeId("some-type-id");
        
        assertEquals(objectUpdated.getTypeId(), objectFirst.getTypeId());
        assertEquals(objectUpdated.getTypeId(), objectUpdated.getTypeId());
        assertEquals(objectUpdated.getTypeVersion(), objectUpdated.getTypeVersion());
        assertEquals(objectUpdated.getBlueprintTemplate(), objectUpdated.getBlueprintTemplate());
    }
	 
}
