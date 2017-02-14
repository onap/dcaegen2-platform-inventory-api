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

import org.openecomp.dcae.inventory.dbthings.mappers.DCAEServiceObjectMapper;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.util.IntegerMapper;


/**
 * Created by mhwang on 4/19/16.
 */
public interface DCAEServicesDAO extends InventoryDAO {

    @SqlQuery("select exists (select * from information_schema.tables where table_name = \'dcae_services\')")
    Boolean checkIfTableExists();

    @SqlUpdate("create table dcae_services (service_id varchar not null primary key, type_id varchar not null, " +
            "vnf_id varchar not null, vnf_type varchar not null, vnf_location varchar not null, deployment_ref varchar, " +
            "created timestamp not null, modified timestamp not null, status varchar not null)")
    void createTable();

    @SqlUpdate("insert into dcae_services(service_id, type_id, vnf_id, vnf_type, vnf_location, deployment_ref, " +
            "created, modified, status) values (:serviceId, :typeId, :vnfId, :vnfType, :vnfLocation, :deploymentRef, " +
            ":created, :modified, :status)")
    void insert(@BindBean DCAEServiceObject serviceObject);

    @SqlUpdate("update dcae_services set type_id = :typeId, vnf_id = :vnfId, vnf_type = :vnfType, " +
            "vnf_location = :vnfLocation, deployment_ref = :deploymentRef, modified = :modified, status = :status " +
            "where service_id = :serviceId")
    void update(@BindBean DCAEServiceObject serviceObject);

    @Mapper(DCAEServiceObjectMapper.class)
    @SqlQuery("select * from dcae_services where status = :status and service_id = :serviceId")
    DCAEServiceObject getByServiceId(@Bind("status") DCAEServiceObject.DCAEServiceStatus status, @Bind("serviceId") String serviceId);

    @Mapper(DCAEServiceObjectMapper.class)
    @SqlQuery("select * from dcae_services where service_id = :serviceId")
    DCAEServiceObject getByServiceId(@Bind("serviceId") String serviceId);

    @SqlUpdate("update dcae_services set modified = :modified, status = :status where service_id = :serviceId")
    void updateStatusByServiceId(@Bind("modified") DateTime modified,
                                 @Bind("status") DCAEServiceObject.DCAEServiceStatus status,
                                 @Bind("serviceId") String serviceId);

    @Mapper(IntegerMapper.class)
    @SqlQuery("select count(1) from dcae_services where status = :status and type_id = :typeId")
    Integer countByType(@Bind("status") DCAEServiceObject.DCAEServiceStatus status, @Bind("typeId") String typeId);

}


