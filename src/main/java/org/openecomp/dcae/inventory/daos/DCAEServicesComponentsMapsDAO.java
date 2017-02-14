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

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * This class manages the joining table called "dcae_services_components_maps" which maps the many-to-many relationship
 * between DCAE services and DCAE service components.
 *
 * Created by mhwang on 4/19/16.
 */
public interface DCAEServicesComponentsMapsDAO extends InventoryDAO {

    @SqlQuery("select exists (select * from information_schema.tables where table_name = \'dcae_services_components_maps\')")
    Boolean checkIfTableExists();

    @SqlUpdate("create table dcae_services_components_maps (service_id varchar not null references dcae_services (service_id), " +
            "component_id varchar not null references dcae_service_components (component_id), " +
            "created timestamp not null, primary key (service_id, component_id))")
    void createTable();

    @SqlUpdate("insert into dcae_services_components_maps (service_id, component_id, created) values (:serviceId, :componentId, :created)")
    void insert(@Bind("serviceId") String serviceId, @Bind("componentId") String componentId, @Bind("created") DateTime created);

    @SqlUpdate("delete from dcae_services_components_maps where service_id = :serviceId and component_id = :componentId")
    void delete(@Bind("serviceId") String serviceId, @Bind("componentId") String componentId);

}
