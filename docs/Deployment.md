# Deployment Guide - DCAE Inventory
*Last update: 2016-11-04*

## Requirements

### Network

Requires ports 8080 to be open for inbound traffic on the machine that the service is hosted on.

The DCAE Inventory relies on connectivity to a Postgres database, a DCAE Controller service instance, and a Databus Controller service instance all of which are configurable through the configuration file.  The DCAE Inventory makes client-side calls to these external services.

DCAE Inventory supports both HTTP and HTTPS through configuration setup.

### Configuration

The DCAE Inventory requires a configuration file and requires three groups of environment-dependent dynamic configuration information:

1. Postgres database connection details
    - `user`
    - `password`
    - `url`
2. DCAE controller connection details
    - `host`
    - `port`
    - `basePath`
    - `user`
    - `password`
3. DMaaP controller connection details
    - `host`

In order for HTTPS to be enabled for DCAE Inventory, an HTTPS `applicationConnectors` need to be added to the configuration file.  Details of this can be found [here](http://www.dropwizard.io/0.7.1/docs/manual/configuration.html#https).  What is required at deployment time is a valide key store file (e.g. `PKCS12`)  (i.e. set via `keyStorePath` parameter in the config) with the accompanying password (i.e. set via `keyStorePassword` parameter in the config).

## Run Command

The DCAE Inventory has been developed and tested using Java 8.  Here is a sample run command:

```
java -jar dcae-inventory-2.3.1.jar server /opt/config.yml
```
