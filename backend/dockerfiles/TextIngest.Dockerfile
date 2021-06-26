FROM openjdk:15-jdk-alpine

COPY /text-ingest/target/TextCaptchaTextIngest.jar /app.jar

EXPOSE 8010

# CMD java -jar /app.jar --spring.profiles.active=docker
CMD java -jar /app.jar