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

import org.openecomp.dcae.inventory.dbthings.mappers.DCAEServiceTypeObjectMapper;

import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceTypeObject;
import io.swagger.model.DCAEServiceTypeRequest;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * DCAE service type records are no longer treated immutable because versioning is handled by clients
 * to DCAE inventory i.e. ASDC. There is field "deactivated" that determines if a service type is active.
 * Inserts and updates automatically activates a service type while deleting deactivates a service type.
 *
 * Created by mhwang on 4/19/16.
 */
public interface DCAEServiceTypesDAO extends InventoryDAO {

    @SqlQuery("select exists (select * from information_schema.tables where table_name = \'dcae_service_types\')")
    Boolean checkIfTableExists();

    /**
     * Note that service_ids and service_locations are nullable fields. This might not be the right decision but because
     * the resource model allows for nulls, thought it should consistent.
     */
    @SqlUpdate("create table dcae_service_types (type_id varchar not null, type_version integer not null, " +
            "type_name varchar not null, owner varchar not null, blueprint_template text not null, " +
            "vnf_types varchar[] not null, service_ids varchar[], service_locations varchar[], " +
            "asdc_service_id varchar, asdc_resource_id varchar, " +
            "created timestamp not null, deactivated timestamp, constraint pk_type_created primary key (type_id))")
    void createTable();

    // REVIEW: asdcServiceId and asdcResourceId is implicitly part of the unique key and thus shouldn't be updated.
    @SqlUpdate("insert into dcae_service_types(type_id, type_version, type_name, owner, blueprint_template, vnf_types, " +
            "service_ids, service_locations, asdc_service_id, asdc_resource_id, created, deactivated) " +
            "values (:typeId, :typeVersion, :typeName, :owner, :blueprintTemplate, :vnfTypes, :serviceIds, " +
            ":serviceLocations, :asdcServiceId, :asdcResourceId, :created, null)")
    void insert(@BindBean DCAEServiceTypeObject serviceObject);

    @SqlUpdate("update dcae_service_types set " +
            "owner = :owner, blueprint_template = :blueprintTemplate, vnf_types = :vnfTypes, " +
            "service_ids = :serviceIds, service_locations = :serviceLocations, created = :created, " +
            "deactivated = null where type_id = :typeId")
    void update(@BindBean DCAEServiceTypeObject serviceObject);

    @SqlUpdate("update dcae_service_types set deactivated = (now() at time zone 'utc') where type_id = :typeId")
    void deactivateExisting(@Bind("typeId") String typeId);

    @Mapper(DCAEServiceTypeObjectMapper.class)
    @SqlQuery("select * from dcae_service_types where type_id = :it")
    DCAEServiceTypeObject getByTypeId(@Bind String typeId);

    @Mapper(DCAEServiceTypeObjectMapper.class)
    @SqlQuery("select * from dcae_service_types where deactivated is null and type_id = :it")
    DCAEServiceTypeObject getByTypeIdActiveOnly(@Bind String typeId);

    @Mapper(DCAEServiceTypeObjectMapper.class)
    @SqlQuery("select * from dcae_service_types where type_name = :typeName and type_version = :typeVersion " +
            "and asdc_service_id is null and asdc_resource_id is null")
    DCAEServiceTypeObject getByRequestFromNotASDC(@BindBean DCAEServiceTypeRequest serviceTypeObject);

    @Mapper(DCAEServiceTypeObjectMapper.class)
    @SqlQuery("select * from dcae_service_types where type_name = :typeName and type_version = :typeVersion " +
            "and asdc_service_id = :asdcServiceId and asdc_resource_id = :asdcResourceId")
    DCAEServiceTypeObject getByRequestFromASDC(@BindBean DCAEServiceTypeRequest serviceTypeObject);

}
