# integration-demo

Integration demo of the CAPTCHA system.

This service is bootstrapped with [Spring Boot](https://spring.io/).

## Building & running

### Build & run on host

Requires Java 11+ & Maven 3+ installed on host.

1. run `mvn package`
2. run `java -jar target/IntegrationDemo.jar --spring.profiles.active=dev`

### Build on host, run in Docker

Requires Java 11+, Maven 3+ and Docker installed on host.

1. run `mvn package` (this should generate `jar` files in `target` directory)
2. run `docker build --tag text-captcha-integration-demo .`
3. run the Docker image directly or as a part of Docker compose configuration

Note that Docker build runs with `docker` Spring profile (and uses `application-docker.yml` configuration).

### Build & run in Docker

Requires only Docker installed on host. Useful if host has no Java and/or Maven installed.

Note however that downloading dependencies during build may take a while since there is no local Maven cache.

1. run `docker build --tag text-captcha-integration-demo -f withBuild.Dockerfile .`
2. run the Docker image directly or as a part of Docker compose configuration

## Usage

Serves a webpage on designated port. Usage should be self-explanatory.

Check out yaml configuration files (i.e. `application-*.yml`) for a potential `context-path` setting.