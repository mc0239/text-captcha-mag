FROM openjdk:15-jdk-alpine

COPY /result-processing/target/TextCaptchaResultProcessing.jar /app.jar

EXPOSE 8030

# CMD java -jar /app.jar --spring.profiles.active=docker
CMD java -jar /app.jar