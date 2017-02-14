# DCAE Inventory

DCAE Inventory is a web service that provides a REST-API to obtain DCAE service and DCAE service type information.  You can find the design documentation [here](docs/Design.md).

## Usage

### Build Docker image

The following Maven command will build the Uber JAR, build the Docker image to the local machine registry and then push the same image to the specified remote registry.

```
mvn clean package docker:build -DpushImageTag
```

The image will be tagged with the project version found in the `pom.xml`.

### Run via Docker

Execute this to run.

```
docker run -p 8080:8080 -d -v <some local directory>/config.yml:/opt/config.yml --name dcae-inventory <docker registry>/dcae-inventory:<version>
```

#### Running local Dockerized Postgres

The following is the docker command used to run a container of a postgres 9.5.2 image from the official repository:

```
 docker run --name dcae-postgres -e POSTGRES_PASSWORD=test123 -e PGDATA=/var/lib/postgresql/data/pgdata -v <local directory>:/var/lib/postgresql/data/pgdata -p 5432:5432 -d postgres:9.5.2
 ```

The central postgres instance for 1607 will be using 9.5.2.

## Swagger

To view the running service's swagger specification, execute the following:

```
curl <hostname>:8080/swagger.json
```

or

```
curl <hostname>:8080/swagger.yaml
```

To view it offline, an AsciiDoctor version is available [here](docs/API.adoc).
