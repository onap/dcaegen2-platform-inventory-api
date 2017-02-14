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

package org.openecomp.dcae.inventory.exceptions.mappers;

import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import io.swagger.api.ApiResponseMessage;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by mhwang on 3/6/17.
 *
 * Responsible for handling DBIExceptions for Inventory which are raised by database client calls
 * http://jdbi.org/apidocs/org/skife/jdbi/v2/exceptions/DBIException.html
 *
 * This class is intended to be extended to match on specific exceptions that are derived from DBIException
 */
public class DBIExceptionMapper<T extends DBIException> implements ExceptionMapper<T> {

    private final static Logger LOG = LoggerFactory.getLogger(DBIExceptionMapper.class);

    /**
     * Upon a DBIException, this handler will attempt to re-initialize the Inventory's database connection
     * and craft a specific message telling the client what to do.
     *
     * @param exception Derived class of DBIException
     * @return Returns a Response with a status code of 502 with an ApiResponseMessage object
     */
    @Override
    public Response toResponse(T exception) {
        LOG.error("", exception);
        StringBuilder clientMessage = new StringBuilder("There is a database issue.");

        try {
            InventoryDAOManager.getInstance().initialize();
            clientMessage.append(" Connection has been successfully reset. Please try again.");
        } catch(Exception e) {
            LOG.error(String.format("Failed to re-initialize database connection: %s", e.getMessage()));
            clientMessage.append(" Connection reset attempt has failed. Please try again soon.");
        }

        ApiResponseMessage response = new ApiResponseMessage(ApiResponseMessage.ERROR, clientMessage.toString());
        return Response.status(Response.Status.BAD_GATEWAY).entity(response).build();
    }

    // Here are the handlers for specific derived DBIException

    public final static class UnableToObtainConnectionExceptionMapper extends DBIExceptionMapper<UnableToObtainConnectionException> {
    }

    public final static class UnableToCreateStatementExceptionMapper extends DBIExceptionMapper<UnableToCreateStatementException> {
    }

    public final static class UnableToExecuteStatementExceptionMapper extends DBIExceptionMapper<UnableToExecuteStatementException> {
    }

}
