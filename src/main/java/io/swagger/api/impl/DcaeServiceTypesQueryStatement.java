/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2020 Nokia Intellectual Property. All rights reserved.
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
package io.swagger.api.impl;

import org.glassfish.jersey.internal.util.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DcaeServiceTypesQueryStatement {
    private final StringBuilder sb = new StringBuilder("select * from ");
    private final List<String> whereClauses = new ArrayList<>();

    private static DcaeServiceTypesQueryStatement create() {
        return new DcaeServiceTypesQueryStatement();
    }


    public static String create(String typeName, Boolean onlyLatest, Boolean onlyActive, String vnfType,
                                String serviceId, String serviceLocation, String asdcServiceId, String asdcResourceId,
                                String owner, String application, String component) {

        String from = resolveFrom(onlyLatest);

        final DcaeServiceTypesQueryStatement queryStatement = DcaeServiceTypesQueryStatement.create();
        queryStatement.from(from)
                .where()
                .andIfNotNull(typeName, queryStatement::typeName)
                .andIfNotNull(vnfType, queryStatement::vnfType)
                .andIfNotNull(serviceId, queryStatement::serviceId)
                .andIfNotNull(serviceLocation, queryStatement::serviceLocation)
                .andIfNotNull(asdcServiceId, queryStatement::asdcServiceId)
                .andIfNotNull(asdcResourceId, queryStatement::asdcResourceId)
                .andIfNotNull(owner, queryStatement::owner)
                .andIfNotNull(application, queryStatement::application)
                .andIfNotNull(component, queryStatement::component)
                .and().createdStatement()
                .andIfTrue(onlyActive, queryStatement::activeOnly);

        return queryStatement.build();
    }

    private DcaeServiceTypesQueryStatement where() {
        return this;
    }

    private DcaeServiceTypesQueryStatement and() {
        return this;
    }

    private DcaeServiceTypesQueryStatement andIfNotNull(String value, Function<String, DcaeServiceTypesQueryStatement> callback) {
        if (value != null) {
            return callback.apply(value);
        }

        return this;
    }

    private DcaeServiceTypesQueryStatement andIfTrue(boolean value, Producer<DcaeServiceTypesQueryStatement> callback) {
        if (value) {
            return callback.call();
        }

        return this;
    }

    private DcaeServiceTypesQueryStatement from(String from) {
        sb.append(from);
        return this;
    }


    private DcaeServiceTypesQueryStatement typeName(String typeName) {
        if (!typeName.contains("*")) {
            whereClauses.add(":typeName = type_name");
        } else {
            whereClauses.add("type_name LIKE :typeName");
        }

        return this;
    }

    private DcaeServiceTypesQueryStatement vnfType(String vnfType) {
        whereClauses.add("lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[])");
        return this;
    }

    private DcaeServiceTypesQueryStatement serviceId(String serviceId) {
        whereClauses.add("(:serviceId = any(service_ids) or service_ids = \'{}\' or service_ids is null)");
        return this;
    }

    private DcaeServiceTypesQueryStatement serviceLocation(String serviceLocation) {
        whereClauses.add("(:serviceLocation = any(service_locations) or service_locations = \'{}\' or service_locations is null)");
        return this;
    }

    private DcaeServiceTypesQueryStatement asdcServiceId(String asdcServiceId) {
        if (asdcServiceId.equalsIgnoreCase("NONE")) {
            whereClauses.add("asdc_service_id is null");
        } else {
            whereClauses.add(":asdcServiceId = asdc_service_id");
        }

        return this;
    }

    private DcaeServiceTypesQueryStatement asdcResourceId(String asdcResourceId) {
        if (asdcResourceId.equalsIgnoreCase("NONE")) {
            whereClauses.add("asdc_resource_id is null");
        } else {
            whereClauses.add(":asdcResourceId = asdc_resource_id");
        }

        return this;
    }

    private DcaeServiceTypesQueryStatement owner(String owner) {
        whereClauses.add(":owner = owner");
        return this;
    }

    private DcaeServiceTypesQueryStatement application(String application) {
        whereClauses.add(":application = application");
        return this;
    }


    private DcaeServiceTypesQueryStatement component(String component) {
        whereClauses.add(":component = component");
        return this;
    }

    private DcaeServiceTypesQueryStatement createdStatement() {
        whereClauses.add("created < :createdCutoff");
        return this;
    }

    private DcaeServiceTypesQueryStatement activeOnly() {
        whereClauses.add("deactivated is null");
        return this;
    }

    private String build() {
        if (!whereClauses.isEmpty()) {
            sb.append(" where ");
            sb.append(String.join(" and ", whereClauses));
        }

        // Sort by created timestamp - always descending.
        sb.append(" order by created desc");

        return sb.toString();
    }

    private static String resolveFrom(Boolean onlyLatest) {
        String from;
        if (onlyLatest) {
            // Use the view which filters types that are of only the latest versions
            from = "dcae_service_types_latest";
        } else {
            from = "dcae_service_types";
        }
        return from;
    }
}
