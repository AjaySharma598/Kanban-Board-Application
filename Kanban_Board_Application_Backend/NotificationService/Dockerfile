FROM openjdk:11-jre-slim
WORKDIR usr/src
ADD ./target/NotificationService-0.0.1-SNAPSHOT.jar /usr/src/NotificationService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","NotificationService-0.0.1-SNAPSHOT.jar"]
