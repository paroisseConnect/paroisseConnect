# 🐳 Docker Quick Start - Paroisse Connect

Guide rapide pour démarrer l'application avec Docker.

## ⚡ Démarrage rapide

```bash
# 1. Construire toutes les images
docker compose build

# 2. Démarrer tous les services
docker compose up -d

# 3. Vérifier l'état
docker compose ps

# 4. Voir les logs
docker compose logs -f
```

## 📋 Commandes essentielles

### Démarrer/Arrêter
```bash
# Démarrer
docker compose up -d

# Arrêter
docker compose stop

# Arrêter et supprimer
docker compose down
```

### Logs
```bash
# Tous les services
docker compose logs -f

# Un service spécifique
docker compose logs -f gateway-service
```

### Rebuild
```bash
# Reconstruire après modification du code
docker compose up -d --build
```

## 🔍 Vérification

Une fois démarré, vérifiez :

- **Eureka Dashboard** : http://localhost:8761
- **Gateway** : http://localhost:8088/actuator/health
- **API Documentation (Gateway)** : http://localhost:8088/swagger-ui.html

## ⚠️ Notes importantes

1. **Premier démarrage** : Peut prendre 5-10 minutes (téléchargement et compilation)
2. **Ordre de démarrage** : Les services démarrent automatiquement dans le bon ordre
3. **Healthchecks** : Attendez que tous les services soient "healthy" avant d'utiliser l'application
4. **PostgreSQL** : Le port exposé sur l'hôte est **5433** (pour éviter un conflit avec un PostgreSQL local sur 5432). Connexion depuis l'hôte : `localhost:5433`.
5. **Gateway** : Le port exposé sur l'hôte est **8088** (pour éviter un conflit avec une autre app sur 8080). Accès : http://localhost:8088

## 🔧 Dépannage

- **Port 5432 déjà utilisé** : Le `docker-compose.yml` expose PostgreSQL sur le port **5433** par défaut. Si besoin, modifiez `ports: - "5433:5432"` dans la section `postgres`.
- **Port 8080 déjà utilisé** : Le gateway est exposé sur le port **8088** par défaut. Utilisez http://localhost:8088 pour le gateway et Swagger.
- **config-service en erreur** (« Property spring.profiles.active is invalid in a profile specific resource ») : Reconstruire l’image : `docker-compose build config-service` puis `docker-compose up -d`.
- **Build lent** : Le premier build Maven dans Docker peut prendre 10–15 min (téléchargement des dépendances). Lancez `docker-compose build` et attendez la fin.

## 📚 Documentation complète

Pour plus de détails, consultez [DOCKER_README.md](./DOCKER_README.md)
