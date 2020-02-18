/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2020 Nokia. All rights reserved.
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.onap.dcae.inventory.daos.InventoryDataAccessManager;
import org.onap.dcae.inventory.dbthings.mappers.DCAEServiceTypeObjectMapper;
import org.onap.dcae.inventory.dbthings.models.DCAEServiceTypeObject;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

class DcaeServiceTypeObjectRepository {
    private static final Logger metricsLogger = LoggerFactory.getLogger("metricsLogger");
    private final InventoryDataAccessManager instance;

    public DcaeServiceTypeObjectRepository(InventoryDataAccessManager instance) {
        this.instance = instance;
    }

    List<DCAEServiceTypeObject> fetch(String typeName, Boolean onlyLatest, Boolean onlyActive, String vnfType,
                                      String serviceId, String serviceLocation, String asdcServiceId,
                                      String asdcResourceId, String application, String component, String owner) {

        List<DCAEServiceTypeObject> serviceTypeObjects;

        // TODO: Make this variable also a URL parameter.
        DateTime createdCutoff = DateTime.now(DateTimeZone.UTC);

        try (Handle jdbiHandle = instance.getHandle()) {
            final String queryStatement = DcaeServiceTypesQueryStatement.create(typeName, onlyLatest, onlyActive,
                    vnfType, serviceId, serviceLocation, asdcServiceId, asdcResourceId, owner, application, component);

            metricsLogger.info("Query created as: {}." + queryStatement);

            Query<DCAEServiceTypeObject> query = getQuery(jdbiHandle, queryStatement);

            if (typeName != null){
                typeName = resolveTypeName(typeName);
            }

            ifNotNullBind(typeName, it -> query.bind("typeName", it));
            ifNotNullBind(vnfType, it -> query.bind("vnfType", it));
            ifNotNullBind(serviceId, it -> query.bind("serviceId", it));
            ifNotNullBind(serviceLocation, it -> query.bind("serviceLocation", it));
            ifNotNoneBind(asdcServiceId, it -> query.bind("asdcServiceId", it));
            ifNotNoneBind(asdcResourceId, it -> query.bind("asdcResourceId", it));
            ifNotNullBind(application, it -> query.bind("application", it));
            ifNotNullBind(component, it -> query.bind("component", it));
            ifNotNullBind(owner, it -> query.bind("owner", it));
            bindCreatedCutoff(createdCutoff, query);

            serviceTypeObjects = query.list();
        }

        return serviceTypeObjects;
    }

    private void ifNotNullBind(String value, Consumer<String> bind) {
        if (value != null) {
            bind.accept(value);
        }
    }

    private void ifNotNoneBind(String value, Consumer<String> bind) {
        if (value != null && !"NONE".equalsIgnoreCase(value)) {
            bind.accept(value);
        }
    }

    void bindCreatedCutoff(DateTime createdCutoff, Query<DCAEServiceTypeObject> query) {
        query.bind("createdCutoff", createdCutoff);
    }

    Query<DCAEServiceTypeObject> getQuery(Handle jdbiHandle, String queryStatement) {
        return jdbiHandle.createQuery(queryStatement).map(new DCAEServiceTypeObjectMapper());
    }

    static String resolveTypeName(String typeName){
        if (typeName.contains("*")) {
            return typeName.replaceAll("\\*", "%");
        }
        return typeName;
    }
}
