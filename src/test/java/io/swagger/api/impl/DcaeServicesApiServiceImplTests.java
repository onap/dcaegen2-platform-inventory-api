package io.swagger.api.impl;/*-
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.dcae.inventory.clients.DatabusControllerClient;
import org.onap.dcae.inventory.daos.DCAEServiceComponentsDAO;
import org.onap.dcae.inventory.daos.DCAEServiceTypesDAO;
import org.onap.dcae.inventory.daos.DCAEServicesDAO;
import org.onap.dcae.inventory.daos.InventoryDAOManager;
import org.onap.dcae.inventory.dbthings.models.DCAEServiceComponentObject;
import org.onap.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.api.NotFoundException;
import io.swagger.api.FakeUriInfoTestDataFactory;
import io.swagger.model.DCAEService;
import io.swagger.model.DCAEServiceRequest;

/**
 * Created by mhwang on 9/25/17.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class DcaeServicesApiServiceImplTests {

    private static final Logger log = LoggerFactory.getLogger(DcaeServicesApiServiceImplTests.class);

    private DCAEServiceTypesDAO mockTypesDao = null;
    private DCAEServicesDAO mockServicesDao = null;
    private DCAEServiceComponentsDAO mockComponentsDao = null;

    @Before
    public void setupClass() {
        mockTypesDao = mock(DCAEServiceTypesDAO.class);
        mockServicesDao = mock(DCAEServicesDAO.class);
        mockComponentsDao = mock(DCAEServiceComponentsDAO.class);

        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDaoManager = mock(InventoryDAOManager.class);

        when(InventoryDAOManager.getInstance()).thenReturn(mockDaoManager);
        when(mockDaoManager.getDCAEServicesDAO()).thenReturn(mockServicesDao);
        when(mockDaoManager.getDCAEServiceComponentsDAO()).thenReturn(mockComponentsDao);
        when(mockDaoManager.getDCAEServiceTypesDAO()).thenReturn(mockTypesDao);
    }

    @Test
    public void testCreateDCAEService() {
        Method createDCAEService = null;

        // This block is a trick to make a private method accessible for testing
        try {
            createDCAEService = DcaeServicesApiServiceImpl.class.getDeclaredMethod("createDCAEService", 
		    DCAEServiceObject.class, Collection.class, UriInfo.class);
            createDCAEService.setAccessible(true);
        } catch (NoSuchMethodException e) {
            fail("Failed to do the reflection trick to test the private method: createDCAEService");
        }

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);

        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");
        String serviceId = "service-id-123";
        DCAEServiceObject serviceObject = new DCAEServiceObject(serviceId, serviceRequest);
        List<DCAEServiceComponentObject> components = new ArrayList<>();
        UriInfo uriInfo = FakeUriInfoTestDataFactory.givenFakeUriInfo();

        try {
            DCAEService service = (DCAEService) createDCAEService.invoke(api, serviceObject, components, uriInfo);
            assertEquals(service.getServiceId(), serviceObject.getServiceId());
        } catch (Exception e) {
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
        when(mockServicesDao.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId)).thenReturn(serviceObject);
        when(mockComponentsDao.getByServiceId(serviceId)).thenReturn(new ArrayList<>());

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);
        UriInfo uriInfo = FakeUriInfoTestDataFactory.givenFakeUriInfo();

        try {
            Response response = api.dcaeServicesServiceIdGet(serviceId, uriInfo, null);
            assertEquals(HttpStatus.OK_200, response.getStatus());
        } catch (NotFoundException e) {
            fail("Service should have been found");
        }
    }

    /*
    Commented this unit test because could not get past Nullpointer in the line trying to mock the explicit "bind" 
    function call. Mockito does not handle mocking overloaded functions well so it goes into the actual method where
    an member variable called foreman is null.
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

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);

        String typeId = "some-type-id";
        String vnfId = "some-vnf-id";
        String vnfType = "some-vnf-type";
        String vnfLocation = "some-vnf-location";
        String componentType = "some-component-type";
        Boolean shareable = Boolean.TRUE;
        DateTime created = null;
        Integer offset = 0;
        UriInfo uriInfo = new io.swagger.api.Util.FakeUriInfo();
        SecurityContext securityContext = null;

        when(mockQuery.list()).thenReturn(new ArrayList<DCAEServiceObject>());

        Response response = api.dcaeServicesGet(typeId, vnfId, vnfType, vnfLocation, componentType, shareable, created,
                offset, uriInfo, securityContext);
        assertEquals(response.getStatus(), 200);
    }
    */

    @Test
    public void testDcaeServicesServiceIdPutMissingType() {
        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");

        when(mockTypesDao.getByTypeIdActiveOnly(serviceRequest.getTypeId())).thenReturn(null);

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);
        UriInfo uriInfo = FakeUriInfoTestDataFactory.givenFakeUriInfo();

        String serviceId = "service-id-123";
        Response response = api.dcaeServicesServiceIdPut(serviceId, serviceRequest, uriInfo, null);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.getStatus());
    }

    @Test
    public void testDcaeServicesServiceIdDelete() {
        String serviceId = "service-id-123";
        DCAEServiceRequest serviceRequest = new DCAEServiceRequest();
        serviceRequest.setTypeId("type-id-abc");
        serviceRequest.setVnfId("vnf-id-def");
        DCAEServiceObject serviceObject = new DCAEServiceObject(serviceId, serviceRequest);
        when(mockServicesDao.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId)).thenReturn(serviceObject);

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);

        try {
            Response response = api.dcaeServicesServiceIdDelete(serviceId, null);
            assertEquals(HttpStatus.OK_200, response.getStatus());
        } catch (NotFoundException e) {
            fail("Should have NOT thrown a NotFoundException");
        } catch (Exception e) {
            log.error("Unexpected exception", e);
            fail("Unexpected exception");
        }
    }

    @Test
    public void testDcaeServicesServiceIdDeleteMissingService() {
        String serviceId = "service-id-123";
        when(mockServicesDao.getByServiceId(DCAEServiceObject.DCAEServiceStatus.RUNNING, serviceId)).thenReturn(null);

        DatabusControllerClient dbcc = mock(DatabusControllerClient.class);
        DcaeServicesApiServiceImpl api = new DcaeServicesApiServiceImpl(dbcc);

        try {
            api.dcaeServicesServiceIdDelete(serviceId, null);
            fail("Should have thrown a NotFoundException");
        } catch (NotFoundException e) {
            log.info("NotFoundException successfully thrown");
        } catch (Exception e) {
            log.error("Unexpected exception", e);
            fail("Unexpected exception");
        }
    }

}
