/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.dcae.inventory.daos.InventoryDataAccessManager;
import org.skife.jdbi.v2.Handle;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RunningDcaeServicesProviderTest {

    @Mock
    private InventoryDataAccessManager inventoryDataAccessManager;
    @Mock
    private Handle handle;

    private DcaeServicesGroupByApiServiceImpl.RunningDcaeServicesProvider runningDcaeServicesProvider;


    @Before
    public void setUp() {
        runningDcaeServicesProvider = Mockito.spy(new DcaeServicesGroupByApiServiceImpl.RunningDcaeServicesProvider(inventoryDataAccessManager));
    }

    @Test
    public void shouldInvokeQueryForRunningDcaeServices() {
        // given
        when(inventoryDataAccessManager.getHandle()).thenReturn(handle);
        Mockito.doReturn(new ArrayList<>()).when(runningDcaeServicesProvider).executeQuery(any(), any());

        // when
        runningDcaeServicesProvider.fetch("testColumnName");

        // then
        verify(runningDcaeServicesProvider, times(1)).executeQuery(any(), any());

    }
}
