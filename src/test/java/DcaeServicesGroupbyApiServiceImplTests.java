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

import io.swagger.api.impl.DcaeServicesGroupbyApiServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.dcae.inventory.daos.InventoryDAOManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mhwang on 9/27/17.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class DcaeServicesGroupbyApiServiceImplTests {

    @Before
    public void setupClass() {
        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDAOManager = mock(InventoryDAOManager.class);

        when(InventoryDAOManager.getInstance()).thenReturn(mockDAOManager);
    }

    @Test
    public void testBadRequest() {
        DcaeServicesGroupbyApiServiceImpl api = new DcaeServicesGroupbyApiServiceImpl();

        String propertyName = "non-existent";
        UriInfo uriInfo = new Util.FakeUriInfo();
        Response response = api.dcaeServicesGroupbyPropertyNameGet(propertyName, uriInfo, null);
        assertEquals(response.getStatus(), 400);
    }

    /*
    Commented this unit test because could not get past Nullpointer in the line trying to mock the explicit "bind" function
    call. Mockito does not handle mocking overloaded functions well so it goes into the actual method where an member variable
    called foreman is null.
    @Test
    public void testNoResults() {
        DcaeServicesGroupbyApiServiceImpl api = new DcaeServicesGroupbyApiServiceImpl();

        String propertyName = "type";
        UriInfo uriInfo = new Util.FakeUriInfo();

        Handle mockHandle = mock(Handle.class);
        when(InventoryDAOManager.getInstance().getHandle()).thenReturn(mockHandle);
        Query mockQueryGeneric = mock(Query.class);
        when(mockHandle.createQuery(any())).thenReturn(mockQueryGeneric);
        // LOOK HERE!
        when(mockQueryGeneric.bind(any(), DCAEServiceObject.DCAEServiceStatus.RUNNING)).thenReturn(mockQueryGeneric);
        when(mockQueryGeneric.list()).thenReturn(null);

        Response response = api.dcaeServicesGroupbyPropertyNameGet(propertyName, uriInfo, null);
        assertEquals(response.getStatus(), 400);
    }
    */

}
