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

COPY --from=mvn-build /text-ingest/target/TextCaptchaTextIngest.jar /app.jar

EXPOSE 8010

CMD java -jar /app.jar