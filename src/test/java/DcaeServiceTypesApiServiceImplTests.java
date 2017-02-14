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

import org.openecomp.dcae.inventory.daos.DCAEServiceTypesDAO;
import org.openecomp.dcae.inventory.daos.DCAEServicesDAO;
import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceObject;
import org.openecomp.dcae.inventory.dbthings.models.DCAEServiceTypeObject;
import io.swagger.api.impl.DcaeServiceTypesApiServiceImpl;
import io.swagger.model.DCAEServiceType;
import io.swagger.model.DCAEServiceTypeRequest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;


/**
 * Created by mhwang on 10/27/16.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class DcaeServiceTypesApiServiceImplTests {

    private final static Logger LOG = LoggerFactory.getLogger(DcaeServiceTypesApiServiceImplTests.class);

    private final static String URL_PATH = "http://testing-is-good.com";

    private DcaeServiceTypesApiServiceImpl api = null;
    private UriInfo uriInfo = null;
    private DCAEServiceTypesDAO mockTypesDAO = null;
    private DCAEServicesDAO mockServicesDAO = null;

    @Before
    public void setupClass() {
        api = new DcaeServiceTypesApiServiceImpl();

        uriInfo = mock(UriInfo.class);
        mockTypesDAO = mock(DCAEServiceTypesDAO.class);
        mockServicesDAO = mock(DCAEServicesDAO.class);

        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDAOManager = mock(InventoryDAOManager.class);

        when(InventoryDAOManager.getInstance()).thenReturn(mockDAOManager);
        when(mockDAOManager.getDCAEServiceTypesDAO()).thenReturn(mockTypesDAO);
        when(mockDAOManager.getDCAEServicesDAO()).thenReturn(mockServicesDAO);

        when(uriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromPath(URL_PATH));
    }

    private static boolean matchTypeVsTypeObject(DCAEServiceType serviceType, DCAEServiceTypeObject serviceTypeObject, String prefixPath) {
        return Objects.equals(serviceType.getTypeId(), serviceTypeObject.getTypeId())
                && Objects.equals(serviceType.getTypeName(), serviceTypeObject.getTypeName())
                && Objects.equals(serviceType.getTypeVersion(), serviceTypeObject.getTypeVersion())
                && Objects.equals(serviceType.getOwner(), serviceTypeObject.getOwner())
                && Objects.equals(serviceType.getBlueprintTemplate(), serviceTypeObject.getBlueprintTemplate())
                && Objects.equals(serviceType.getCreated(), serviceTypeObject.getCreated().toDate())
                && Objects.equals(serviceType.getVnfTypes(), serviceTypeObject.getVnfTypes())
                && Objects.equals(serviceType.getServiceIds(), serviceTypeObject.getServiceIds())
                && Objects.equals(serviceType.getServiceLocations(), serviceTypeObject.getServiceLocations())
                && Objects.equals(serviceType.getAsdcResourceId(), serviceTypeObject.getAsdcResourceId())
                && Objects.equals(serviceType.getAsdcServiceId(), serviceTypeObject.getAsdcServiceId())
                && serviceType.getSelfLink().getUri().toString().contains(prefixPath)
                && serviceType.getSelfLink().getUri().toString().contains(serviceTypeObject.getTypeId());
    }

    @Test
    public void testGetSuccess() {
        DCAEServiceTypeObject minimalFixture = new DCAEServiceTypeObject();
        minimalFixture.setTypeId("abc:1");
        minimalFixture.setTypeName("abc");
        minimalFixture.setTypeVersion(1);
        minimalFixture.setOwner("tester");
        minimalFixture.setBlueprintTemplate("{ blueprint template goes here }");
        minimalFixture.setCreated(DateTime.parse("2016-10-28T00:00"));

        DCAEServiceTypeObject fullFixture = new DCAEServiceTypeObject();
        fullFixture.setTypeId("def:1");
        fullFixture.setTypeName("def");
        fullFixture.setTypeVersion(1);
        fullFixture.setOwner("tester");
        fullFixture.setBlueprintTemplate("{ blueprint template goes here }");
        fullFixture.setCreated(DateTime.parse("2016-10-28T00:00"));
        fullFixture.setAsdcServiceId("4bb4e740-3920-442d-9ed3-89f15bdbff8a");
        fullFixture.setAsdcResourceId("3ea9dfae-a00d-4da8-8c87-02a34de8fc02");
        fullFixture.setVnfTypes(Arrays.asList(new String[] { "vnf-marble", "vnf-granite" }));
        fullFixture.setServiceIds(Arrays.asList(new String[] { "service-alpha", "service-bravo" }));
        fullFixture.setServiceLocations(Arrays.asList(new String[] { "New York", "Washington" }));

        for (DCAEServiceTypeObject fixture : new DCAEServiceTypeObject[] {minimalFixture, fullFixture}) {
            String someTypeId = fixture.getTypeId();
            when(mockTypesDAO.getByTypeId(someTypeId)).thenReturn(fixture);

            try {
                Response response = api.dcaeServiceTypesTypeIdGet(someTypeId, uriInfo, null);
                DCAEServiceType serviceType = (DCAEServiceType) response.getEntity();
                assertTrue("GET - 200 test case failed", matchTypeVsTypeObject(serviceType, fixture, URL_PATH));
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception: get 200", e);
            }
        }
    }

    @Test
    public void testGetNotFound() {
        String someTypeId = "abc:1";
        when(mockTypesDAO.getByTypeId(someTypeId)).thenReturn(null);

        try {
            Response response = api.dcaeServiceTypesTypeIdGet(someTypeId, uriInfo, null);
            assertEquals("GET - 404 test case failed", 404, response.getStatus());
        } catch(Exception e) {
            throw new RuntimeException("Unexpected exception: get 404", e);
        }
    }

    // TODO: Update this to check type id again. Must mock dao calls deeper.
    private static boolean matchTypeVsTypeRequest(DCAEServiceType serviceType, DCAEServiceTypeRequest serviceTypeRequest, String prefixPath) {
        return Objects.equals(serviceType.getTypeName(), serviceTypeRequest.getTypeName())
                && Objects.equals(serviceType.getTypeVersion(), serviceTypeRequest.getTypeVersion())
                && Objects.equals(serviceType.getOwner(), serviceTypeRequest.getOwner())
                && Objects.equals(serviceType.getBlueprintTemplate(), serviceTypeRequest.getBlueprintTemplate())
                && serviceType.getCreated() != null
                && Objects.equals(serviceType.getVnfTypes(), serviceTypeRequest.getVnfTypes())
                && Objects.equals(serviceType.getServiceIds(), serviceTypeRequest.getServiceIds())
                && Objects.equals(serviceType.getServiceLocations(), serviceTypeRequest.getServiceLocations())
                && Objects.equals(serviceType.getAsdcResourceId(), serviceTypeRequest.getAsdcResourceId())
                && Objects.equals(serviceType.getAsdcServiceId(), serviceTypeRequest.getAsdcServiceId())
                && serviceType.getSelfLink().getUri().toString().contains(prefixPath);
    }

    // TODO: Need to add tests for repeated POSTs == updates.
    @Test
    public void testPost() {
        DCAEServiceTypeRequest minimalFixture = new DCAEServiceTypeRequest();
        minimalFixture.setTypeName("abc");
        minimalFixture.setTypeVersion(1);
        minimalFixture.setOwner("tester");
        minimalFixture.setBlueprintTemplate("{ blueprint template goes here }");

        DCAEServiceTypeRequest fullFixture = new DCAEServiceTypeRequest();
        fullFixture.setTypeName("def");
        fullFixture.setTypeVersion(1);
        fullFixture.setOwner("tester");
        fullFixture.setBlueprintTemplate("{ blueprint template goes here }");
        fullFixture.setAsdcServiceId("4bb4e740-3920-442d-9ed3-89f15bdbff8a");
        fullFixture.setAsdcResourceId("3ea9dfae-a00d-4da8-8c87-02a34de8fc02");
        fullFixture.setVnfTypes(Arrays.asList(new String[] { "vnf-marble", "vnf-granite" }));
        fullFixture.setServiceIds(Arrays.asList(new String[] { "service-alpha", "service-bravo" }));
        fullFixture.setServiceLocations(Arrays.asList(new String[] { "New York", "Washington" }));

        for (DCAEServiceTypeRequest fixture : new DCAEServiceTypeRequest[] {minimalFixture, fullFixture}) {
            try {
                Response response = api.dcaeServiceTypesTypeIdPost(fixture, uriInfo, null);
                DCAEServiceType serviceType = (DCAEServiceType) response.getEntity();
                assertTrue("POST - 200 test case failed", matchTypeVsTypeRequest(serviceType, fixture, URL_PATH));
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception: post new 200", e);
            }
        }
    }

    @Test
    public void testPostConflict() {
        DCAEServiceTypeRequest minimalFixture = new DCAEServiceTypeRequest();
        minimalFixture.setTypeName("abc");
        minimalFixture.setTypeVersion(1);
        minimalFixture.setOwner("tester");
        minimalFixture.setBlueprintTemplate("{ blueprint template goes here }");

        String expectedTypeId = String.format("%s:%s", minimalFixture.getTypeName(), minimalFixture.getTypeVersion());

        when(mockTypesDAO.getByTypeId(expectedTypeId)).thenReturn(new DCAEServiceTypeObject());
        when(mockServicesDAO.countByType(DCAEServiceObject.DCAEServiceStatus.RUNNING, expectedTypeId)).thenReturn(10);

        try {
            Response response = api.dcaeServiceTypesTypeIdPost(minimalFixture, uriInfo, null);
            assertEquals("POST - 401 test case failed", 409, response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception: post new 200", e);
        }
    }

}
