/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.dcae.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by mhwang on 4/11/16.
 */
public class InventoryConfiguration extends Configuration {

    public static class DatabusControllerConnectionConfiguration {
        @NotEmpty
        @JsonProperty
        private String host;

        @NotNull
        @JsonProperty
        private Integer port;

        @NotNull
        @JsonProperty
        private String mechId;

        @NotNull
        @JsonProperty
        private String password;

        @JsonProperty
        private Boolean required = true;

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }

        public String getMechId() {
            return mechId;
        }

        public String getPassword() {
            return password;
        }

        public Boolean getRequired() {
            return this.required;
        }
    }

    @NotEmpty
    private String defaultName = "DCAEInventory";

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    @JsonProperty
    private DatabusControllerConnectionConfiguration databusControllerConnection = new DatabusControllerConnectionConfiguration();

    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    public DataSourceFactory getDataSourceFactory() {
        return this.database;
    }

    public DatabusControllerConnectionConfiguration getDatabusControllerConnection() {
        return databusControllerConnection;
    }

    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

}
