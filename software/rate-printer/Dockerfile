FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
COPY currency-rate-provider/pom.xml currency-rate-provider/
COPY rate-printer/pom.xml rate-printer/
COPY rate-printer/src rate-printer/src

RUN mvn -q -pl rate-printer -am package -DskipTests

FROM eclipse-temurin:17-jre-jammy
COPY --from=build /workspace/rate-printer/target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
