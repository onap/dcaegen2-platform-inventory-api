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

import io.swagger.model.DCAEServiceComponentRequest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by mhwang on 4/19/16.
 */
public class DCAEServiceComponentObject {

    private String componentId = null;
    private DateTime created = null;
    private DateTime modified = null;
    private String componentType = null;
    private String componentSource = null;
    private Integer shareable = null;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
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

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentSource() {
        return componentSource;
    }

    public void setComponentSource(String componentSource) {
        this.componentSource = componentSource;
    }

    public Integer getShareable() {
        return shareable;
    }

    public void setShareable(Integer shareable) {
        this.shareable = shareable;
    }

    public DCAEServiceComponentObject() {
    }

    /**
     * Intended to be used for inserts - new objects.
     *
     * @param request
     */
    public DCAEServiceComponentObject(DCAEServiceComponentRequest request) {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        this.setComponentId(request.getComponentId());
        this.setComponentType(request.getComponentType());
        this.setComponentSource(request.getComponentSource());
        this.setCreated(now);
        this.setModified(now);
        this.setShareable(request.getShareable());
    }

    /**
     * Intended to be used for updates - some fields should not be updated.
     *
     * @param source
     * @param updateRequest
     */
    public DCAEServiceComponentObject(DCAEServiceComponentObject source, DCAEServiceComponentRequest updateRequest) {
        // Immutable fields
        this.setComponentId(source.getComponentId());
        this.setCreated(source.getCreated());

        // Mutable fields
        this.setComponentType(updateRequest.getComponentType());
        this.setComponentSource(updateRequest.getComponentSource());
        this.setShareable(updateRequest.getShareable());
        this.setModified(DateTime.now(DateTimeZone.UTC));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DCAEServiceComponentObject {\n");

        sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
        sb.append("    componentType: ").append(toIndentedString(componentType)).append("\n");
        sb.append("    componentSource: ").append(toIndentedString(componentSource)).append("\n");
        sb.append("    shareable: ").append(toIndentedString(shareable)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
