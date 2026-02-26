FROM gradle:8.7-jdk17-alpine AS build
WORKDIR /app

# Copy project files
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the application
RUN gradle clean bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]