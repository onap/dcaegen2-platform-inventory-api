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

package org.openecomp.dcae.inventory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javax.ws.rs.core.Link;
import java.io.IOException;

/**
 * Lifted from
 * http://stackoverflow.com/questions/26989004/how-to-serialize-declarative-links-jersey-with-jackson
 *
 * Created by mhwang on 5/10/16.
 */
public class LinkSerializer extends JsonSerializer<Link> {

    @Override
    public void serialize(Link link, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartObject();

        if (link.getTitle() != null) {
            jg.writeStringField("title", link.getTitle());
        }

        jg.writeStringField("rel", link.getRel());
        jg.writeStringField("href", link.getUri().toString());
        jg.writeEndObject();
    }

}
