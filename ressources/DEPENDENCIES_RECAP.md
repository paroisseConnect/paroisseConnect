# Récapitulatif des Dépendances - ParoisseConnect

## Vue d'ensemble

Ce document liste toutes les dépendances Maven nécessaires pour chaque service de l'architecture ParoisseConnect.

**Versions utilisées:**
- Spring Boot: `3.5.10` ou `3.5.11-SNAPSHOT`
- Spring Cloud: `2025.0.1`
- Java: `21`
- PostgreSQL Driver: (version gérée par Spring Boot)
- Keycloak Admin Client: `24.0.0`

---

## 1. Discovery Service (Port 8761)

**Rôle:** Service de découverte Eureka Server

### Dépendances

```xml
<dependencies>
    <!-- Eureka Server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>

    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Dépendances de gestion

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 2. Config Service (Port 8888)

**Rôle:** Service de configuration centralisée (Spring Cloud Config Server)

### Dépendances

```xml
<dependencies>
    <!-- Spring Cloud Config Server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

    <!-- Eureka Client (pour s'enregistrer) -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- Actuator (pour les endpoints de santé) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Dépendances de gestion

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 3. Gateway Service (Port 8888)

**Rôle:** API Gateway avec routage et sécurité

### Dépendances

```xml
<dependencies>
    <!-- Actuator (monitoring) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- OAuth2 Resource Server (validation JWT) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>

    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Spring Cloud Gateway (WebFlux) -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway-server-webflux</artifactId>
    </dependency>

    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.projectreactor</groupId>
        <artifactId>reactor-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Dépendances de gestion

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 4. User Service (Port 8081)

**Rôle:** Gestion des utilisateurs, rôles, permissions et synchronisation Keycloak

### Dépendances communes (tous les microservices métier)

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- OAuth2 Resource Server -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>

    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- OpenFeign (communication inter-services) -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Dépendances spécifiques à User Service

```xml
<!-- Keycloak Admin Client (pour synchronisation) -->
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-admin-client</artifactId>
    <version>24.0.0</version>
</dependency>
```

### Dépendances de gestion

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 5. Parish Service (Port 8082)

**Rôle:** Gestion des unités ecclésiales et abonnements

### Dépendances

Utilise les **dépendances communes** listées dans la section User Service (sans Keycloak Admin Client).

---

## 6. Activity Service (Port 8083)

**Rôle:** Gestion des activités paroissiales

### Dépendances

Utilise les **dépendances communes** listées dans la section User Service.

---

## 7. Worship Service (Port 8084)

**Rôle:** Gestion des célébrations, intentions de messe et confessions

### Dépendances

Utilise les **dépendances communes** listées dans la section User Service.

---

## 8. Communication Service (Port 8085)

**Rôle:** Gestion des communiqués paroissiaux

### Dépendances

Utilise les **dépendances communes** listées dans la section User Service.

---

## 9. Content Service (Port 8086)

**Rôle:** Gestion des lieux saints, objets sacrés et programmes paroissiaux

### Dépendances

Utilise les **dépendances communes** listées dans la section User Service.

---

## 10. API Documentation Service (Port 8087)

**Rôle:** Service d'agrégation de la documentation API (Swagger/OpenAPI)

### Dépendances

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- SpringDoc OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>

    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- WebClient pour récupérer les specs OpenAPI -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <!-- Actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Dépendances de gestion

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

**Note:** Ce service est optionnel mais recommandé pour centraliser la documentation API. Voir `SWAGGER_CONFIGURATION.md` pour plus de détails.

---

## Dépendances communes résumées

### Pour tous les microservices métier (user, parish, activity, worship, communication, content)

```xml
<!-- Spring Boot Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- OAuth2 Resource Server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Eureka Client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Tests -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Dépendances de gestion (pour tous les services)

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## Dépendances optionnelles recommandées

### Pour tous les microservices (optionnel mais recommandé)

```xml
<!-- Spring Boot Actuator (monitoring) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- SpringDoc OpenAPI (documentation API) - RECOMMANDÉ -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

**Note:** SpringDoc OpenAPI est fortement recommandé pour exposer la documentation Swagger de chaque microservice. Voir `SWAGGER_CONFIGURATION.md` pour la configuration complète.

### Pour Config Service (si utilisation de Git)

```xml
<!-- Si configuration stockée dans Git -->
<!-- Déjà inclus dans spring-cloud-config-server -->
```

### Pour Config Service (si utilisation de Vault)

```xml
<!-- Spring Cloud Vault -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>
```

---

## Configuration Maven Compiler Plugin (pour Lombok)

Pour tous les services utilisant Lombok, ajouter dans le `build` section:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## Tableau récapitulatif

| Service | Port | Dépendances Spécifiques | Dépendances Communes |
|---------|------|-------------------------|----------------------|
| **Discovery Service** | 8761 | Eureka Server | Tests |
| **Config Service** | 8888 | Config Server, Actuator | Eureka Client, Tests |
| **Gateway Service** | 8888 | Gateway, Security, Actuator | Eureka Client, OAuth2, Tests |
| **User Service** | 8081 | Keycloak Admin Client | Toutes les dépendances communes |
| **Parish Service** | 8082 | Aucune | Toutes les dépendances communes |
| **Activity Service** | 8083 | Aucune | Toutes les dépendances communes |
| **Worship Service** | 8084 | Aucune | Toutes les dépendances communes |
| **Communication Service** | 8085 | Aucune | Toutes les dépendances communes |
| **Content Service** | 8086 | Aucune | Toutes les dépendances communes |
| **API Documentation Service** | 8087 | WebFlux, OpenAPI | Web, Eureka Client, Actuator |

---

## Notes importantes

1. **Spring Cloud Dependencies:** Tous les services doivent inclure `spring-cloud-dependencies` dans `dependencyManagement` avec la version `2025.0.1`.

2. **Java Version:** Tous les services utilisent Java 21.

3. **Spring Boot Version:** 
   - Discovery Service: `3.5.10`
   - Gateway Service: `3.5.11-SNAPSHOT`
   - Autres services: `3.5.10`

4. **PostgreSQL:** Le driver PostgreSQL est géré automatiquement par Spring Boot, pas besoin de spécifier la version.

5. **Keycloak:** Seul le `user-service` nécessite le client admin Keycloak pour la synchronisation.

6. **OpenFeign:** Tous les microservices métier utilisent OpenFeign pour la communication inter-services.

7. **Eureka Client:** Tous les services (sauf Discovery Service) s'enregistrent auprès d'Eureka.

8. **OAuth2:** Tous les microservices métier et le Gateway utilisent OAuth2 Resource Server pour valider les tokens JWT de Keycloak.

---

## Vérification des dépendances

Pour vérifier que toutes les dépendances sont correctement configurées:

1. Vérifier que chaque `pom.xml` contient les dépendances listées ci-dessus
2. Exécuter `mvn dependency:tree` dans chaque service pour voir l'arbre des dépendances
3. Vérifier qu'il n'y a pas de conflits de versions avec `mvn dependency:analyze`

---

**Date de création:** 2025-01-26  
**Version:** 1.0
