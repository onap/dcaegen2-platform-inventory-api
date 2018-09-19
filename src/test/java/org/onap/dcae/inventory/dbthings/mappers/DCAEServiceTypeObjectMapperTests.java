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

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Created by mhwang on 10/2/17.
 */
public class DCAEServiceTypeObjectMapperTests {

    @Test
    public void testMap() {
        ResultSet resultSet = mock(ResultSet.class);
        DCAEServiceTypeObjectMapper mapper = new DCAEServiceTypeObjectMapper();

        try {
            when(resultSet.getArray(anyString())).thenReturn(new Array() {
                @Override
                public String getBaseTypeName() throws SQLException {
                    return null;
                }

                @Override
                public int getBaseType() throws SQLException {
                    return 0;
                }

                @Override
                public Object getArray() throws SQLException {
                    return new String[10];
                }

                @Override
                public Object getArray(Map<String, Class<?>> map) throws SQLException {
                    return null;
                }

                @Override
                public Object getArray(long index, int count) throws SQLException {
                    return null;
                }

                @Override
                public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
                    return null;
                }

                @Override
                public ResultSet getResultSet() throws SQLException {
                    return null;
                }

                @Override
                public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
                    return null;
                }

                @Override
                public ResultSet getResultSet(long index, int count) throws SQLException {
                    return null;
                }

                @Override
                public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
                    return null;
                }

                @Override
                public void free() throws SQLException {

                }
            });
            TestCase.assertNotNull(mapper.map(0, resultSet, null));

            when(resultSet.getArray("service_ids")).thenReturn(null);
            when(resultSet.getArray("service_locations")).thenReturn(null);
            TestCase.assertNotNull(mapper.map(0, resultSet, null));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

}
