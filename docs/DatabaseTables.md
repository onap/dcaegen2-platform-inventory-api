# Database Tables

As of version 0.12.0.

## `dcae_service_types`

### Relationships

* 1:1 - `dcae_services`

### Schema

```
              Table "public.dcae_service_types"
       Column       |            Type             | Modifiers 
--------------------+-----------------------------+-----------
 type_id            | character varying           | not null
 type_version       | integer                     | not null
 type_name          | character varying           | not null
 owner              | character varying           | not null
 blueprint_template | text                        | not null
 vnf_types          | character varying[]         | not null
 service_ids        | character varying[]         | 
 service_locations  | character varying[]         | 
 asdc_service_id    | character varying           | not null
 asdc_resource_id   | character varying           | not null
 created            | timestamp without time zone | not null
 is_active          | boolean                     | not null
Indexes:
    "pk_type_created" PRIMARY KEY, btree (type_id, created)
```

## `dcae_services`

### Relationships

* 1:1 - `dcae_service_types`
* N:N - `dcae_service_components` mapped through `dcae_services_components_maps`

### Schema

```
               Table "public.dcae_services"
     Column     |            Type             | Modifiers 
----------------+-----------------------------+-----------
 service_id     | character varying           | not null
 type_id        | character varying           | not null
 vnf_id         | character varying           | not null
 vnf_type       | character varying           | not null
 vnf_location   | character varying           | not null
 deployment_ref | character varying           | 
 created        | timestamp without time zone | not null
 modified       | timestamp without time zone | not null
 status         | character varying           | not null
Indexes:
    "dcae_services_pkey" PRIMARY KEY, btree (service_id)
Referenced by:
    TABLE "dcae_services_components_maps" CONSTRAINT "dcae_services_components_maps_service_id_fkey" FOREIGN KEY (service_id) REFERENCES dcae_services(service_id)
```

## `dcae_service_components`

### Relationships

* N:N - `dcae_services` mapped through `dcae_services_components_maps`

### Schema

```
           Table "public.dcae_service_components"
      Column      |            Type             | Modifiers 
------------------+-----------------------------+-----------
 component_id     | character varying           | not null
 component_type   | character varying           | not null
 component_source | character varying           | not null
 shareable        | integer                     | default 0
 created          | timestamp without time zone | not null
 modified         | timestamp without time zone | not null
Indexes:
    "dcae_service_components_pkey" PRIMARY KEY, btree (component_id)
Referenced by:
    TABLE "dcae_services_components_maps" CONSTRAINT "dcae_services_components_maps_component_id_fkey" FOREIGN KEY (component_id) REFERENCES dcae_service_components(component_id)
```

## `dcae_services_components_maps`

### Schema

```
      Table "public.dcae_services_components_maps"
    Column    |            Type             | Modifiers 
--------------+-----------------------------+-----------
 service_id   | character varying           | not null
 component_id | character varying           | not null
 created      | timestamp without time zone | not null
Indexes:
    "dcae_services_components_maps_pkey" PRIMARY KEY, btree (service_id, component_id)
Foreign-key constraints:
    "dcae_services_components_maps_component_id_fkey" FOREIGN KEY (component_id) REFERENCES dcae_service_components(component_id)
    "dcae_services_components_maps_service_id_fkey" FOREIGN KEY (service_id) REFERENCES dcae_services(service_id)
```
