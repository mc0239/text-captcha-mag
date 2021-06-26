# backend

Backend services of the CAPTCHA system.

|SERVICE_NAME|SERVICE_DIR|SERVICE_JAR|SERVICE_TAG|PORT|
|TaskManager|task-manager|TextCaptchaTaskManager|text-captcha-task-manager|8020|
|TextIngest|text-ingest|TextCaptchaTextIngest|text-captcha-text-ingest|8010|

Services are bootstrapped with [Spring Boot](https://spring.io/).

## Building & running

### Build & run on host

Requires Java 11+ & Maven 3+ installed on host.

1. run `mvn package` in project root
2. run `java -jar SERVICE_DIR/target/SERVICE_JAR.jar --spring.profiles.active=dev`

### Build on host, run in Docker

Requires Java 11+, Maven 3+ and Docker installed on host.

1. run `mvn package` in project root
2. run `docker build -f dockerfiles/SERVICE_NAME.Dockerfile --tag SERVICE_TAG .`
3. run the Docker image directly or as a part of Docker compose configuration

Note that Docker build runs with `docker` Spring profile (and uses `application-docker.yml` configuration).

### Build & run in Docker

Requires only Docker installed on host. Useful if host has no Java and/or Maven installed. 

Note however that downloading dependencies during build may take a while since there is no local Maven cache.

1. run `docker build -f dockerfiles/SERVICE_NAME.WithBuild.Dockerfile --tag SERVICE_TAG .`
2. run the Docker image directly or as a part of Docker compose configuration

## Usage

When service successfully starts, it exposes a Swagger UI on `{{SERVICE_URL}}/{{CONTEXT_PATH}}/swagger-ui.html` 

Check out yaml configuration files (i.e. `application-*.yml`) for a port and potential `context-path` settings.