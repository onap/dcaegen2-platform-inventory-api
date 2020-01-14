/*-
 * ============LICENSE_START=======================================================
 * dcae-inventory
 * ================================================================================
 * Copyright (C) 2020 Nokia Intellectual Property. All rights reserved.
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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class DcaeServiceTypesQueryStatementTest {

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypes() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create(null, true, true,
                null, null, null, null, null, null,
                null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastDeactivatedDcaeServiceTypes() {
        //when
        final String sql = DcaeServiceTypesQueryStatement.create(null, true, false,
                null, null, null, null, null,
                null, null, null);

        //then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where created < :createdCutoff order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForActiveDcaeServiceTypes() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create(null, false, true,
                null, null, null, null, null,
                null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types where created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForDeactivatedDcaeServiceTypes() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create(null, false, false,
                null, null, null, null, null,
                null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types where created < :createdCutoff order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithAsteriskTypeName() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("*", true, true,
                null, null, null, null, null, null,
                null, null);

        //  then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where type_name LIKE :typeName and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeName() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                null, null, null, null, null, null,
                null, null);

        // then
        assertThat(sql).isEqualTo("select * from dcae_service_types_latest where :typeName = type_name and created < :createdCutoff and deactivated is null order by created desc");
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfType() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", null, null, null, null, null,
                null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceId() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", null, null, null,
                null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocation() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", null,
                null, null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceIdSetToNone() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "none",
                null, null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and asdc_service_id is null and created < :createdCutoff and deactivated is null order by created desc"
        );
    }


    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceId() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "asdcServiceId",
                null, null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and :asdcServiceId = asdc_service_id and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceIdAndAsdcResourceIdSetToNone() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "none",
                "none", null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and asdc_service_id is null and asdc_resource_id is null and created < :createdCutoff and deactivated is null order by created desc"
        );
    }


    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceIdAndAsdcResourceId() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "asdcServiceId",
                "asdcResourceId", null, null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and :asdcServiceId = asdc_service_id and :asdcResourceId = asdc_resource_id and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceIdAndAsdcResourceIdAndOwner() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "asdcServiceId",
                "asdcResourceId", "owner", null, null);

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and :asdcServiceId = asdc_service_id and :asdcResourceId = asdc_resource_id and :owner = owner and created < :createdCutoff and deactivated is null order by created desc"
        );
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceIdAndAsdcResourceIdAndOwnerAndApplication() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "asdcServiceId",
                "asdcResourceId", "owner", "application", null);

        // then
        assertThat(sql).isEqualTo("select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and :asdcServiceId = asdc_service_id and :asdcResourceId = asdc_resource_id and :owner = owner and :application = application and created < :createdCutoff and deactivated is null order by created desc");
    }

    @Test
    public void shouldReturnSqlTemplateForLastActiveDcaeServiceTypesWithTypeNameAndVnfTypeAndServiceIdAndServiceLocationAndAsdcServiceIdAndAsdcResourceIdAndOwnerAndApplicationAndComponent() {
        // when
        final String sql = DcaeServiceTypesQueryStatement.create("typeName", true, true,
                "vnfType", "serviceId", "serviceLocation", "asdcServiceId",
                "asdcResourceId", "owner", "application", "component");

        // then
        assertThat(sql).isEqualTo(
                "select * from dcae_service_types_latest where :typeName = type_name and lower(:vnfType) = any(lower(vnf_types\\:\\:text)\\:\\:text[]) and (:serviceId = any(service_ids) or service_ids = '{}' or service_ids is null) and (:serviceLocation = any(service_locations) or service_locations = '{}' or service_locations is null) and :asdcServiceId = asdc_service_id and :asdcResourceId = asdc_resource_id and :owner = owner and :application = application and :component = component and created < :createdCutoff and deactivated is null order by created desc"
        );
    }
}
