# Déploiement Paroisse Connect sur serveur Ubuntu (Docker)

Ce guide décrit les étapes pour déployer l’application **Paroisse Connect** sur un serveur Ubuntu en utilisant **Docker**, sans modifier les logiciels déjà installés sur le serveur (PostgreSQL, Java, etc. restent intacts).

Le projet est hébergé sur **GitHub** ; le serveur clone le dépôt puis lance l’ensemble avec Docker Compose.

---

## 1. Pourquoi Docker n’impacte pas les installations existantes

- **Réseau** : Les conteneurs utilisent un réseau Docker dédié (`paroisse-network`). Les ports exposés (5433, 8088, 8761, etc.) sont uniquement ceux que vous mappez dans `docker-compose.yml`.
- **Fichiers** : Données et code vivent dans le répertoire du projet et dans des **volumes Docker** (ex. `postgres_data`). Aucune installation système (ex. `/usr`, `/etc`) n’est modifiée pour l’app.
- **Runtime** : Java et PostgreSQL tournent **dans les conteneurs**, pas sur l’hôte. Vous pouvez garder une autre version de Java ou PostgreSQL installée sur le serveur pour d’autres usages.

En résumé : installer Docker/Docker Compose et lancer ce projet n’installe ni n’écrase PostgreSQL ni Java au niveau du système.

---

## 2. Prérequis sur le serveur Ubuntu

- **OS** : Ubuntu Server 22.04 LTS ou 24.04 LTS (recommandé).
- **Accès** : SSH avec un utilisateur pouvant utiliser `sudo`.
- **Réseau** : Le serveur doit pouvoir accéder à Internet (pour clone GitHub, téléchargement d’images et de dépendances Maven).
- **Ressources** : Au moins 2 Go RAM, 10 Go disque libre (recommandé 4 Go RAM et 20 Go pour le build et les logs).

Aucune nécessité d’installer Java ou PostgreSQL sur l’hôte pour cette application.

---

## 3. Installer Docker et Docker Compose (sans toucher au reste)

On installe uniquement le moteur Docker et Docker Compose. Les paquets système existants ne sont pas supprimés.

### 3.1 Mettre à jour le système (optionnel mais recommandé)

```bash
sudo apt-get update
sudo apt-get upgrade -y
```

**Explication** : Met à jour la liste des paquets et les paquets déjà installés. Réduit les failles de sécurité.

### 3.2 Installer les dépendances pour Docker

```bash
sudo apt-get install -y ca-certificates curl gnupg
```

**Explication** : `ca-certificates` et `curl` permettent de télécharger des fichiers en HTTPS. `gnupg` sert à vérifier la clé du dépôt Docker.

### 3.3 Ajouter la clé GPG et le dépôt Docker officiel

```bash
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "${VERSION_CODENAME:-$VERSION_ID}") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

**Explication** : On ajoute le dépôt officiel Docker pour Ubuntu. Les paquets Docker ne remplacent pas les paquets système (Java, PostgreSQL, etc.) car les noms de paquets sont différents.

### 3.4 Installer le moteur Docker et Docker Compose

```bash
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

**Explication** :
- `docker-ce` : moteur Docker.
- `docker-compose-plugin` : fournit la commande `docker compose` (v2). Le projet utilise `docker-compose` (v1) ou `docker compose` (v2) ; les deux sont supportés si vous avez le plugin.

### 3.5 Permettre à votre utilisateur de lancer Docker sans sudo (recommandé)

```bash
sudo usermod -aG docker $USER
```

**Explication** : Ajoute votre utilisateur au groupe `docker`. Après une **nouvelle connexion SSH**, vous pourrez lancer `docker` et `docker compose` sans `sudo`. Évite de lancer les conteneurs en root.

**Important** : Déconnectez-vous puis reconnectez-vous en SSH (ou exécutez `newgrp docker`) pour que le groupe soit pris en compte.

### 3.6 Vérifier l’installation

```bash
docker --version
docker compose version
```

**Explication** : Vérifie que Docker et Docker Compose sont bien installés.

---

## 4. Créer un répertoire pour l’application

On utilise un répertoire dédié (par exemple sous `/opt` ou dans le home de l’utilisateur). Tout le déploiement reste isolé dans ce dossier.

```bash
# Exemple : déploiement dans /opt (nécessite sudo pour créer /opt/paroisse-connect)
sudo mkdir -p /opt/paroisse-connect
sudo chown $USER:$USER /opt/paroisse-connect
cd /opt/paroisse-connect
```

**Alternative (sans sudo)** : déployer dans votre répertoire personnel :

```bash
mkdir -p ~/paroisse-connect
cd ~/paroisse-connect
```

**Explication** : Un seul répertoire contient le code, le `docker-compose.yml` et les volumes (données). Facilite les sauvegardes et les mises à jour.

