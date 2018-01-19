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

package org.onap.dcae.inventory.providers;

import io.swagger.api.NotFoundException;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created by mhwang on 10/2/17.
 */
public class NotFoundExceptionMapperTests {

    @Test
    public void testNotFoundExceptionMapper() {
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        Response response = mapper.toResponse(new NotFoundException(100, "Some error message"));
        assertEquals(response.getStatus(), 404);
    }

}
