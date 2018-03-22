/*-
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

package org.onap.dcae.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.JsonConfigurationFactory;
import io.dropwizard.configuration.UrlConfigurationSourceProvider;
import org.onap.dcae.inventory.clients.DatabusControllerClient;
import org.onap.dcae.inventory.exceptions.mappers.DBIExceptionMapper;
import org.onap.dcae.inventory.providers.NotFoundExceptionMapper;
import org.onap.dcae.inventory.daos.InventoryDAOManager;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.api.DcaeServiceTypesApi;
import io.swagger.api.DcaeServicesApi;
import io.swagger.api.DcaeServicesGroupbyApi;
import io.swagger.api.factories.DcaeServicesApiServiceFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.Validator;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Link;
import java.util.EnumSet;
import java.util.Locale;


/**
 * Created by mhwang on 4/11/16.
 */
public class InventoryApplication extends Application<InventoryConfiguration> {

    static final Logger LOG = LoggerFactory.getLogger(InventoryApplication.class);
    static boolean shouldRemoteFetchConfig = false;

    public static void main(String[] args) throws Exception {
        // This is here to try to fix a "high" issue caught by Fortify. Did this **plus** setting locale for each of the
        // string comparisons that use `toUpper` because of this StackOverflow post:
        // http://stackoverflow.com/questions/38308777/fixed-fortify-scan-locale-changes-are-reappearing
        Locale.setDefault(Locale.ENGLISH);

        if (args.length < 2 && "server".equals(args[0])) {
            // When the start command is just "server", this will trigger inventory to look for its configuration
            // from Consul's KV store.  The url is hardcoded here which should be used as the "path" variable into
            // the UrlConfigurationSourceProvider.
            String[] customArgs = new String[args.length+1];
            System.arraycopy(args, 0, customArgs, 0, args.length);
            customArgs[args.length] = "http://consul:8500/v1/kv/inventory?raw=true";
            shouldRemoteFetchConfig = true;

            new InventoryApplication().run(customArgs);
        } else {
            // You are here because you want to use the default way of configuring inventory - YAML file.
            new InventoryApplication().run(args);
        }
    }

    @Override
    public String getName() {
        return "dcae-inventory";
    }

    private static class JsonConfigurationFactoryFactory<T> implements ConfigurationFactoryFactory<T> {
        @Override
        public ConfigurationFactory<T> create(Class<T> klass, Validator validator, ObjectMapper objectMapper, String propertyPrefix) {
            return new JsonConfigurationFactory(klass, validator, objectMapper, propertyPrefix);
        }
    }

    @Override
    public void initialize(Bootstrap<InventoryConfiguration> bootstrap) {
        // This Info object was lifted from the Swagger generated io.swagger.api.Bootstrap file. Although it was not generated
        // correctly.
        Info info = new Info().title("DCAE Inventory API").version("0.8.0")
                .description("DCAE Inventory is a web service that provides the following:\n\n1. Real-time data on all DCAE services and their components\n2. Comprehensive details on available DCAE service types\n")
                .contact(new Contact().email("dcae@lists.onap.org"));
        // Swagger/servlet/jax-rs magic!
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setInfo(info);
        beanConfig.setResourcePackage("io.swagger.api");
        beanConfig.setScan(true);

        if (shouldRemoteFetchConfig) {
            // You are here because the configuration is sitting on a remote server in json format
            bootstrap.setConfigurationSourceProvider(new UrlConfigurationSourceProvider());
            bootstrap.setConfigurationFactoryFactory(new JsonConfigurationFactoryFactory<>());
        }
    }

    @Override
    public void run(InventoryConfiguration configuration, Environment environment) {
        LOG.info("Starting DCAE inventory application");
        LOG.info(String.format("DB driver properties: %s", configuration.getDataSourceFactory().getProperties().toString()));
        InventoryDAOManager.getInstance().setup(environment, configuration);
        InventoryDAOManager.getInstance().initialize();

        // Add filter for CORS support for DCAE dashboard
        // http://jitterted.com/tidbits/2014/09/12/cors-for-dropwizard-0-7-x/
        // https://gist.github.com/yunspace/07d80a9ac32901f1e149#file-dropwizardjettycrossoriginintegrationtest-java-L11
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");

        // Want to serialize Link in a way we like
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Link.class, new LinkSerializer());
        environment.getObjectMapper().registerModule(simpleModule);

        // Setup Databus controller client
        // Used by the dcae-services API
        if (configuration.getDatabusControllerConnection().getRequired()) {
            final Client clientDatabusController = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                    .build("DatabusControllerClient");
            clientDatabusController.register(HttpAuthenticationFeature.basicBuilder().credentials(
                    configuration.getDatabusControllerConnection().getMechId(),
                    configuration.getDatabusControllerConnection().getPassword()).build());
            final DatabusControllerClient databusControllerClient = new DatabusControllerClient(clientDatabusController,
                    configuration.getDatabusControllerConnection());
            DcaeServicesApiServiceFactory.setDatabusControllerClient(databusControllerClient);

            LOG.info("Use of databus controller client is required. Turned on.");
        } else {
            LOG.warn("Use of databus controller client is *not* required. Turned off.");
        }

        environment.jersey().register(NotFoundExceptionMapper.class);
        environment.jersey().register(DBIExceptionMapper.UnableToObtainConnectionExceptionMapper.class);
        environment.jersey().register(DBIExceptionMapper.UnableToExecuteStatementExceptionMapper.class);
        environment.jersey().register(DBIExceptionMapper.UnableToCreateStatementExceptionMapper.class);

        environment.jersey().register(new DcaeServicesApi());
        environment.jersey().register(new DcaeServiceTypesApi());
        environment.jersey().register(new DcaeServicesGroupbyApi());

        // https://github.com/swagger-api/swagger-core/wiki/Swagger-Core-Jersey-2.X-Project-Setup-1.5
        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(new SwaggerSerializers());
    }

}