---

## 5. Cloner le projet depuis GitHub

Remplacez `VOTRE_ORGANISATION` et `paroisseConnect` par l’URL réelle de votre dépôt.

```bash
cd /opt/paroisse-connect   # ou cd ~/paroisse-connect
git clone https://github.com/VOTRE_ORGANISATION/paroisseConnect.git .
```

Le point (`.`) à la fin clone dans le répertoire courant (par ex. `/opt/paroisse-connect`) au lieu de créer un sous-dossier `paroisseConnect`.

**Si le dépôt est privé** : utilisez un token ou une clé SSH, par exemple :

```bash
git clone https://<TOKEN>@github.com/VOTRE_ORGANISATION/paroisseConnect.git .
# ou
git clone git@github.com:VOTRE_ORGANISATION/paroisseConnect.git .
```

**Explication** : Vous récupérez la dernière version du code et de la configuration Docker (Dockerfile, docker-compose.yml).

---

## 6. (Optionnel) Fichier d’environnement pour la production

Pour changer des mots de passe ou des paramètres sans modifier le dépôt, vous pouvez utiliser un fichier `.env` à la racine du projet (le même répertoire que `docker-compose.yml`).

```bash
cd /opt/paroisse-connect   # ou le répertoire où vous avez cloné
nano .env
```

Exemple de contenu (à adapter) :

```env
# Base de données (optionnel : par défaut dans docker-compose)
POSTGRES_PASSWORD=votre_mot_de_passe_securise

# Ports exposés sur l’hôte (optionnel)
# GATEWAY_PORT=8088
# POSTGRES_PORT=5433
```

Ensuite, dans `docker-compose.yml`, vous pouvez référencer ces variables avec `${POSTGRES_PASSWORD}` etc. Si vous ne créez pas de `.env`, le projet fonctionne avec les valeurs par défaut du `docker-compose.yml`.

**Explication** : Le `.env` permet de garder des secrets hors du dépôt Git (pensez à ajouter `.env` dans `.gitignore` si il contient des secrets).

---

## 7. Construire les images et démarrer les services

Tout se fait dans le répertoire du projet.

### 7.1 Premier déploiement (build + démarrage)

```bash
cd /opt/paroisse-connect   # ou le répertoire du clone
docker compose build
docker compose up -d
```

**Explication** :
- `docker compose build` : construit les images des microservices (Maven, etc.). Le premier build peut durer 10–15 minutes.
- `docker compose up -d` : démarre tous les conteneurs en arrière-plan (`-d`). Les services attendent les healthchecks (PostgreSQL, Discovery, Config, puis les autres).

Si votre environnement n’a que la commande `docker-compose` (sans espace) :

```bash
docker-compose build
docker-compose up -d
```

### 7.2 Vérifier que les conteneurs tournent

```bash
docker compose ps
```

**Explication** : Affiche la liste des conteneurs, leur statut (Up/Exit) et les ports mappés. Attendez que les services passent en état **healthy** (quelques minutes après le premier démarrage).

### 7.3 Vérifier les logs (en cas de problème)

```bash
docker compose logs -f
```

Pour un service précis (ex. gateway) :

```bash
docker compose logs -f gateway-service
```

**Explication** : Les logs permettent de voir les erreurs de démarrage (ex. base de données, config, mémoire).

---

## 8. Accès à l’application après déploiement

Une fois tous les services **healthy** :

- **API / Gateway** : `http://IP_DU_SERVEUR:8088`
- **Santé du gateway** : `http://IP_DU_SERVEUR:8088/actuator/health`
- **Swagger UI** : `http://IP_DU_SERVEUR:8088/swagger-ui.html`
- **Eureka** : `http://IP_DU_SERVEUR:8761`

Remplacez `IP_DU_SERVEUR` par l’adresse IP ou le nom de domaine du serveur. Si vous testez depuis le serveur lui-même, vous pouvez utiliser `localhost`.

**Explication** : Le gateway est exposé sur le port **8088** (configuré dans `docker-compose.yml`) pour éviter un conflit avec d’éventuelles autres applications sur 8080.

---

## 9. (Optionnel) Redémarrage automatique au boot du serveur

Par défaut, les conteneurs lancés avec `docker compose up -d` ne redémarrent pas toujours après un reboot, selon la configuration Docker. Pour que la stack redémarre automatiquement :

```bash
cd /opt/paroisse-connect
docker compose up -d
```

Puis vérifiez la politique de redémarrage des conteneurs. Avec Docker Compose v2, vous pouvez ajouter dans `docker-compose.yml` pour chaque service :

```yaml
restart: unless-stopped
```

