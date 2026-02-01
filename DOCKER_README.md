# Guide Docker - Paroisse Connect Microservices

Ce guide explique comment dockeriser et déployer l'application Paroisse Connect en utilisant Docker et Docker Compose.

## 📋 Table des matières

1. [Architecture Docker](#architecture-docker)
2. [Structure des fichiers](#structure-des-fichiers)
3. [Bonnes pratiques implémentées](#bonnes-pratiques-implémentées)
4. [Prérequis](#prérequis)
5. [Construction et démarrage](#construction-et-démarrage)
6. [Gestion des services](#gestion-des-services)
7. [Configuration](#configuration)
8. [Dépannage](#dépannage)

## 🏗️ Architecture Docker

### Services Dockerisés

L'application est composée de 11 services :

1. **postgres** - Base de données PostgreSQL
2. **discovery-service** - Service Discovery (Eureka Server)
3. **config-service** - Config Server (Spring Cloud Config)
4. **gateway-service** - API Gateway (Spring Cloud Gateway)
5. **user-service** - Service de gestion des utilisateurs
6. **parish-service** - Service de gestion des paroisses
7. **activity-service** - Service de gestion des activités
8. **communication-service** - Service de communication
9. **content-service** - Service de gestion de contenu
10. **worship-service** - Service de gestion des cultes
11. **api-documentation-service** - Service de documentation API

### Réseau Docker

Tous les services communiquent via un réseau Docker dédié (`paroisse-network`) qui permet :
- La résolution DNS automatique (les services se trouvent par leur nom)
- L'isolation du trafic réseau
- La communication sécurisée entre conteneurs

## 📁 Structure des fichiers

```
paroisseConnect/
├── Dockerfile                    # Dockerfile multi-stage pour tous les services
├── docker-compose.yml           # Orchestration de tous les services
├── .dockerignore                # Fichiers à exclure du build
├── DOCKER_README.md             # Ce fichier
└── services/
    └── [service-name]/
        └── src/main/resources/
            └── application-docker.properties  # Configuration spécifique Docker
```

## ✅ Bonnes pratiques implémentées

### 1. Dockerfile Multi-Stage

**Pourquoi ?** Réduit la taille de l'image finale et améliore la sécurité.

**Comment ça marche ?**
- **Stage 1 (build)** : Utilise Maven pour compiler l'application
- **Stage 2 (runtime)** : Utilise uniquement le JRE et le JAR compilé

**Avantages :**
- Image finale ~50% plus petite
- Pas d'outils de build dans l'image de production
- Meilleure sécurité

### 2. Layer Caching

**Pourquoi ?** Accélère les builds Docker en réutilisant les couches mises en cache.

**Comment ça marche ?**
```dockerfile
# D'abord copier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Ensuite copier le code source
COPY . .
RUN mvn package
```

**Avantages :**
- Les dépendances Maven sont mises en cache
- Seul le code source change entre les builds
- Builds beaucoup plus rapides

### 3. Utilisateur Non-Root

**Pourquoi ?** Améliore la sécurité en évitant d'exécuter l'application en tant que root.

**Comment ça marche ?**
```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
```

**Avantages :**
- Réduction de la surface d'attaque
- Conformité aux bonnes pratiques de sécurité

### 4. Healthchecks

**Pourquoi ?** Permet à Docker de vérifier automatiquement l'état de santé des services.

**Comment ça marche ?**
- Chaque service expose un endpoint `/actuator/health`
- Docker vérifie périodiquement cet endpoint
- Les dépendances attendent que les services soient "healthy"

**Avantages :**
- Démarrage ordonné des services
- Détection automatique des problèmes
- Meilleure résilience

### 5. Variables d'environnement

**Pourquoi ?** Permet de configurer les services sans modifier le code.

**Comment ça marche ?**
- Configuration via `docker-compose.yml`
- Utilisation de profils Spring (`docker`)
- Remplacement de `localhost` par les noms de services Docker

**Avantages :**
- Configuration flexible
- Pas de hardcoding
- Facile à adapter pour différents environnements

### 6. Volumes persistants

**Pourquoi ?** Conserve les données de la base de données entre les redémarrages.

**Comment ça marche ?**
```yaml
volumes:
  - postgres_data:/var/lib/postgresql/data
```

**Avantages :**
- Données persistantes
- Pas de perte de données lors des redémarrages

## 🔧 Prérequis

### Logiciels nécessaires

1. **Docker** (version 20.10 ou supérieure)
   ```bash
   docker --version
   ```

2. **Docker Compose** (version 2.0 ou supérieure)
   ```bash
   docker compose version
   ```

3. **Java 21** (uniquement pour le développement local, pas nécessaire pour Docker)

### Vérification de l'installation

```bash
# Vérifier Docker
docker --version
docker ps

# Vérifier Docker Compose
docker compose version

# Vérifier l'espace disque (recommandé: au moins 5GB libres)
df -h
```

## 🚀 Construction et démarrage

### Étape 1 : Cloner et naviguer vers le projet

```bash
cd /home/wuwei/paroisseConnect
```

### Étape 2 : Construire les images Docker

**Option A : Construire toutes les images**
```bash
docker compose build
```

**Option B : Construire une image spécifique**
```bash
docker compose build user-service
```

**Option C : Construire sans cache (si problèmes)**
```bash
docker compose build --no-cache
```

**Explication :**
- Docker Compose lit le `docker-compose.yml`
- Pour chaque service, il exécute le `Dockerfile` avec l'argument `SERVICE_NAME`
- Les images sont construites en utilisant le build multi-stage
- Les dépendances Maven sont téléchargées et mises en cache

### Étape 3 : Démarrer tous les services

```bash
docker compose up -d
```

**Explication :**
- `-d` : Démarrer en mode détaché (en arrière-plan)
- Docker Compose démarre les services dans l'ordre des dépendances :
  1. `postgres` (base de données)
  2. `discovery-service` (Eureka)
  3. `config-service` (Config Server)
  4. `gateway-service` (Gateway)
  5. Services métier (en parallèle)

### Étape 4 : Vérifier l'état des services

```bash
# Voir tous les conteneurs
docker compose ps

# Voir les logs
docker compose logs -f

# Voir les logs d'un service spécifique
docker compose logs -f gateway-service
```

### Étape 5 : Vérifier que tout fonctionne

```bash
# Vérifier Eureka Dashboard
curl http://localhost:8761

# Vérifier la Gateway
curl http://localhost:8080/actuator/health

# Vérifier un service métier
curl http://localhost:8081/actuator/health
```

## 🎮 Gestion des services

### Démarrer les services

```bash
# Démarrer tous les services
docker compose up -d

# Démarrer un service spécifique
docker compose up -d user-service

# Démarrer plusieurs services
docker compose up -d postgres discovery-service config-service
```

### Arrêter les services

```bash
# Arrêter tous les services (sans supprimer les conteneurs)
docker compose stop

# Arrêter un service spécifique
docker compose stop user-service

# Arrêter et supprimer les conteneurs
docker compose down

# Arrêter, supprimer les conteneurs ET les volumes (⚠️ supprime les données)
docker compose down -v
```

### Redémarrer les services

```bash
# Redémarrer tous les services
docker compose restart

# Redémarrer un service spécifique
docker compose restart user-service
```

### Voir les logs

```bash
# Logs de tous les services
docker compose logs -f

# Logs d'un service spécifique
docker compose logs -f gateway-service

# Dernières 100 lignes
docker compose logs --tail=100 gateway-service

# Logs depuis une date
docker compose logs --since 2024-01-01T00:00:00 gateway-service
```

### Reconstruire après modification du code

```bash
# Reconstruire et redémarrer
docker compose up -d --build

# Reconstruire un service spécifique
docker compose up -d --build user-service
```

## ⚙️ Configuration

### Variables d'environnement

Les variables d'environnement sont définies dans `docker-compose.yml`. Principales variables :

- `SPRING_PROFILES_ACTIVE=docker` : Active le profil Docker
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` : URL du serveur Eureka
- `SPRING_CLOUD_CONFIG_URI` : URL du Config Server
- `SPRING_DATASOURCE_URL` : URL de la base de données

### Profils Spring

Chaque service a un fichier `application-docker.properties` qui :
- Remplace `localhost` par les noms de services Docker
- Configure les URLs pour le réseau Docker
- Optimise les paramètres pour l'environnement conteneurisé

### Ports exposés

| Service | Port | URL |
|---------|------|-----|
| PostgreSQL | 5432 | `localhost:5432` |
| Discovery Service | 8761 | `http://localhost:8761` |
| Config Service | 8888 | `http://localhost:8888` |
| Gateway Service | 8080 | `http://localhost:8080` |
| User Service | 8081 | `http://localhost:8081` |
| Parish Service | 8082 | `http://localhost:8082` |
| Activity Service | 8083 | `http://localhost:8083` |
| Communication Service | 8084 | `http://localhost:8084` |
| Content Service | 8085 | `http://localhost:8085` |
| Worship Service | 8086 | `http://localhost:8086` |
| API Documentation Service | 8087 | `http://localhost:8087` |

## 🔍 Dépannage

### Problème : Les services ne démarrent pas

**Vérifier les logs :**
```bash
docker compose logs [service-name]
```

**Vérifier l'état :**
```bash
docker compose ps
```

**Vérifier les ressources :**
```bash
docker stats
```

### Problème : Erreur de connexion à la base de données

**Vérifier que PostgreSQL est démarré :**
```bash
docker compose ps postgres
```

**Vérifier les logs PostgreSQL :**
```bash
docker compose logs postgres
```

**Vérifier la connexion :**
```bash
docker compose exec postgres psql -U church -d churchnow
```

### Problème : Services non visibles dans Eureka

**Vérifier que Discovery Service est démarré :**
```bash
curl http://localhost:8761
```

**Vérifier les logs :**
```bash
docker compose logs discovery-service
docker compose logs [service-name]
```

**Vérifier la configuration réseau :**
```bash
docker network inspect paroisseconnect_paroisse-network
```

### Problème : Erreur "port already in use"

**Trouver le processus utilisant le port :**
```bash
# Linux/Mac
lsof -i :8080
# ou
netstat -tulpn | grep 8080

# Windows
netstat -ano | findstr :8080
```

**Arrêter le processus ou changer le port dans docker-compose.yml**

### Problème : Build échoue

**Nettoyer et reconstruire :**
```bash
docker compose down
docker system prune -f
docker compose build --no-cache
```

**Vérifier l'espace disque :**
```bash
df -h
docker system df
```

### Problème : Services lents à démarrer

**Normal !** Les services Spring Boot prennent du temps à démarrer. Vérifier les healthchecks :
```bash
docker compose ps
# Attendre que tous les services soient "healthy"
```

## 📊 Monitoring

### Vérifier l'utilisation des ressources

```bash
docker stats
```

### Vérifier les healthchecks

```bash
# Voir l'état de santé
docker compose ps

# Vérifier manuellement
curl http://localhost:8080/actuator/health
```

### Accéder aux interfaces

- **Eureka Dashboard** : http://localhost:8761
- **Gateway Health** : http://localhost:8080/actuator/health
- **Swagger UI** : http://localhost:8087/swagger-ui.html (accès direct)

## 🧹 Nettoyage

### Nettoyer les conteneurs arrêtés

```bash
docker compose down
```

### Nettoyer les images non utilisées

```bash
docker image prune -a
```

### Nettoyer tout (⚠️ attention)

```bash
docker system prune -a --volumes
```

## 📝 Notes importantes

1. **Premier démarrage** : Le premier démarrage peut prendre 5-10 minutes (téléchargement des images, compilation)
2. **Mémoire** : Assurez-vous d'avoir au moins 4GB de RAM disponibles
3. **Disque** : Les images Docker peuvent prendre plusieurs GB d'espace
4. **Réseau** : Les services communiquent via le réseau Docker, pas via localhost

## 🎯 Prochaines étapes

- [ ] Ajouter des variables d'environnement pour différents environnements (dev, prod)
- [ ] Configurer des secrets Docker pour les mots de passe
- [ ] Ajouter un reverse proxy (Nginx) devant la Gateway
- [ ] Configurer la persistance des logs
- [ ] Ajouter du monitoring (Prometheus, Grafana)
