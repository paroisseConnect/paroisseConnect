# Guide d'accès à la documentation API

## Problèmes courants et solutions

### 1. Erreur 404 sur Swagger UI via la Gateway

**Problème** : `http://localhost:8080/swagger-ui/index.html` retourne une 404

**Explication** : 
- Spring Cloud Gateway est basé sur WebFlux (réactif) et ne peut pas servir directement Swagger UI
- Swagger UI doit être accessible via chaque service individuellement

**Solution** : Utilisez les URLs suivantes :

#### Via la Gateway (recommandé)
- **API Documentation Service** : `http://localhost:8080/api-documentation-service/swagger-ui.html`
- **User Service** : `http://localhost:8080/user-service/swagger-ui.html`
- **Parish Service** : `http://localhost:8080/parish-service/swagger-ui.html`
- **Activity Service** : `http://localhost:8080/activity-service/swagger-ui.html`
- **Communication Service** : `http://localhost:8080/communication-service/swagger-ui.html`
- **Content Service** : `http://localhost:8080/content-service/swagger-ui.html`
- **Worship Service** : `http://localhost:8080/worship-service/swagger-ui.html`

#### Accès direct (si la Gateway ne fonctionne pas)
- **API Documentation Service** : `http://localhost:8087/swagger-ui.html`
- **User Service** : `http://localhost:8081/swagger-ui.html`
- **Parish Service** : `http://localhost:8082/swagger-ui.html`
- **Activity Service** : `http://localhost:8083/swagger-ui.html`
- **Communication Service** : `http://localhost:8084/swagger-ui.html`
- **Content Service** : `http://localhost:8085/swagger-ui.html`
- **Worship Service** : `http://localhost:8086/swagger-ui.html`

### 2. Services non visibles dans Eureka

**Problème** : Seuls 3 services apparaissent dans Eureka (gateway, config, api-docs)

**Explication** : Les services métier ne sont pas démarrés

**Solution** : Démarrez tous les services dans l'ordre suivant :

1. **Discovery Service** (port 8761) - Doit être démarré en premier
2. **Config Service** (port 8888) - Doit être démarré en second
3. **Gateway Service** (port 8080)
4. **Services métier** (dans n'importe quel ordre) :
   - User Service (8081)
   - Parish Service (8082)
   - Activity Service (8083)
   - Communication Service (8084)
   - Content Service (8085)
   - Worship Service (8086)
   - API Documentation Service (8087)

### 3. Vérification de l'état des services

**Via Eureka Dashboard** :
- Accédez à : `http://localhost:8761`
- Vérifiez que tous les services sont enregistrés avec le statut "UP"

**Via l'API Documentation Service** :
- Accédez à : `http://localhost:8080/api-documentation-service/api/documentation/services`
- Cette API liste tous les services enregistrés dans Eureka

### 4. API Documentation centralisée

Le service **api-documentation-service** fournit une documentation centralisée :

- **Swagger UI centralisé** : `http://localhost:8080/api-documentation-service/swagger-ui.html`
- **Liste des services** : `http://localhost:8080/api-documentation-service/api/documentation/services`
- **URLs Swagger** : `http://localhost:8080/api-documentation-service/api/documentation/swagger-urls`

## URLs importantes

### Services d'infrastructure
- **Eureka Dashboard** : `http://localhost:8761`
- **Config Server** : `http://localhost:8888`
- **Gateway** : `http://localhost:8080`

### Documentation API
- **Documentation centralisée** : `http://localhost:8080/api-documentation-service/swagger-ui.html`
- **API Docs JSON** : `http://localhost:8080/api-documentation-service/v3/api-docs`

## Notes importantes

1. **Tous les services doivent être démarrés** pour que la Gateway puisse router les requêtes
2. **Swagger UI n'est pas accessible directement sur la Gateway** - utilisez les URLs spécifiques à chaque service
3. **Le routage dynamique** fonctionne automatiquement via Eureka - les services sont accessibles via `http://localhost:8080/{service-name}/...`
