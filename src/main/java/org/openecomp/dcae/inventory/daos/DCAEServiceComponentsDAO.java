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

import org.openecomp.dcae.inventory.dbthings.mappers.DCAEServiceComponentObjectMapper;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceComponentObject;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 * Created by mhwang on 4/19/16.
 */
public interface DCAEServiceComponentsDAO extends InventoryDAO {

    @SqlQuery("select exists (select * from information_schema.tables where table_name = \'dcae_service_components\')")
    Boolean checkIfTableExists();

    @SqlUpdate("create table dcae_service_components (component_id varchar not null primary key, component_type varchar not null, " +
            "component_source varchar not null, shareable integer default 0, created timestamp not null, modified timestamp not null)")
    void createTable();

    @SqlUpdate("insert into dcae_service_components (component_id, component_type, component_source, shareable, created, modified) " +
            "values (:componentId, :componentType, :componentSource, :shareable, :created, :modified)")
    void insert(@BindBean DCAEServiceComponentObject componentObject);

    @SqlUpdate("update dcae_service_components set component_type = :componentType, component_source = :componentSource, " +
            "shareable = :shareable, modified = :modified where component_id = :componentId")
    void update(@BindBean DCAEServiceComponentObject componentObject);

    @Mapper(DCAEServiceComponentObjectMapper.class)
    @SqlQuery("select c.* from dcae_services_components_maps m join dcae_service_components c " +
            "on m.component_id = c.component_id where m.service_id = :it")
    List<DCAEServiceComponentObject> getByServiceId(@Bind String serviceId);

    @Mapper(DCAEServiceComponentObjectMapper.class)
    @SqlQuery("select c.* from dcae_service_components c where c.component_id = :it")
    DCAEServiceComponentObject getByComponentId(@Bind String componentId);

}
