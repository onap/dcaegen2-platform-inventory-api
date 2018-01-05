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

package io.swagger.api.factories;

import io.swagger.api.DcaeServicesGroupbyApiService;
import io.swagger.api.impl.DcaeServicesGroupbyApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-04-18T20:16:22.119Z")
public final class DcaeServicesGroupbyApiServiceFactory {

   private static final DcaeServicesGroupbyApiService service = new DcaeServicesGroupbyApiServiceImpl();

   //Utility classes, which are a collection of static members, are not meant to be instantiated.
   private DcaeServicesGroupbyApiServiceFactory(){
   }

   public static DcaeServicesGroupbyApiService getDcaeServicesGroupbyApi()
   {
      return service;
   }
}
