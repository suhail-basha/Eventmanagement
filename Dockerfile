FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /target/EventManageProject-0.0.1-SNAPSHOT.jar /app/EventManageProject-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java" ,"-jar", "EventManageProject-0.0.1-SNAPSHOT.jar"]