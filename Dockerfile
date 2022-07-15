FROM openjdk:8-jdk-alpine
WORKDIR /backend-technical-test-v2-master
COPY . .
RUN mvn clean install
CMD mvn spring-boot:run