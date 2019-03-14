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

package org.onap.dcae.inventory.dbthings;

import org.junit.Test;
import org.skife.jdbi.v2.tweak.Argument;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mhwang on 3/14/19.
 */
public class StringListArgumentTest {

    @Test
    public void testAccepts() {
        StringListArgument test = new StringListArgument();
        assertEquals(test.accepts(null, new ArrayList<>(), null), true);
        assertEquals(test.accepts(null, null, null), false);
        assertEquals(test.accepts(null, "fail case", null), false);
    }

    @Test
    public void testStringListArgumentBuild() {
        StringListArgument test = new StringListArgument();
        assertNotNull(test.build(null, null, null));

        List<String> value = new ArrayList<>();
        Argument func = test.build(null, value, null);

        try {
            PreparedStatement statement = mock(PreparedStatement.class);
            Connection conn = mock(Connection.class);
            when(conn.createArrayOf(any(), any())).thenReturn(new Array() {
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
                    return null;
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
            when(statement.getConnection()).thenReturn(conn);
            func.apply(0, statement, null);
        } catch (SQLException e) {
            fail("Unexpected SQLException");
            e.printStackTrace();
        }
    }

}
