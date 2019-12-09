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


import com.google.common.collect.Lists;
import io.swagger.model.DCAEServiceGroupByResults;
import io.swagger.model.DCAEServiceGroupByResultsPropertyValues;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DcaeServicesGroupByApiServiceImplTest {


    @Mock
    private DcaeServicesGroupByApiServiceImpl.RunningDcaeServicesProvider runningDcaeServicesProvider;
    @Mock
    private DcaeServicesGroupByApiServiceImpl.DcaeServicesLinkResolver dcaeServicesLinkResolver;
    @Mock
    private UriInfo uriInfo;
    @Mock
    private SecurityContext securityContext;
    private DcaeServicesGroupByApiServiceImpl dcaeServicesGroupbyApiService;

    @Before
    public void setUp() {
        this.dcaeServicesGroupbyApiService = new DcaeServicesGroupByApiServiceImpl(
                runningDcaeServicesProvider, dcaeServicesLinkResolver);
    }

    @Test
    public void shouldReportBadRequestResponseWhenColumnNameDoesNotExist() {
        final Response response = dcaeServicesGroupbyApiService.dcaeServicesGroupbyPropertyNameGet(
                "columnNameDoesNotExist",
                uriInfo,
                securityContext
        );

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldMapPropertyNameToProperColumnName() {
        assertThat(dcaeServicesGroupbyApiService.resolveColumnName("type")).isEqualTo("type_id");
        assertThat(dcaeServicesGroupbyApiService.resolveColumnName("vnfLocation")).isEqualTo("vnf_location");
        assertThat(dcaeServicesGroupbyApiService.resolveColumnName("vnfType")).isEqualTo("vnf_type");
        assertThat(dcaeServicesGroupbyApiService.resolveColumnName("unknownPropertyName")).isEqualTo("n/a");
    }

    @Test
    public void shouldCreateQueryTemplate() {
        assertThat(
                DcaeServicesGroupByApiServiceImpl.RunningDcaeServicesProvider.createQuery("type")
        ).isEqualTo("select type, count(1) as num  from dcae_services where status = :serviceStatus group by type order by count(1) desc");
    }

    @Test
    public void shouldReturnOkResponseWhenThereIsNoRunningDcaeServices(){
        final Response response = dcaeServicesGroupbyApiService.dcaeServicesGroupbyPropertyNameGet("type", uriInfo, securityContext);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
    @Test
    public void shouldReturnOkResponseWhenThereIsOneRunningDcaeService(){

        // given
        final Map<String, Object> dataMap = Maps.newHashMap("num", 1L);
        dataMap.put("type_id", "some_id_value");

        List<Map<String, Object>> data = Lists.newArrayList(dataMap);
        when(runningDcaeServicesProvider.fetch(eq("type_id"))).thenReturn(data);

        Link link = mock(Link.class);
        when(dcaeServicesLinkResolver.resolveLink(eq("type"),eq(uriInfo),eq("some_id_value"))).thenReturn(link);

        // when
        final Response response = dcaeServicesGroupbyApiService.dcaeServicesGroupbyPropertyNameGet("type", uriInfo, securityContext);

        // then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        final DCAEServiceGroupByResults entity = (DCAEServiceGroupByResults) response.getEntity();
        assertThatEntityResponseContainsInformationAboutRunningDcaeServices(entity);
    }

    void assertThatEntityResponseContainsInformationAboutRunningDcaeServices(DCAEServiceGroupByResults entity) {
        assertThat(entity.getPropertyName()).isEqualTo("type");
        assertThat(entity.getPropertyValues().size()).isEqualTo(1);
        final DCAEServiceGroupByResultsPropertyValues propertyValues = entity.getPropertyValues().get(0);
        assertThat(propertyValues.getCount()).isEqualTo(1);
        assertThat(propertyValues.getPropertyValue()).isEqualTo("some_id_value");
    }
}
