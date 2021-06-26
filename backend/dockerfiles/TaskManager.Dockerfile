FROM openjdk:15-jdk-alpine

COPY /task-manager/target/TextCaptchaTaskManager.jar /app.jar

EXPOSE 8020

# CMD java -jar /app.jar --spring.profiles.active=docker
CMD java -jar /app.jar