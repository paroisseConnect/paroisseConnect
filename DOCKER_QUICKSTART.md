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
- **Gateway** : http://localhost:8080/actuator/health
- **API Documentation** : http://localhost:8087/swagger-ui.html

## ⚠️ Notes importantes

1. **Premier démarrage** : Peut prendre 5-10 minutes (téléchargement et compilation)
2. **Ordre de démarrage** : Les services démarrent automatiquement dans le bon ordre
3. **Healthchecks** : Attendez que tous les services soient "healthy" avant d'utiliser l'application

## 📚 Documentation complète

Pour plus de détails, consultez [DOCKER_README.md](./DOCKER_README.md)
