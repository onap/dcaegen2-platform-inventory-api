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

package org.openecomp.dcae.inventory.dbthings;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * This class if needed for jdbi queries to be able to properly bind List<String> objects.
 *
 * http://stackoverflow.com/questions/33062516/insert-2d-array-into-postresql-db-using-jdbi
 *
 * Created by mhwang on 5/4/16.
 */
public class StringListArgument implements ArgumentFactory<List<String>> {

    @Override
    public boolean accepts(Class<?> expectedType, Object value, StatementContext statementContext) {
        return value != null && List.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Argument build(Class<?> expectedType, List<String> value, StatementContext statementContext) {
        return new Argument() {
            @Override
            public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
                Array values = statement.getConnection().createArrayOf("varchar", value.toArray());
                statement.setArray(position, values);
            }
        };
    }

}
