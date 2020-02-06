/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2017-2020 AT&T Intellectual Property. All rights reserved.
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
import io.swagger.api.HealthCheckApi;
import io.swagger.api.ServiceHealthCheckApi;
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
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.Validator;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Link;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Created by mhwang on 4/11/16.
 */
public class InventoryApplication extends Application<InventoryConfiguration> {

    static final Logger metricsLogger = LoggerFactory.getLogger("metricsLogger");
	static final Logger debugLogger = LoggerFactory.getLogger("debugLogger");
	private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    static boolean shouldRemoteFetchConfig = false;
    static final String configFile = "/opt/config_active.json";

    /**
     * Parses user's args and makes adjustments if necessary
     *
     * NOTE: This function adjusts global state of InventoryApplication - shouldRemoteFetchConfig
     *
     * @param userArgs
     * @return Adjusted user's args or just the user's args untouched either way a String[]
     */
    public static String[] processArgs(String[] userArgs) {
        if (userArgs.length < 2 && "server".equals(userArgs[0])) {
            // When the start command is just "server", this will trigger inventory to look for its configuration
            // from Consul's KV store.  The url is hardcoded here which should be used as the "path" variable into
            // the UrlConfigurationSourceProvider.
            String[] customArgs = new String[userArgs.length+1];
            System.arraycopy(userArgs, 0, customArgs, 0, userArgs.length);
            customArgs[userArgs.length] = "http://consul:8500/v1/kv/inventory?raw=true";
            shouldRemoteFetchConfig = true;

            return customArgs;
        } else {
            // You are here because you want to use the default way of configuring inventory - YAML file.
        	// The config file yaml file however has the path to the file that has the cert jks password in the keyStorePassword filed
        	// Update config file's keyStorePassword to have actual password instead of path to the password file
        	debugLogger.debug(String.format("Default configuration file received: %s", userArgs[1]));
        	createConfigFileFromDefault(userArgs[1]);
        	userArgs[1] = configFile;
        	debugLogger.debug(String.format("Active config file that will be used: %s", userArgs[1]));
            return userArgs;
        }
    }

    public static void main(String[] args) throws Exception {
        metricsLogger.info("DCAE inventory application main Startup");
        // This is here to try to fix a "high" issue caught by Fortify. Did this **plus** setting locale for each of the
        // string comparisons that use `toUpper` because of this StackOverflow post:
        // http://stackoverflow.com/questions/38308777/fixed-fortify-scan-locale-changes-are-reappearing
        Locale.setDefault(Locale.ENGLISH);

        final String[] processedArgs = processArgs(args);
        new InventoryApplication().run(processedArgs);

        // revert to using logback.xml:
        LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
    	context.reset();
    	ContextInitializer initializer = new ContextInitializer(context);
    	initializer.autoConfig();
    	
    	metricsLogger.info("Starting DCAE inventory application...");
    	debugLogger.debug(String.format("Starting DCAE inventory application... args[0]: %s", args[0]));
    	
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
        Info info = new Info().title("DCAE Inventory API").version("0.8.1")
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
        debugLogger.info("Starting DCAE inventory application");
        debugLogger.info(String.format("DB driver properties: %s", configuration.getDataSourceFactory().getProperties().toString()));
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
            debugLogger.info("Use of DCAE controller client is required. Turned on.");
        } else {
            debugLogger.warn("Use of DCAE controller client is *not* required. Turned off.");
        }

        environment.jersey().register(NotFoundExceptionMapper.class);
        environment.jersey().register(DBIExceptionMapper.UnableToObtainConnectionExceptionMapper.class);
        environment.jersey().register(DBIExceptionMapper.UnableToExecuteStatementExceptionMapper.class);
        environment.jersey().register(DBIExceptionMapper.UnableToCreateStatementExceptionMapper.class);

        environment.jersey().register(new DcaeServicesApi());
        environment.jersey().register(new DcaeServiceTypesApi());
        environment.jersey().register(new DcaeServicesGroupbyApi());
        environment.jersey().register(new HealthCheckApi());
        environment.jersey().register(new ServiceHealthCheckApi());

        // https://github.com/swagger-api/swagger-core/wiki/Swagger-Core-Jersey-2.X-Project-Setup-1.5
        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(new SwaggerSerializers());
    }
    
    
    private static void createConfigFileFromDefault (String defaultConfigFile) {
    	
    	try {
			JSONObject dzConfig = new JSONObject ( new JSONTokener ( new FileInputStream ( new File ( defaultConfigFile ) ) ) );    
			JSONObject server = dzConfig.getJSONObject("server");
			JSONArray applicationConnectors = server.getJSONArray("applicationConnectors");
			String jksPasswdFile = applicationConnectors.getJSONObject(0).getString("keyStorePassword");
			if ( jksPasswdFile != null ) {
				applicationConnectors.getJSONObject(0).put("keyStorePassword", getFileContents(jksPasswdFile));
			}
			else {
				errorLogger.error(String.format("Exiting due to null value for JKS password file: %s", jksPasswdFile));
				System.exit(1);
			}			
			FileWriter fileWriter = new FileWriter(configFile);
			fileWriter.write(dzConfig.toString());
			fileWriter.flush();
			fileWriter.close();		
	    } catch (JSONException | FileNotFoundException e) {
	    	errorLogger.error(String.format("JSONException | FileNotFoundException while processing default config file: %s; execption: %s", 
	    			defaultConfigFile, e));
			System.exit(1);
		} catch ( Exception e ) {
			errorLogger.error(String.format("Exception while processing default config file: %s; execption: %s", 
					defaultConfigFile, e));
			System.exit(1);
		}
    }
    
    public static String getFileContents (String filename) {
		File f = new File(filename);
		try {
			byte[] bytes = Files.readAllBytes(f.toPath());
			return new String(bytes,"UTF-8").trim();
		} catch (FileNotFoundException e) {
			errorLogger.error(String.format("FileNotFoundException for filename: %s; execption: %s", filename, e));
			System.exit(1);
		} catch (IOException e) {
			errorLogger.error(String.format("IOException for filename: %s; execption: %s", filename, e));	
			System.exit(1);
		}
		return null;
	}

}
