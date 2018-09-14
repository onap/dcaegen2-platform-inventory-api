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

package org.onap.dcae.inventory.dbthings.mappers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;

import org.junit.Test;

/**
 * Created by mhwang on 10/2/17.
 */
public class DCAEServiceObjectMapperTests {

    @Test
    public void testMap() {
        ResultSet resultSet = mock(ResultSet.class);
        DCAEServiceObjectMapper mapper = new DCAEServiceObjectMapper();

        try {
            when(resultSet.getString("status")).thenReturn("RUNNING");
            assertNotNull(mapper.map(0, resultSet, null));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

}
