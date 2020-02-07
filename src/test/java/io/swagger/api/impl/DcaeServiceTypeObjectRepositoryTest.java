/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2020 Nokia. All rights reserved.
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
package io.swagger.api.impl;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.dcae.inventory.daos.InventoryDataAccessManager;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DcaeServiceTypeObjectRepositoryTest {

    @Mock
    private InventoryDataAccessManager instance;
    @Mock
    private Handle handle;
    @Mock
    Query query;
    private DcaeServiceTypeObjectRepository dcaeServiceTypeObjectRepositorySpy;

    @Before
    public void setUp() {
        when(instance.getHandle()).thenReturn(handle);

        final DcaeServiceTypeObjectRepository dcaeServiceTypeObjectRepository = new DcaeServiceTypeObjectRepository(instance);
        this.dcaeServiceTypeObjectRepositorySpy = Mockito.spy(dcaeServiceTypeObjectRepository);
        Mockito.doReturn(query).when(dcaeServiceTypeObjectRepositorySpy).getQuery(eq(handle), anyString());
        Mockito.doNothing().when(dcaeServiceTypeObjectRepositorySpy).bindCreatedCutoff(any(DateTime.class), eq(query));
    }

    @Test
    public void shouldConfigureBindQueryForTypeNameParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                null, null, null,
                null, null, null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForVnfTypeParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", null, null,
                null, null, null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForServiceIdParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", null,
                null, null, null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForServiceLocationParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                null, null, null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForAsdcServiceIdParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "testAsdcServiceId", null, null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query).bind(eq("asdcServiceId"), eq("testAsdcServiceId"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForNoneAsdcServiceIdParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "NONE", null, null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query, never()).bind(eq("asdcServiceId"), anyString());
        verify(query).list();
    }


    @Test
    public void shouldConfigureBindQueryForAsdcResourceIdParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "testAsdcServiceId", "testAsdcResourceId", null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query).bind(eq("asdcServiceId"), eq("testAsdcServiceId"));
        verify(query).bind(eq("asdcResourceId"), eq("testAsdcResourceId"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForNoneAsdcResourceIdParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "NONE", "NONE", null,
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query, never()).bind(eq("asdcServiceId"), anyString());
        verify(query, never()).bind(eq("asdcResourceId"), anyString());
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForApplicationParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "testAsdcServiceId", "testAsdcResourceId", "testApplication",
                null, null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query).bind(eq("asdcServiceId"), eq("testAsdcServiceId"));
        verify(query).bind(eq("asdcResourceId"), eq("testAsdcResourceId"));
        verify(query).bind(eq("application"), eq("testApplication"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForComponentParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "testAsdcServiceId", "testAsdcResourceId", "testApplication",
                "testComponent", null
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query).bind(eq("asdcServiceId"), eq("testAsdcServiceId"));
        verify(query).bind(eq("asdcResourceId"), eq("testAsdcResourceId"));
        verify(query).bind(eq("component"), eq("testComponent"));
        verify(query).list();
    }

    @Test
    public void shouldConfigureBindQueryForOwnerParameter() {

        this.dcaeServiceTypeObjectRepositorySpy.fetch("testTypeName", false, false,
                "testVnfType", "testServiceId", "testServiceLocation",
                "testAsdcServiceId", "testAsdcResourceId", "testApplication",
                "testComponent", "testOwner"
        );

        verify(query).bind(eq("typeName"), eq("testTypeName"));
        verify(query).bind(eq("vnfType"), eq("testVnfType"));
        verify(query).bind(eq("serviceId"), eq("testServiceId"));
        verify(query).bind(eq("serviceLocation"), eq("testServiceLocation"));
        verify(query).bind(eq("asdcServiceId"), eq("testAsdcServiceId"));
        verify(query).bind(eq("asdcResourceId"), eq("testAsdcResourceId"));
        verify(query).bind(eq("component"), eq("testComponent"));
        verify(query).bind(eq("owner"), eq("testOwner"));
        verify(query).list();
    }
}
