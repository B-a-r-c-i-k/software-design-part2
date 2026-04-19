FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
COPY currency-rate-provider/pom.xml currency-rate-provider/
COPY rate-printer/pom.xml rate-printer/
COPY currency-rate-provider/src currency-rate-provider/src

RUN mvn -q -pl currency-rate-provider -am package -DskipTests

FROM eclipse-temurin:17-jre-jammy
COPY --from=build /workspace/currency-rate-provider/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
