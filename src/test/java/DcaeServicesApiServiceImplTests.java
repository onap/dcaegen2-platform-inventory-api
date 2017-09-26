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

import io.swagger.api.NotFoundException;
import io.swagger.api.impl.DcaeServicesApiServiceImpl;
import io.swagger.model.DCAEService;
import io.swagger.model.DCAEServiceComponent;
import io.swagger.model.DCAEServiceRequest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.openecomp.dcae.inventory.clients.DCAEControllerClient;
import org.openecomp.dcae.inventory.clients.DatabusControllerClient;
import org.openecomp.dcae.inventory.daos.DCAEServiceComponentsDAO;
import org.openecomp.dcae.inventory.daos.DCAEServiceTypesDAO;
import org.openecomp.dcae.inventory.daos.DCAEServicesDAO;
import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import org.openecomp.dcae.inventory.dbthings.mappers.DCAEServiceObjectMapper;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceComponentObject;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.skife.jdbi.v2.Foreman;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.tweak.Argument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created by mhwang on 9/25/17.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class DcaeServicesApiServiceImplTests {

    private final static Logger LOG = LoggerFactory.getLogger(DcaeServicesApiServiceImplTests.class);

    private DCAEServiceTypesDAO mockTypesDAO = null;
    private DCAEServicesDAO mockServicesDAO = null;
    private DCAEServiceComponentsDAO mockComponentsDAO = null;

    @Before
    public void setupClass() {
        mockTypesDAO = mock(DCAEServiceTypesDAO.class);
        mockServicesDAO = mock(DCAEServicesDAO.class);
        mockComponentsDAO = mock(DCAEServiceComponentsDAO.class);

        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDAOManager = mock(InventoryDAOManager.class);

        when(InventoryDAOManager.getInstance()).thenReturn(mockDAOManager);
        when(mockDAOManager.getDCAEServicesDAO()).thenReturn(mockServicesDAO);
        when(mockDAOManager.getDCAEServiceComponentsDAO()).thenReturn(mockComponentsDAO);
        when(mockDAOManager.getDCAEServiceTypesDAO()).thenReturn(mockTypesDAO);
    }

    @Test
    public void testCreateDCAEService() {
        Method createDCAEService = null;

        // This block is a trick to make a private method accessible for testing
        try {
            createDCAEService = DcaeServicesApiServiceImpl.class.getDeclaredMethod("createDCAEService", DCAEServiceObject.class,
                    Collection.class, UriInfo.class);
            createDCAEService.setAccessible(true);
        } catch(NoSuchMethodException e) {
            fail("Failed to do the reflection trick to test the private method: createDCAEService");
        }

        DCAEControllerClient dcc = mock(DCAEControllerClient.class);
        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dcc, dbcc);

        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");
        String serviceId = "service-id-123";
        DCAEServiceObject serviceObject = new DCAEServiceObject(serviceId, serviceRequest);
        List<DCAEServiceComponentObject> components = new ArrayList<DCAEServiceComponentObject>();
        UriInfo uriInfo = new Util.FakeUriInfo();

        try {
            DCAEService service = (DCAEService) createDCAEService.invoke(api, serviceObject, components, uriInfo);
            assertEquals(service.getServiceId(), serviceObject.getServiceId());
        } catch(Exception e) {
            fail("Failed to execute the hacked createDCAEService method");
        }
    }

    @Test
    public void testDcaeServicesServiceIdGet() {
        String serviceId = "service-id-123";
        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");
        DCAEServiceObject serviceObject = new DCAEServiceObject(serviceId, serviceRequest);
        when(mockServicesDAO.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId)).thenReturn(serviceObject);
        when(mockComponentsDAO.getByServiceId(serviceId)).thenReturn(new ArrayList<DCAEServiceComponentObject>());

        DCAEControllerClient dcc = mock(DCAEControllerClient.class);
        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dcc, dbcc);
        UriInfo uriInfo = new Util.FakeUriInfo();

        try {
            Response response = api.dcaeServicesServiceIdGet(serviceId, uriInfo, null);
            assertEquals(response.getStatus(), 200);
        } catch(NotFoundException e) {
            fail("Service should have been found");
        }
    }

    /*
    Commented this unit test because could not get past Nullpointer in the line trying to mock the explicit "bind" function
    call. Mockito does not handle mocking overloaded functions well so it goes into the actual method where an member variable
    called foreman is null.
    @Test
    public void testDcaeServicesGet() {
        Handle mockHandle = mock(Handle.class);
        when(InventoryDAOManager.getInstance().getHandle()).thenReturn(mockHandle);
        Query mockQueryGeneric = mock(Query.class);
        Query<DCAEServiceObject> mockQuery = mock(Query.class);
        when(mockHandle.createQuery(any())).thenReturn(mockQueryGeneric);
        when(mockQueryGeneric.map(any(DCAEServiceObjectMapper.class))).thenReturn(mockQuery);
        // LOOK HERE!
        doReturn(null).when(mockQuery.bind(anyString(), any(DateTime.class)));
        when(mockQuery.bind(anyString(), anyInt())).thenReturn(null);

        DCAEControllerClient dcc = mock(DCAEControllerClient.class);
        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dcc, dbcc);

        String typeId = "some-type-id";
        String vnfId = "some-vnf-id";
        String vnfType = "some-vnf-type";
        String vnfLocation = "some-vnf-location";
        String componentType = "some-component-type";
        Boolean shareable = Boolean.TRUE;
        DateTime created = null;
        Integer offset = 0;
        UriInfo uriInfo = new Util.FakeUriInfo();
        SecurityContext securityContext = null;

        when(mockQuery.list()).thenReturn(new ArrayList<DCAEServiceObject>());

        Response response = api.dcaeServicesGet(typeId, vnfId, vnfType, vnfLocation, componentType, shareable, created,
                offset, uriInfo, securityContext);
        assertEquals(response.getStatus(), 200);
    }
    */

    @Test
    public void testDcaeServicesServiceIdPutMissingType() {
        String serviceId = "service-id-123";
        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");

        when(mockTypesDAO.getByTypeIdActiveOnly(serviceRequest.getTypeId())).thenReturn(null);

        DCAEControllerClient dcc = mock(DCAEControllerClient.class);
        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dcc, dbcc);
        UriInfo uriInfo = new Util.FakeUriInfo();

        Response response = api.dcaeServicesServiceIdPut(serviceId, serviceRequest, uriInfo, null);
        assertEquals(response.getStatus(), 422);
    }

    @Test
    public void testDcaeServicesServiceIdDelete() {
        String serviceId = "service-id-123";
        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");
        DCAEServiceObject serviceObject = new DCAEServiceObject(serviceId, serviceRequest);
        when(mockServicesDAO.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId)).thenReturn(serviceObject);

        DCAEControllerClient dcc = mock(DCAEControllerClient.class);
        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dcc, dbcc);
        UriInfo uriInfo = new Util.FakeUriInfo();

        try {
            Response response = api.dcaeServicesServiceIdDelete(serviceId, null);
            assertEquals(response.getStatus(), 200);
        } catch(NotFoundException e) {
            fail("Should have NOT thrown a NotFoundException");
        } catch(Exception e) {
            LOG.error("Unexpected exception", e);
            fail("Unexpected exception");
        }
    }

    @Test
    public void testDcaeServicesServiceIdDeleteMissingService() {
        String serviceId = "service-id-123";
        when(mockServicesDAO.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId)).thenReturn(null);

        DCAEControllerClient dcc = mock(DCAEControllerClient.class);
        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dcc, dbcc);
        UriInfo uriInfo = new Util.FakeUriInfo();

        try {
            api.dcaeServicesServiceIdDelete(serviceId, null);
            fail("Should have thrown a NotFoundException");
        } catch(NotFoundException e) {
            LOG.info("NotFoundException successfully thrown");
        } catch(Exception e) {
            LOG.error("Unexpected exception", e);
            fail("Unexpected exception");
        }
    }

}
