# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# On reçoit le nom du service via ARG (ex: user-service)
ARG SERVICE_NAME

# On copie le contenu du dossier spécifique du service vers /app
# Note : Dans docker-compose, le contexte est la racine '.', donc le chemin est services/nom
COPY services/${SERVICE_NAME}/ /app/

# On lance le build directement (le pom.xml est maintenant à la racine de /app)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Sécurité : utilisateur non-root [cite: 2]
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

# Récupération du JAR généré dans le dossier target du stage build
COPY --from=build /app/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring:spring

EXPOSE 8080

# Healthcheck générique [cite: 3]
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]