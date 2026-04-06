# Multi-stage build for an optimized, production-ready Docker image

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Gradle wrapper and configuration files first to cache dependencies
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# Copy the source code
COPY src/ src/

# Make the wrapper executable and build the application (skip tests for faster build)
RUN chmod +x gradlew && ./gradlew clean build -x test --no-daemon

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Payment Services Team"
LABEL description="Order Payment Lifecycle Management Service"
LABEL version="1.0.0"

WORKDIR /app

# Copy the generated JAR file from the builder stage
COPY --from=builder /build/build/libs/order-payment-lifecycle*.jar app.jar

# Create a non-root user and group for enhanced security
RUN addgroup -g 1000 paymentservice && \
    adduser -D -u 1000 -G paymentservice paymentservice && \
    chown -R paymentservice:paymentservice /app

# Switch to the non-root user
USER paymentservice

# Define a health check for orchestration systems (like Docker Compose / Kubernetes)
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose the application port
EXPOSE 8080

# Configure the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}", "-jar", "app.jar"]