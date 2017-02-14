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

package org.openecomp.dcae.inventory.daos;

import org.openecomp.dcae.inventory.InventoryConfiguration;
import io.dropwizard.setup.Environment;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Created by mhwang on 3/8/17.
 */
public class InventoryDAOManagerTests {

    @Test(expected=InventoryDAOManager.InventoryDAOManagerSetupException.class)
    public void testInitializeStrictness() {
        InventoryDAOManager daoManager = InventoryDAOManager.getInstance();
        Environment environment = mock(Environment.class);
        InventoryConfiguration configuration = mock(InventoryConfiguration.class);
        // This should be ok
        daoManager.setup(environment, configuration);
        // Cannot do another call
        daoManager.setup(environment, configuration);
    }

}
