#
# Build stage image
#
FROM maven:3-openjdk-16-slim AS mvn-build
COPY . .
RUN mvn package

#
# Final image
#
FROM openjdk:15-jdk-alpine

COPY --from=mvn-build /task-manager/target/TextCaptchaTaskManager.jar /app.jar

EXPOSE 8020

CMD java -jar /app.jar