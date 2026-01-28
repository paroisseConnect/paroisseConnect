# API Documentation Service

Ce service centralise la documentation de tous les microservices de Paroisse Connect.

## Accès à la documentation

### Documentation centralisée (Service de documentation)

- **Swagger UI** : http://localhost:8080/api-documentation-service/swagger-ui.html
- **API Docs JSON** : http://localhost:8080/api-documentation-service/v3/api-docs
- **Liste des services** : http://localhost:8080/api-documentation-service/api/documentation/services
- **URLs Swagger de tous les services** : http://localhost:8080/api-documentation-service/api/documentation/swagger-urls

### Documentation par service (via Gateway)

Chaque service expose sa propre documentation Swagger :

- **User Service** : http://localhost:8080/user-service/swagger-ui.html
- **Parish Service** : http://localhost:8080/parish-service/swagger-ui.html
- **Activity Service** : http://localhost:8080/activity-service/swagger-ui.html
- **Communication Service** : http://localhost:8080/communication-service/swagger-ui.html
- **Content Service** : http://localhost:8080/content-service/swagger-ui.html
- **Worship Service** : http://localhost:8080/worship-service/swagger-ui.html

### Documentation par service (accès direct)

Si vous accédez directement aux services (sans passer par la Gateway) :

- **User Service** : http://localhost:8081/swagger-ui.html
- **Parish Service** : http://localhost:8082/swagger-ui.html
- **Activity Service** : http://localhost:8083/swagger-ui.html
- **Communication Service** : http://localhost:8084/swagger-ui.html
- **Content Service** : http://localhost:8085/swagger-ui.html
- **Worship Service** : http://localhost:8086/swagger-ui.html
- **API Documentation Service** : http://localhost:8087/swagger-ui.html

## Fonctionnalités

### Découverte automatique des services

Le service utilise Eureka pour découvrir automatiquement tous les services enregistrés et fournir leurs informations de documentation.

### Endpoints disponibles

- `GET /api/documentation/services` : Liste tous les services avec leurs détails
- `GET /api/documentation/swagger-urls` : Liste toutes les URLs Swagger disponibles

## Configuration

La documentation est générée automatiquement à partir des annotations OpenAPI dans vos contrôleurs. Assurez-vous d'utiliser les annotations suivantes :

- `@Tag` : Pour grouper les endpoints par fonctionnalité
- `@Operation` : Pour documenter chaque endpoint
- `@Parameter` : Pour documenter les paramètres
- `@ApiResponse` : Pour documenter les réponses

## Exemple d'utilisation dans vos contrôleurs

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestion des utilisateurs")
public class UserController {

    @GetMapping
    @Operation(summary = "Liste tous les utilisateurs", 
               description = "Retourne la liste complète des utilisateurs")
    public ResponseEntity<List<User>> getAllUsers() {
        // ...
    }
}
```
