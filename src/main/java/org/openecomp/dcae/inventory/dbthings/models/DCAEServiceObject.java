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

package org.openecomp.dcae.inventory.dbthings.models;

import io.swagger.model.DCAEServiceRequest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by mhwang on 4/19/16.
 */
public class DCAEServiceObject {

    public static enum DCAEServiceStatus {
        RUNNING,
        REMOVED
    }

    private String serviceId = null;
    private String typeId = null;
    private DateTime created = null;
    private DateTime modified = null;
    private String vnfId = null;
    private String vnfType = null;
    private String vnfLocation = null;
    private String deploymentRef = null;

    // These properties are meant to be used internally in the service only
    private DCAEServiceStatus status = null;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    public DCAEServiceStatus getStatus() {
        return status;
    }

    public void setStatus(DCAEServiceStatus status) {
        this.status = status;
    }

    public String getVnfId() {
        return vnfId;
    }

    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }

    public String getVnfType() {
        return vnfType;
    }

    public void setVnfType(String vnfType) {
        this.vnfType = vnfType;
    }

    public String getVnfLocation() {
        return vnfLocation;
    }

    public void setVnfLocation(String vnfLocation) {
        this.vnfLocation = vnfLocation;
    }

    public String getDeploymentRef() {
        return deploymentRef;
    }

    public void setDeploymentRef(String deploymentRef) {
        this.deploymentRef = deploymentRef;
    }

    public DCAEServiceObject() {
    }

    // TODO: Move the constructors functionality below out into the actual handlers.

    /**
     * Intended to be used for inserts - new objects.
     *
     * @param serviceId
     * @param request
     */
    public DCAEServiceObject(String serviceId, DCAEServiceRequest request) {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        this.setServiceId(serviceId);
        this.setTypeId(request.getTypeId());
        this.setVnfId(request.getVnfId());
        this.setVnfType(request.getVnfType());
        this.setVnfLocation(request.getVnfLocation());
        this.setDeploymentRef(request.getDeploymentRef());
        this.setCreated(now);
        this.setModified(now);
        // Assumption here is that you are here from the PUT which means that the service is RUNNING.
        this.setStatus(DCAEServiceStatus.RUNNING);
    }

    /**
     * Intended to be used for updates - some fields should not be updated.
     *
     * @param source
     * @param updateRequest
     */
    public DCAEServiceObject(DCAEServiceObject source, DCAEServiceRequest updateRequest) {
        // Immutable fields
        this.setServiceId(source.getServiceId());
        this.setCreated(source.getCreated());

        // Mutable fields
        this.setTypeId(updateRequest.getTypeId());
        this.setVnfId(updateRequest.getVnfId());
        this.setVnfType(updateRequest.getVnfType());
        this.setVnfLocation(updateRequest.getVnfLocation());
        this.setDeploymentRef(updateRequest.getDeploymentRef());
        this.setModified(DateTime.now(DateTimeZone.UTC));
        // Assumption here is that you are here from the PUT which means that the service is RUNNING.
        this.setStatus(DCAEServiceStatus.RUNNING);
    }

}