**Explication** : `restart: unless-stopped` demande à Docker de redémarrer le conteneur après un redémarrage du serveur, sauf si vous l’avez arrêté manuellement.

---

## 10. Mise à jour après une modification sur GitHub

Pour tirer les dernières modifications du code et redéployer :

```bash
cd /opt/paroisse-connect
git pull
docker compose build
docker compose up -d
```

**Explication** : `git pull` met à jour les fichiers ; `docker compose build` reconstruit les images modifiées ; `docker compose up -d` recrée les conteneurs si nécessaire.

---

## 11. Arrêter ou supprimer l’application

- **Arrêter les conteneurs (garder les données)** :
  ```bash
  cd /opt/paroisse-connect
  docker compose stop
  ```

- **Arrêter et supprimer les conteneurs (garder les volumes et le code)** :
  ```bash
  docker compose down
  ```

- **Tout supprimer y compris les volumes (données PostgreSQL supprimées)** :
  ```bash
  docker compose down -v
  ```

**Explication** : Les volumes nommés (ex. `postgres_data`) conservent les données tant que vous n’utilisez pas `-v`. Utilisez `-v` uniquement si vous voulez repartir de zéro.

---

## 12. Résumé : toutes les commandes regroupées

Copier-coller ces blocs dans l’ordre sur le serveur Ubuntu (en adaptant l’URL Git et le répertoire si besoin).

### A. Installation Docker (une seule fois)

```bash
# Mise à jour système
sudo apt-get update
sudo apt-get upgrade -y

# Dépendances
sudo apt-get install -y ca-certificates curl gnupg

# Clé et dépôt Docker
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "${VERSION_CODENAME:-$VERSION_ID}") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Installation Docker + Docker Compose
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Utilisateur dans le groupe docker (puis se reconnecter en SSH)
sudo usermod -aG docker $USER

# Vérification (après reconnexion)
docker --version
docker compose version
```

### B. Répertoire et clone GitHub

```bash
# Répertoire (choisir l’un des deux)
sudo mkdir -p /opt/paroisse-connect && sudo chown $USER:$USER /opt/paroisse-connect && cd /opt/paroisse-connect
# OU : mkdir -p ~/paroisse-connect && cd ~/paroisse-connect

# Clone (remplacer par votre URL GitHub)
git clone https://github.com/VOTRE_ORGANISATION/paroisseConnect.git .
```

### C. Premier démarrage

```bash
docker compose build
docker compose up -d
docker compose ps
```

### D. Vérifications

```bash
# Logs
docker compose logs -f

# Santé du gateway (depuis le serveur)
curl -s http://localhost:8088/actuator/health
```

### E. Mise à jour ultérieure

```bash
cd /opt/paroisse-connect   # ou ~/paroisse-connect
git pull
docker compose build
docker compose up -d
```

### F. Arrêt / suppression

```bash
cd /opt/paroisse-connect
docker compose stop          # arrêt
docker compose down          # arrêt + suppression des conteneurs
docker compose down -v       # + suppression des volumes (données)
```

---

## 13. Dépannage rapide

| Problème | Action |
|----------|--------|
| Port 8088 ou 5433 déjà utilisé | Changer le mapping dans `docker-compose.yml` (ex. `"8089:8080"` pour le gateway) ou arrêter le service qui utilise le port. |
| Build très lent | Normal au premier build (téléchargement Maven). Attendre ou vérifier la connexion Internet du serveur. |
| Un service reste en "starting" ou "unhealthy" | Consulter les logs : `docker compose logs <nom-service>`. Vérifier la RAM et l’espace disque. |
| Impossible d’accéder à Swagger depuis l’extérieur | Vérifier le pare-feu (ex. `sudo ufw allow 8088/tcp` puis `sudo ufw reload`). |

---

## 14. Bonnes pratiques résumées

1. **Ne pas lancer les conteneurs en root** : utiliser `usermod -aG docker $USER` et se reconnecter.
2. **Sauvegarder les données** : le volume `postgres_data` contient la base ; faire des sauvegardes régulières (ex. `pg_dump` depuis le conteneur ou un cron).
3. **Secrets** : en production, utiliser un `.env` ou un gestionnaire de secrets (Docker Secrets, Vault) et ne pas committer de mots de passe dans Git.
4. **Pare-feu** : n’ouvrir que les ports nécessaires (ex. 8088 pour l’API, 8761 si besoin pour Eureka).
5. **HTTPS** : pour exposer l’API en production, placer un reverse proxy (Nginx ou Caddy) devant le gateway et configurer SSL (ex. Let’s Encrypt). Le gateway reste en HTTP en interne.

---

Ce document couvre le déploiement de bout en bout sur Ubuntu avec Docker, en regroupant toutes les commandes et en expliquant chaque étape pour un déploiement propre et compréhensible.
