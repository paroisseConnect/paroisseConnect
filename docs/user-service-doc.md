# User Service — Documentation

Documentation du **user-service** (Paroisse Connect) : compilation, démarrage, vérifications, rôles USER/ADMIN et utilisation de Swagger.

---

## 1. Prérequis

- **Java 21** (OpenJDK 21)
- **Maven 3.9+**
- **PostgreSQL** (ex. Docker : `docker-compose up -d postgres`, port 5433 en local)
- Variables d’environnement ou Config Server pour la base (voir ci-dessous)

---

## 2. Compilation

La compilation du user-service doit passer sans erreur :

```bash
cd services/user-service
mvn clean compile -DskipTests
```

Ou pour packager le JAR :

```bash
mvn clean package -DskipTests
```

En cas d’échec, vérifier la version Java : `java -version` doit afficher **21**.

---

## 3. Démarrage du service

### 3.1 Avec Docker (recommandé en intégration)

```bash
# À la racine du projet
docker-compose up -d postgres discovery-service config-service user-service
```

Le user-service écoute sur le port **8081** (ou `USER_SERVICE_PORT`).

### 3.2 En local (Maven) sans Config Server ni Eureka

Utile pour développer ou tester sans lancer toute la stack :

```bash
cd services/user-service

export SPRING_PROFILES_ACTIVE=docker
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5433/churchnow?currentSchema=paroisse_schema"
export SPRING_DATASOURCE_USERNAME=church
export SPRING_DATASOURCE_PASSWORD=church

mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Deureka.client.enabled=false -Dspring.cloud.config.enabled=false"
```

**Important :** PostgreSQL doit être démarré (ex. `docker-compose up -d postgres`) et accessible sur le port **5433** (ou celui configuré).

---

## 4. Vérification du bon fonctionnement

### 4.1 Santé du service

```bash
curl -s http://localhost:8081/actuator/health
```

Réponse attendue : `{"status":"UP",...}` (HTTP 200).

### 4.2 Tests de tous les endpoints (script)

Un script exécute des appels type Postman sur tous les endpoints :

```bash
cd services/user-service
./test-endpoints.sh
```

En mode **direct** (port 8081), le script envoie le header **X-User-Id** pour les ressources protégées.  
En mode **gateway** (port 8088), il utilise le token JWT après login :

```bash
BASE_URL=http://localhost:8088/user-service ./test-endpoints.sh
```

### 4.3 Vérification des rôles USER / ADMIN

Un second script crée un utilisateur **fidèle** (USER) et un **admin** (USER + ADMIN), attribue le rôle ADMIN en base, puis vérifie les accès :

```bash
cd services/user-service
chmod +x verify-roles.sh
./verify-roles.sh http://localhost:8081
```

**Comportement attendu :**

| Utilisateur | GET /api/users/me | GET /api/admin/users |
|-------------|-------------------|------------------------|
| Fidèle (USER) | 200 | **403** |
| Admin (USER + ADMIN) | 200 | 200 |
| Sans header (X-User-Id) | 400 (header manquant) ou 401/403 | 401 ou 403 |

**Prérequis :** PostgreSQL accessible (ex. conteneur `paroisse-postgres`) pour l’insertion du lien `role_utilisateur` (rôle ADMIN, code 2).  
Si Docker n’est pas disponible, exécuter manuellement l’`INSERT` indiqué dans le script (voir commentaire dans `verify-roles.sh`).

**Note :** Si le fidèle reçoit 200 au lieu de 403 sur `/api/admin/users`, le service tourne encore avec l’ancienne configuration. Après modification de `SecurityConfig` (autorisations par chemin), **recompiler et redémarrer** le user-service, puis relancer `verify-roles.sh`.

---

## 5. Swagger — Documentation et tests des API

### 5.1 Accès à l’interface

Une fois le service démarré :

