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

import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceComponentObject;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * This is based-off of this blog post.
 * http://manikandan-k.github.io/2015/05/10/Transactions_in_jdbi.html
 *
 * Created by mhwang on 4/21/16.
 */
public abstract class DCAEServiceTransactionDAO {

    public static class DCAEServiceTransactionContext {

        private final String serviceId;
        private final DateTime modified;
        private DCAEServiceObject serviceObjectToInsert;
        private DCAEServiceObject serviceObjectToUpdate;
        private List<DCAEServiceComponentObject> componentObjectsToInsert;
        private List<DCAEServiceComponentObject> componentObjectsToUpdate;
        private List<String> mappingsToInsert;
        private List<String> mappingsToDelete;

        public String getServiceId() {
            return serviceId;
        }

        public DateTime getModified() {
            return modified;
        }

        public DCAEServiceObject getServiceObjectToInsert() {
            return serviceObjectToInsert;
        }

        public void setServiceObjectToInsert(DCAEServiceObject serviceObjectToInsert) {
            this.serviceObjectToInsert = serviceObjectToInsert;
        }

        public DCAEServiceObject getServiceObjectToUpdate() {
            return serviceObjectToUpdate;
        }

        public void setServiceObjectToUpdate(DCAEServiceObject serviceObjectToUpdate) {
            this.serviceObjectToUpdate = serviceObjectToUpdate;
        }

        public List<DCAEServiceComponentObject> getComponentObjectsToInsert() {
            return componentObjectsToInsert;
        }

        public List<DCAEServiceComponentObject> addComponentObjectToInsert(DCAEServiceComponentObject componentObject) {
            this.componentObjectsToInsert.add(componentObject);
            return this.componentObjectsToInsert;
        }

        public List<DCAEServiceComponentObject> getComponentObjectsToUpdate() {
            return componentObjectsToUpdate;
        }

        public List<DCAEServiceComponentObject> addComponentObjectToUpdate(DCAEServiceComponentObject componentObject) {
            this.componentObjectsToUpdate.add(componentObject);
            return this.componentObjectsToUpdate;
        }

        public List<String> getMappingsToInsert() {
            return mappingsToInsert;
        }

        public List<String> addMappingsToInsert(String componentId) {
            this.mappingsToInsert.add(componentId);
            return this.mappingsToInsert;
        }

        public List<String> getMappingsToDelete() {
            return mappingsToDelete;
        }

        public List<String> addMappingsToDelete(String componentId) {
            this.mappingsToDelete.add(componentId);
            return this.mappingsToDelete;
        }

        public DCAEServiceTransactionContext(String serviceId, DateTime modified) {
            this.serviceId = serviceId;
            this.modified = modified;
            this.componentObjectsToInsert = new ArrayList<>();
            this.componentObjectsToUpdate = new ArrayList<>();
            this.mappingsToInsert = new ArrayList<>();
            this.mappingsToDelete = new ArrayList<>();
        }

    }

    @CreateSqlObject
    abstract DCAEServicesDAO getServicesDAO();

    @CreateSqlObject
    abstract DCAEServicesComponentsMapsDAO getServicesComponentsMappingDAO();

    @CreateSqlObject
    abstract DCAEServiceComponentsDAO getComponentsDAO();

    @Transaction
    public void insert(DCAEServiceTransactionContext context) {
        if (context.getServiceObjectToInsert() != null) {
            this.getServicesDAO().insert(context.getServiceObjectToInsert());
        }

        if (context.getServiceObjectToUpdate() != null) {
            this.getServicesDAO().update(context.getServiceObjectToUpdate());
        }

        for (DCAEServiceComponentObject sco : context.getComponentObjectsToInsert()) {
            this.getComponentsDAO().insert(sco);
        }

        for (DCAEServiceComponentObject sco : context.getComponentObjectsToUpdate()) {
            this.getComponentsDAO().update(sco);
        }

        for (String componentId : context.getMappingsToInsert()) {
            this.getServicesComponentsMappingDAO().insert(context.getServiceId(), componentId, context.getModified());
        }

        for (String componentId : context.getMappingsToDelete()) {
            this.getServicesComponentsMappingDAO().delete(context.serviceId, componentId);
        }
    }

}
