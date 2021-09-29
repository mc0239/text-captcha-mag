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

COPY --from=mvn-build /result-processing/target/TextCaptchaResultProcessing.jar /app.jar

EXPOSE 8030

CMD java -jar /app.jar