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

package org.onap.dcae.inventory.exceptions.mappers;

import org.onap.dcae.inventory.exceptions.DCAEControllerTimeoutException;

import javax.ws.rs.core.Response;

/**
 * Created by mhwang on 8/23/16.
 */
public class DCAEControllerTimeoutExceptionMapper extends DCAEControllerExceptionMapper<DCAEControllerTimeoutException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.GATEWAY_TIMEOUT;
    }

}
