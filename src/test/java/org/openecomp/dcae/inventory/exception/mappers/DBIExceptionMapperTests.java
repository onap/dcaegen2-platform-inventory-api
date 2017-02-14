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

package org.openecomp.dcae.inventory.exception.mappers;

import org.openecomp.dcae.inventory.daos.InventoryDAOManager;
import org.openecomp.dcae.inventory.exceptions.mappers.DBIExceptionMapper;
import io.swagger.api.ApiResponseMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mhwang on 3/8/17.
 */
@PrepareForTest({InventoryDAOManager.class})
@RunWith(PowerMockRunner.class)
public class DBIExceptionMapperTests {

    @Test
    public void testReinitializeSuccess() {
        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDAOManager = mock(InventoryDAOManager.class);
        when(InventoryDAOManager.getInstance()).thenReturn(mockDAOManager);

        RuntimeException fakeException = new RuntimeException("Spoofing database failure");

        // Test UnableToObtainConnectionExceptionMapper

        DBIExceptionMapper.UnableToObtainConnectionExceptionMapper mapperOne = new DBIExceptionMapper.UnableToObtainConnectionExceptionMapper();
        Response responseOne = mapperOne.toResponse(new UnableToObtainConnectionException(fakeException));
        assert responseOne.getStatus() == 502;
        String messageOne = ((ApiResponseMessage) responseOne.getEntity()).getMessage();
        assert messageOne.contains("successfully reset");

        // Test UnableToCreateStatementExceptionMapper

        DBIExceptionMapper.UnableToCreateStatementExceptionMapper mapperTwo = new DBIExceptionMapper.UnableToCreateStatementExceptionMapper();
        Response responseTwo = mapperTwo.toResponse(new UnableToCreateStatementException(fakeException));
        assert responseTwo.getStatus() == 502;
        String messageTwo = ((ApiResponseMessage) responseTwo.getEntity()).getMessage();
        assert messageTwo.contains("successfully reset");

        // Test UnableToExecuteStatementExceptionMapper

        DBIExceptionMapper.UnableToExecuteStatementExceptionMapper mapperThree = new DBIExceptionMapper.UnableToExecuteStatementExceptionMapper();
        Response responseThree = mapperThree.toResponse(new UnableToExecuteStatementException(fakeException));
        assert responseThree.getStatus() == 502;
        String messageThree = ((ApiResponseMessage) responseThree.getEntity()).getMessage();
        assert messageThree.contains("successfully reset");
    }

    @Test
    public void testReinitializeFailed() {
        // PowerMockito does bytecode magic to mock static methods and use final classes
        PowerMockito.mockStatic(InventoryDAOManager.class);
        InventoryDAOManager mockDAOManager = mock(InventoryDAOManager.class);
        when(InventoryDAOManager.getInstance()).thenReturn(mockDAOManager);
        Mockito.doThrow(new RuntimeException("Spoof initialization failure")).when(mockDAOManager).initialize();

        RuntimeException fakeException = new RuntimeException("Spoofing database failure");

        // Test UnableToObtainConnectionExceptionMapper

        DBIExceptionMapper.UnableToObtainConnectionExceptionMapper mapperOne = new DBIExceptionMapper.UnableToObtainConnectionExceptionMapper();
        Response responseOne = mapperOne.toResponse(new UnableToObtainConnectionException(fakeException));
        assert responseOne.getStatus() == 502;
        String messageOne = ((ApiResponseMessage) responseOne.getEntity()).getMessage();
        assert !messageOne.contains("successfully reset");

        // Test UnableToCreateStatementExceptionMapper

        DBIExceptionMapper.UnableToCreateStatementExceptionMapper mapperTwo = new DBIExceptionMapper.UnableToCreateStatementExceptionMapper();
        Response responseTwo = mapperTwo.toResponse(new UnableToCreateStatementException(fakeException));
        assert responseTwo.getStatus() == 502;
        String messageTwo = ((ApiResponseMessage) responseTwo.getEntity()).getMessage();
        assert !messageTwo.contains("successfully reset");

        // Test UnableToExecuteStatementExceptionMapper

        DBIExceptionMapper.UnableToExecuteStatementExceptionMapper mapperThree = new DBIExceptionMapper.UnableToExecuteStatementExceptionMapper();
        Response responseThree = mapperThree.toResponse(new UnableToExecuteStatementException(fakeException));
        assert responseThree.getStatus() == 502;
        String messageThree = ((ApiResponseMessage) responseThree.getEntity()).getMessage();
        assert !messageThree.contains("successfully reset");
    }

}
