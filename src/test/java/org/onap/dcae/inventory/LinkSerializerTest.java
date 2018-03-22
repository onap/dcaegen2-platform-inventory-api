/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.dcae.inventory;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Created by mhwang on 3/25/18.
 */
public class LinkSerializerTest {

    @Test
    public void testSerialize() {
        JsonGenerator jg = mock(JsonGenerator.class);

        try {
            Link link = new Link() {
                @Override
                public URI getUri() {
                    try {
                        return new URI("http://localhost/some-title");
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                public UriBuilder getUriBuilder() {
                    return null;
                }

                @Override
                public String getRel() {
                    return "self";
                }

                @Override
                public List<String> getRels() {
                    return null;
                }

                @Override
                public String getTitle() {
                    return "some-title";
                }

                @Override
                public String getType() {
                    return null;
                }

                @Override
                public Map<String, String> getParams() {
                    return null;
                }

                @Override
                public String toString() {
                    return null;
                }
            };
            (new LinkSerializer()).serialize(link, jg, null);
        } catch (Exception e) {
            fail("Failed to serialze link");
        }
    }

}
