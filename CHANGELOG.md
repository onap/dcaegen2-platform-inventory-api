# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) 
and this project adheres to [Semantic Versioning](http://semver.org/).

The version in the brackets represents the version of DCAE inventory and not the ONAP DCAE version.

## [3.2.0]

* Add non-root user in Docker image so that the inventory service can be run in non-privileged mode for security reasons DCAEGEN2-1554
* Change base image to alpine based DCAEGEN2-1565

## [3.0.1]

* Explicitly use 5.3.6.Final for hibernate-validator and 9.4.6 for jetty-util to address security issues

## [3.0.0]

* Remove the dcae controller code (housekeeping)
* Upgrade org.apache.httpcomponents/httpclient to 4.5.5

## [2.4.0]

ONAP version: 1.1.0

* Replace the ONAP 1.0.0 code base with the modified internal 1707 code base which includes support for versioning of dcae service types and asdc integration work.

## [1.0.0]

* DCAE service type resource's data model expanded to have the fields: `serviceIds` and `serviceLocations`
* Underlying Postgres table `dcae_service_types` schema changed to store the new fields
* `GET /dcae-service-types` query interface expanded have the query parameters: `serviceId` and `serviceLocation`.
