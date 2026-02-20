# -------- BUILD STAGE --------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml first (dependency caching)
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# -------- RUN STAGE --------
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

# Copy whatever JAR Maven built
COPY --from=builder ./app/target/cycle-tracker-service-0.0.1-SNAPSHOT.jar ./app.jar


# Expose Spring Boot port
EXPOSE 4006

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
