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

import io.swagger.api.FakeUriInfoTestDataFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriInfo;

import static org.assertj.core.api.Assertions.assertThat;

public class DcaeServicesLinkResolverTest {
    private DcaeServicesGroupByApiServiceImpl.DcaeServicesLinkResolver dcaeServicesLinkResolver;
    private UriInfo uriInfo;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        dcaeServicesLinkResolver = new DcaeServicesGroupByApiServiceImpl.DcaeServicesLinkResolver();
        uriInfo = FakeUriInfoTestDataFactory.givenFakeUriInfo();
    }

    @Test
    public void shouldReturnDcaeServiceLinkForTypeParameter(){
        // when
        final Link link = dcaeServicesLinkResolver.resolveLink(
                DcaeServicesGroupByApiServiceImpl.PROPERTY_NAME_TYPE,
                uriInfo,
                "test"
        );

        // then
        assertThat(link.getRel()).isEqualTo("dcae_services");
        assertThat(link.getUri().toString()).isEqualTo("http://some-fake-base-uri/dcae-services/?typeId=test");
    }

    @Test
    public void shouldReturnDcaeServiceLinkForVnfLocationParameter(){
        // when
        final Link link = dcaeServicesLinkResolver.resolveLink(
                DcaeServicesGroupByApiServiceImpl.PROPERTY_NAME_VNF_LOCATION,
                uriInfo,
                "test"
        );

        // then
        assertThat(link.getRel()).isEqualTo("dcae_services");
        assertThat(link.getUri().toString()).isEqualTo("http://some-fake-base-uri/dcae-services/?vnfLocation=test");
    }

    @Test
    public void shouldReturnDcaeServiceLinkForVnfTypeParameter(){
        // when
        final Link link = dcaeServicesLinkResolver.resolveLink(
                DcaeServicesGroupByApiServiceImpl.PROPERTY_NAME_VNF_TYPE,
                uriInfo,
                "test"
        );

        // then
        assertThat(link.getRel()).isEqualTo("dcae_services");
        assertThat(link.getUri().toString()).isEqualTo("http://some-fake-base-uri/dcae-services/?vnfType=test");
    }


    @Test
    public void shouldReportAnErrorForUnknownParameter(){
        // arrange
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Unsupported 'unknown' property name!");

        // when
        dcaeServicesLinkResolver.resolveLink("unknown", uriInfo, "test");

    }
}
