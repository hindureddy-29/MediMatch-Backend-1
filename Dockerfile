# Build Stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/medimatch-backend-1.0.0.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=mysql
ENV DB_URL=jdbc:mysql://localhost:3306/medimatch_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV DB_USERNAME=root
ENV DB_PASSWORD=root
ENV JWT_SECRET=MediMatchSecretKeyForJwtTokenGeneration2026SecureKeyWithAtLeast256Bits

ENTRYPOINT ["java", "-jar", "app.jar"]
