# Build stage
FROM maven:3.6.3-jdk-8-slim AS build
COPY src src
COPY pom.xml pom.xml
RUN mvn -f pom.xml clean test package

# Package stage
FROM openjdk:8-jdk-alpine
COPY --from=build target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
