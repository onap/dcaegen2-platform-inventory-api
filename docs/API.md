# DCAE Inventory API


<a name="overview"></a>
## Overview
DCAE Inventory is a web service that provides the following:

1. Real-time data on all DCAE services and their components
2. Comprehensive details on available DCAE service types


### Version information
*Version* : 2.1.0


### Contact information
*Contact Email* : dcae@lists.openecomp.org




<a name="paths"></a>
## Paths

<a name="dcaeservicetypestypenameput"></a>
### POST /dcae-service-types

#### Description
Inserts a new `DCAEServiceType` or updates an existing instance. Updates are only allowed iff there are no running DCAE services of the requested type,


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Body**|**body**  <br>*required*||[DCAEServiceTypeRequest](#dcaeservicetyperequest)||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Single `DCAEServiceType` object.|[DCAEServiceType](#dcaeservicetype)|
|**400**|Bad request provided.|[ApiResponseMessage](#apiresponsemessage)|
|**409**|Failed to update because there are still DCAE services of the requested type running.|[ApiResponseMessage](#apiresponsemessage)|


#### Consumes

* `application/json`


#### Produces

* `application/json`


<a name="dcaeservicetypesget"></a>
### GET /dcae-service-types

#### Description
Get a list of `DCAEServiceType` objects.


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**asdcResourceId**  <br>*optional*|Filter by associated asdc design resource id. Setting this to `NONE` will return instances that have asdc resource id set to null|string||
|**Query**|**asdcServiceId**  <br>*optional*|Filter by associated asdc design service id. Setting this to `NONE` will return instances that have asdc service id set to null|string||
|**Query**|**offset**  <br>*optional*|Query resultset offset used for pagination (zero-based)|integer(int32)||
|**Query**|**onlyActive**  <br>*optional*|If set to true, query returns only *active* DCAE service types. If set to false, then all DCAE service types are returned. Default is true|boolean|`"true"`|
|**Query**|**onlyLatest**  <br>*optional*|If set to true, query returns just the latest versions of DCAE service types. If set to false, then all versions are returned. Default is true|boolean|`"true"`|
|**Query**|**serviceId**  <br>*optional*|Filter by assocaited service id. Instances with service id null or empty is always returned.|string||
|**Query**|**serviceLocation**  <br>*optional*|Filter by associated service location. Instances with service location null or empty is always returned.|string||
|**Query**|**typeName**  <br>*optional*|Filter by service type name|string||
|**Query**|**vnfType**  <br>*optional*|Filter by associated vnf type. No wildcards, matches are explicit. This field is treated case insensitive.|string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|List of `DCAEServiceType` objects|[InlineResponse200](#inlineresponse200)|


#### Consumes

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


#### Produces

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


<a name="dcaeservicetypestypeidget"></a>
### GET /dcae-service-types/{typeId}

#### Description
Get a `DCAEServiceType` object.


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**typeId**  <br>*required*||string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Single `DCAEServiceType` object|[DCAEServiceType](#dcaeservicetype)|
|**404**|Resource not found|[DCAEServiceType](#dcaeservicetype)|


#### Consumes

* `application/json`


#### Produces

* `application/json`


<a name="dcaeservicetypestypeiddelete"></a>
### DELETE /dcae-service-types/{typeId}

#### Description
Deactivates existing `DCAEServiceType` instances


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**typeId**  <br>*required*||string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|`DCAEServiceType` has been deactivated|[ApiResponseMessage](#apiresponsemessage)|
|**404**|`DCAEServiceType` not found|[ApiResponseMessage](#apiresponsemessage)|
|**410**|`DCAEServiceType` already gone|[ApiResponseMessage](#apiresponsemessage)|


#### Consumes

* `application/vnd.dcae.inventory.v1+json`
* `application/json`


#### Produces

* `application/vnd.dcae.inventory.v1+json`
* `application/json`


<a name="dcaeservicesget"></a>
### GET /dcae-services

#### Description
Get a list of `DCAEService` objects.


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**componentType**  <br>*optional*|Use to filter by a specific DCAE service component type|string||
|**Query**|**created**  <br>*optional*|Use to filter by created time|string||
|**Query**|**offset**  <br>*optional*|Query resultset offset used for pagination (zero-based)|integer(int32)||
|**Query**|**shareable**  <br>*optional*|Use to filter by DCAE services that have shareable components or not|boolean||
|**Query**|**typeId**  <br>*optional*|DCAE service type name|string||
|**Query**|**vnfId**  <br>*optional*||string||
|**Query**|**vnfLocation**  <br>*optional*||string||
|**Query**|**vnfType**  <br>*optional*|Filter by associated vnf type. This field is treated case insensitive.|string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|List of `DCAEService` objects|[InlineResponse2001](#inlineresponse2001)|
|**502**|Bad response from DCAE controller|[ApiResponseMessage](#apiresponsemessage)|
|**504**|Failed to connect with DCAE controller|[ApiResponseMessage](#apiresponsemessage)|


#### Consumes

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


#### Produces

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


<a name="dcaeservicesgroupbypropertynameget"></a>
### GET /dcae-services-groupby/{propertyName}

#### Description
Get a list of unique values for the given `propertyName`


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**propertyName**  <br>*required*|Property to find unique values. Restricted to `type`, `vnfType`, `vnfLocation`|string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|List of unique property values|[DCAEServiceGroupByResults](#dcaeservicegroupbyresults)|


#### Consumes

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


#### Produces

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


<a name="dcaeservicesserviceidget"></a>
### GET /dcae-services/{serviceId}

#### Description
Get a `DCAEService` object.


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**serviceId**  <br>*required*||string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Single `DCAEService` object|[DCAEService](#dcaeservice)|
|**404**|DCAE service not found|[ApiResponseMessage](#apiresponsemessage)|
|**502**|Bad response from DCAE controller|[ApiResponseMessage](#apiresponsemessage)|
|**504**|Failed to connect with DCAE controller|[ApiResponseMessage](#apiresponsemessage)|


#### Consumes

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


#### Produces

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


<a name="dcaeservicesserviceidput"></a>
### PUT /dcae-services/{serviceId}

#### Description
Put a new or update an existing `DCAEService` object.


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**serviceId**  <br>*required*||string||
|**Body**|**body**  <br>*required*||[DCAEServiceRequest](#dcaeservicerequest)||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Single `DCAEService` object|[DCAEService](#dcaeservice)|
|**422**|Bad request provided|[ApiResponseMessage](#apiresponsemessage)|


#### Consumes

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


#### Produces

* `application/json`
* `application/vnd.dcae.inventory.v1+json`


<a name="dcaeservicesserviceiddelete"></a>
### DELETE /dcae-services/{serviceId}

#### Description
Remove an existing `DCAEService` object.


#### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**serviceId**  <br>*required*||string||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|DCAE service has been removed|No Content|
|**404**|Unknown DCAE service|[ApiResponseMessage](#apiresponsemessage)|


#### Consumes

* `application/vnd.dcae.inventory.v1+json`
* `application/json`


#### Produces

* `application/json`
* `application/vnd.dcae.inventory.v1+json`




<a name="definitions"></a>
## Definitions

<a name="apiresponsemessage"></a>
### ApiResponseMessage

|Name|Description|Schema|
|---|---|---|
|**code**  <br>*optional*||integer(int32)|
|**message**  <br>*optional*||string|
|**type**  <br>*optional*||string|


<a name="dcaeservice"></a>
### DCAEService

|Name|Description|Schema|
|---|---|---|
|**components**  <br>*optional*||< [DCAEServiceComponent](#dcaeservicecomponent) > array|
|**created**  <br>*optional*||string(date-time)|
|**deploymentRef**  <br>*optional*|Reference to a Cloudify deployment|string|
|**modified**  <br>*optional*||string(date-time)|
|**selfLink**  <br>*optional*|Link.title is serviceId|[Link](#link)|
|**serviceId**  <br>*optional*||string|
|**typeLink**  <br>*optional*|Link.title is typeId|[Link](#link)|
|**vnfId**  <br>*optional*||string|
|**vnfLink**  <br>*optional*|Link.title is vnfId|[Link](#link)|
|**vnfLocation**  <br>*optional*|Location information of the associated VNF|string|
|**vnfType**  <br>*optional*||string|


<a name="dcaeservicecomponent"></a>
### DCAEServiceComponent

|Name|Description|Schema|
|---|---|---|
|**componentId**  <br>*required*|The id format is unique to the source|string|
|**componentLink**  <br>*required*|Link to the underlying resource of this component|[Link](#link)|
|**componentSource**  <br>*required*|Specifies the name of the underying source service that is responsible for this components|enum (DCAEController, DMaaPController)|
|**componentType**  <br>*required*||string|
|**created**  <br>*required*||string(date-time)|
|**location**  <br>*optional*|Location information of the component|string|
|**modified**  <br>*required*||string(date-time)|
|**shareable**  <br>*required*|Used to determine if this component can be shared amongst different DCAE services|integer(int32)|
|**status**  <br>*optional*||string|


<a name="dcaeservicecomponentrequest"></a>
### DCAEServiceComponentRequest

|Name|Description|Schema|
|---|---|---|
|**componentId**  <br>*required*|The id format is unique to the source|string|
|**componentSource**  <br>*required*|Specifies the name of the underying source service that is responsible for this components|enum (DCAEController, DMaaPController)|
|**componentType**  <br>*required*||string|
|**shareable**  <br>*required*|Used to determine if this component can be shared amongst different DCAE services|integer(int32)|


<a name="dcaeservicegroupbyresults"></a>
### DCAEServiceGroupByResults

|Name|Description|Schema|
|---|---|---|
|**propertyName**  <br>*optional*|Property name of DCAE service that the group by operation was performed on|string|
|**propertyValues**  <br>*optional*||< [DCAEServiceGroupByResultsPropertyValues](#dcaeservicegroupbyresultspropertyvalues) > array|


<a name="dcaeservicegroupbyresultspropertyvalues"></a>
### DCAEServiceGroupByResultsPropertyValues

|Name|Description|Schema|
|---|---|---|
|**count**  <br>*optional*||integer(int32)|
|**dcaeServiceQueryLink**  <br>*optional*|Link.title is the DCAE service property value. Following this link will provide a list of DCAE services that all have this property value.|[Link](#link)|
|**propertyValue**  <br>*optional*||string|


<a name="dcaeservicerequest"></a>
### DCAEServiceRequest

|Name|Description|Schema|
|---|---|---|
|**components**  <br>*required*|List of DCAE service components that this service is composed of|< [DCAEServiceComponentRequest](#dcaeservicecomponentrequest) > array|
|**deploymentRef**  <br>*optional*|Reference to a Cloudify deployment|string|
|**typeId**  <br>*required*|Id of the associated DCAE service type|string|
|**vnfId**  <br>*required*|Id of the associated VNF that this service is monitoring|string|
|**vnfLocation**  <br>*required*|Location identifier of the associated VNF that this service is monitoring|string|
|**vnfType**  <br>*required*|The type of the associated VNF that this service is monitoring|string|


<a name="dcaeservicetype"></a>
### DCAEServiceType

|Name|Description|Schema|
|---|---|---|
|**asdcResourceId**  <br>*optional*|Id of vf/vnf instance this DCAE service type is associated with. Value source is from ASDC's notification event's field `resourceInvariantUUID`.|string|
|**asdcServiceId**  <br>*optional*|Id of service this DCAE service type is associated with. Value source is from ASDC's notification event's field `serviceInvariantUUID`.|string|
|**asdcServiceURL**  <br>*optional*|URL to the ASDC service model|string|
|**blueprintTemplate**  <br>*required*|String representation of a Cloudify blueprint with unbound variables|string|
|**created**  <br>*required*|Created timestamp for this DCAE service type in epoch time|string(date-time)|
|**deactivated**  <br>*optional*|Deactivated timestamp for this DCAE service type in epoch time|string(date-time)|
|**owner**  <br>*required*||string|
|**selfLink**  <br>*required*|Link to self where the Link.title is typeName|[Link](#link)|
|**serviceIds**  <br>*optional*|List of service ids that are used to associate with DCAE service type. DCAE service types with this propery as null or empty means them apply for every service id.|< string > array|
|**serviceLocations**  <br>*optional*|List of service locations that are used to associate with DCAE service type. DCAE service types with this propery as null or empty means them apply for every service location.|< string > array|
|**typeId**  <br>*required*|Unique identifier for this DCAE service type|string|
|**typeName**  <br>*required*|Descriptive name for this DCAE service type|string|
|**typeVersion**  <br>*required*|Version number for this DCAE service type|integer(int32)|
|**vnfTypes**  <br>*optional*||< string > array|


<a name="dcaeservicetyperequest"></a>
### DCAEServiceTypeRequest

|Name|Description|Schema|
|---|---|---|
|**asdcResourceId**  <br>*optional*|Id of vf/vnf instance this DCAE service type is associated with. Value source is from ASDC's notification event's field `resourceInvariantUUID`.|string|
|**asdcServiceId**  <br>*optional*|Id of service this DCAE service type is associated with. Value source is from ASDC's notification event's field `serviceInvariantUUID`.|string|
|**asdcServiceURL**  <br>*optional*|URL to the ASDC service model|string|
|**blueprintTemplate**  <br>*required*|String representation of a Cloudify blueprint with unbound variables|string|
|**owner**  <br>*required*||string|
|**serviceIds**  <br>*optional*|List of service ids that are used to associate with DCAE service type. DCAE service types with this propery as null or empty means them apply for every service id.|< string > array|
|**serviceLocations**  <br>*optional*|List of service locations that are used to associate with DCAE service type. DCAE service types with this propery as null or empty means them apply for every service location.|< string > array|
|**typeName**  <br>*required*|Descriptive name for this DCAE service type|string|
|**typeVersion**  <br>*required*|Version number for this DCAE service type|integer(int32)|
|**vnfTypes**  <br>*optional*||< string > array|


<a name="inlineresponse200"></a>
### InlineResponse200

|Name|Description|Schema|
|---|---|---|
|**items**  <br>*optional*||< [DCAEServiceType](#dcaeservicetype) > array|
|**links**  <br>*optional*||[InlineResponse200Links](#inlineresponse200links)|
|**totalCount**  <br>*optional*||integer(int32)|


<a name="inlineresponse2001"></a>
### InlineResponse2001

|Name|Description|Schema|
|---|---|---|
|**items**  <br>*optional*||< [DCAEService](#dcaeservice) > array|
|**links**  <br>*optional*||[InlineResponse200Links](#inlineresponse200links)|
|**totalCount**  <br>*optional*||integer(int32)|


<a name="inlineresponse200links"></a>
### InlineResponse200Links
Pagination links


|Name|Description|Schema|
|---|---|---|
|**nextLink**  <br>*optional*||[Link](#link)|
|**previousLink**  <br>*optional*||[Link](#link)|


<a name="link"></a>
### Link

|Name|Description|Schema|
|---|---|---|
|**params**  <br>*optional*||< string, string > map|
|**rel**  <br>*optional*||string|
|**rels**  <br>*optional*||< string > array|
|**title**  <br>*optional*||string|
|**type**  <br>*optional*||string|
|**uri**  <br>*optional*||string|
|**uriBuilder**  <br>*optional*||[UriBuilder](#uribuilder)|


<a name="uribuilder"></a>
### UriBuilder
*Type* : object





