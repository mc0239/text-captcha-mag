#
# Build stage image
#
FROM maven:3-openjdk-11-slim AS mvn-build
COPY . .
RUN mvn package

#
# Final image
#
FROM openjdk:15-jdk-alpine

COPY --from=mvn-build /target/CaptchaTaskManager.jar /app.jar

EXPOSE 8080

CMD java -jar /app.jar