- **Swagger UI :** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI JSON :** [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

Via la gateway (si configurée) :

- **Swagger UI :** [http://localhost:8088/user-service/swagger-ui.html](http://localhost:8088/user-service/swagger-ui.html)

### 5.2 Tester les endpoints avec Swagger

1. **Auth (public)**  
   - `POST /api/auth/register` : créer un compte (body : username, noms, prenoms, password, etc.).  
   - `POST /api/auth/login` : se connecter (username, password). La réponse contient un **token** JWT.

2. **Ressources protégées (USER ou ADMIN)**  
   - Cliquer sur **Authorize** en haut de la page Swagger.  
   - **Option A — Bearer JWT :** coller le token obtenu au login dans le champ **bearerAuth** (ex. `eyJ...`).  
   - **Option B — X-User-Id (appel direct au service) :** si vous testez en direct sur le port 8081, vous pouvez indiquer le **code** utilisateur (ex. `1`) dans le header **X-User-Id**.

3. **Endpoints USER** (profil, abonnements)  
   - Ex. : `GET /api/users/me`, `PATCH /api/users/me`, `GET /api/subscriptions`, `POST /api/subscriptions`, `DELETE /api/subscriptions/{code}`.  
   - Nécessitent un utilisateur authentifié avec le rôle **USER** (tout utilisateur existant en base a au moins USER).

4. **Endpoints ADMIN**  
   - Ex. : `GET /api/admin/users`, `GET /api/admin/users/{code}`, `GET /api/admin/roles`, etc.  
   - Nécessitent le rôle **ADMIN** (utilisateur ayant un rôle de libellé `"ADMIN"` en base, via `role_utilisateur`).

La documentation Swagger décrit pour chaque endpoint les réponses (200, 201, 400, 401, 403, 404, 409) et les schémas de requête/réponse.

---

## 6. Rôles et contrôle d’accès

### 6.1 Rôles utilisés

- **USER** : tout utilisateur dont le compte existe en base. Accès : `/api/users/**`, `/api/subscriptions/**` (profil et abonnements du fidèle).
- **ADMIN** : utilisateur ayant au moins un rôle dont le **libellé** est `"ADMIN"` en base. Accès en plus : `/api/admin/**` (utilisateurs, rôles, abonnements globaux).

### 6.2 Comment attribuer le rôle ADMIN

Les rôles par défaut sont créés par la migration Flyway **V2__default_roles.sql** (codes 1 = USER, 2 = ADMIN).  
Pour donner le rôle ADMIN à un utilisateur (ex. `code_utilisateur = 2`) :

```sql
SET search_path TO paroisse_schema;

-- Exemple : lier l'utilisateur 2 au rôle ADMIN (code 2)
-- Remplacez 1002 par un code unique (ex. code_utilisateur * 1000 + 2)
INSERT INTO role_utilisateur (code, code_utilisateur, code_role)
VALUES (1002, 2, 2)
ON CONFLICT (code) DO NOTHING;
```

Le script `verify-roles.sh` fait cet enregistrement automatiquement si Docker (conteneur Postgres) est disponible.

### 6.3 Authentification côté service

- En production, la **gateway** valide le JWT et envoie l’identifiant utilisateur au user-service via le header **X-User-Id**.
- Le user-service utilise ce header pour charger les rôles en base et remplir le **SecurityContext** (USER / ADMIN). Les règles `hasRole("USER")` et `hasRole("ADMIN")` sont appliquées sur les chemins configurés dans `SecurityConfig`.

---

## 7. Résumé des commandes utiles

| Action | Commande |
|--------|----------|
| Compiler | `cd services/user-service && mvn compile -DskipTests` |
| Démarrer (local) | Voir § 3.2 |
| Santé | `curl -s http://localhost:8081/actuator/health` |
| Tests endpoints | `./services/user-service/test-endpoints.sh` |
| Vérification rôles | `./services/user-service/verify-roles.sh http://localhost:8081` |
| Swagger UI | Ouvrir [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |

---

## 8. En cas de problème

- **403 sur /api/admin/** avec un utilisateur qui devrait être admin** : vérifier qu’une ligne existe dans `paroisse_schema.role_utilisateur` avec `code_role = 2` (ADMIN) et le bon `code_utilisateur`, puis redémarrer le user-service.
- **Compilation / Java** : s’assurer que `java -version` affiche bien la version 21.
- **Connexion base** : vérifier que PostgreSQL est démarré et que l’URL, le schéma `paroisse_schema` et les identifiants sont corrects (variables d’environnement ou Config Server).
