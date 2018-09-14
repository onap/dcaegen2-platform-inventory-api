package io.swagger.api;/*-
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

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Created by mhwang on 9/25/17.
 */
public class Util {

    public static class FakeUriInfo implements UriInfo {

        @Override
        public String getPath() {
            return null;
        }

        @Override
        public String getPath(boolean bunused) {
            return null;
        }

        @Override
        public List<PathSegment> getPathSegments() {
            return null;
        }

        @Override
        public List<PathSegment> getPathSegments(boolean bunused) {
            return null;
        }

        @Override
        public URI getRequestUri() {
            return null;
        }

        @Override
        public UriBuilder getRequestUriBuilder() {
            return null;
        }

        @Override
        public URI getAbsolutePath() {
            return null;
        }

        @Override
        public UriBuilder getAbsolutePathBuilder() {
            return null;
        }

        @Override
        public URI getBaseUri() {
            return null;
        }

        @Override
        public UriBuilder getBaseUriBuilder() {
            return UriBuilder.fromUri("http://some-fake-base-uri");
        }

        @Override
        public MultivaluedMap<String, String> getPathParameters() {
            return null;
        }

        @Override
        public MultivaluedMap<String, String> getPathParameters(boolean bunused) {
            return null;
        }

        @Override
        public MultivaluedMap<String, String> getQueryParameters() {
            return null;
        }

        @Override
        public MultivaluedMap<String, String> getQueryParameters(boolean bunused) {
            return null;
        }

        @Override
        public List<String> getMatchedURIs() {
            return null;
        }

        @Override
        public List<String> getMatchedURIs(boolean bunused) {
            return null;
        }

        @Override
        public List<Object> getMatchedResources() {
            return null;
        }

        @Override
        public URI resolve(URI uri) {
            return null;
        }

        @Override
        public URI relativize(URI uri) {
            return null;
        }
    }
}
