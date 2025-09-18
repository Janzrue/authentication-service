# ===== Build =====
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY . .

# Saltamos validateStructure (plugin Bancolombia) y tests para acelerar
RUN chmod +x gradlew && ./gradlew clean :app-service:bootJar -x test -x validateStructure

# ===== Runtime =====
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV TZ=America/Bogota
RUN useradd -ms /bin/bash appuser
USER appuser

# JAR generado por el módulo app-service
COPY --from=build /app/applications/app-service/build/libs/*.jar /app/app.jar

EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=docker \
    JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java","-jar","/app/app.jar"]