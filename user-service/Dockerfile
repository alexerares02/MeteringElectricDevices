# Use Maven with Java 21 for the build stage
FROM maven:3.8.8-eclipse-temurin-21 AS builder

COPY ./src/ /root/src
COPY ./pom.xml /root/
COPY ./checkstyle.xml /root/
WORKDIR /root
RUN mvn clean package -DskipTests

# Use a compatible OpenJDK 21 image for running the application
FROM openjdk:21

ENV TZ=UTC

COPY --from=builder /root/target/user-service-3.3.4.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
