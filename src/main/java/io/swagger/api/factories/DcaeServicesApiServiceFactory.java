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

package io.swagger.api.factories;

import org.openecomp.dcae.inventory.clients.DCAEControllerClient;
import org.openecomp.dcae.inventory.clients.DatabusControllerClient;
import io.swagger.api.DcaeServicesApiService;
import io.swagger.api.impl.DcaeServicesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public class DcaeServicesApiServiceFactory {

    // Yes I agree this code is not great and I blame for Swagger for putting me in this spot.
    private static DCAEControllerClient dcaeControllerClient;
    private static DatabusControllerClient databusControllerClient;

    public static void setDcaeControllerClient(DCAEControllerClient dcaeControllerClient) {
        DcaeServicesApiServiceFactory.dcaeControllerClient = dcaeControllerClient;
    }

    public static void setDatabusControllerClient(DatabusControllerClient databusControllerClient) {
        DcaeServicesApiServiceFactory.databusControllerClient = databusControllerClient;
    }

    public static DcaeServicesApiService getDcaeServicesApi() {
        return new DcaeServicesApiServiceImpl(dcaeControllerClient, databusControllerClient);
    }

}
