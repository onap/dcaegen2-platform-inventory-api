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

package org.openecomp.dcae.inventory.dbthings.mappers;

import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceComponentObject;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mhwang on 4/19/16.
 */
public class DCAEServiceComponentObjectMapper implements ResultSetMapper<DCAEServiceComponentObject> {

    @Override
    public DCAEServiceComponentObject map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        DCAEServiceComponentObject object = new DCAEServiceComponentObject();
        object.setComponentId(resultSet.getString("component_id"));
        object.setComponentType(resultSet.getString("component_type"));
        object.setComponentSource(resultSet.getString("component_source"));
        object.setShareable(resultSet.getInt("shareable"));
        object.setCreated(new DateTime(resultSet.getTimestamp("created")));
        object.setModified(new DateTime(resultSet.getTimestamp("modified")));
        return object;
    }

}
