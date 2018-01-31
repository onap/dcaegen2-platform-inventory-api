# Upgrade Guide - DCAE Inventory
*Last update: 2016-08-19*

## 0.13.0 -> 1.0.0

Requires a database table alteration if you wish to reuse an existing database table generated by DCAE inventory v0.13.0.  The new schema is backwards compatible and safe for v0.13.0 records.

Run the following SQL commands for the target Postgres DCAE inventory database:

```
alter table dcae_service_types add column service_ids varchar[];
alter table dcae_service_types add column service_locations varchar[];
```