 # Plan des tables et endpoints par service
 
 ## Sources
 - Cahier des charges : `ressources/Cahier de Charge 2025_V1.pdf`
 - Schéma SQL : `ressources/churchnow.sql`
 
 Ce document liste, pour chaque service, les tables utilisées et les endpoints
 à exposer côté utilisateur (fidèle) et côté administrateur (équipe paroissiale),
 avec l’explication de l’usage fonctionnel demandé par le cahier des charges.
 
 ---
 
 ## user-service (authentification, comptes, rôles, abonnements)
 **Tables utilisées**
 - `utilisateurs` : comptes et informations de base des fidèles.
 - `roles`, `permissions`, `role_permission`, `role_utilisateur` : contrôle d’accès.
 - `abonnement_unite_ecclesiale` : abonnement principal et secondaires.
 - `unite_ecclesiale` (référence) : paroisses disponibles à l’abonnement.
 
 **Endpoints utilisateur (fidèle)**
 - `POST /api/auth/register` : créer un compte (nom, prénom, email, mot de passe, téléphone optionnel).
 - `POST /api/auth/login` : authentifier un fidèle et délivrer un jeton.
 - `GET /api/users/me` : obtenir le profil du fidèle connecté.
 - `PATCH /api/users/me` : mettre à jour son profil (coordonnées, contacts).
 - `GET /api/subscriptions` : lister ses abonnements (principal + secondaires).
 - `POST /api/subscriptions` : s’abonner à une paroisse (principal ou secondaire).
 - `DELETE /api/subscriptions/{code}` : se désabonner d’une paroisse secondaire.
 
 **Endpoints administrateur**
 - `GET /api/admin/users` : lister les utilisateurs avec filtres (actifs, paroisse, rôle).
 - `GET /api/admin/users/{code}` : consulter le détail d’un utilisateur.
 - `PATCH /api/admin/users/{code}` : modifier le profil ou l’état d’un utilisateur.
 - `GET /api/admin/roles` : lister les rôles disponibles.
 - `POST /api/admin/roles` : créer un rôle.
 - `PUT /api/admin/roles/{code}` : mettre à jour un rôle.
 - `DELETE /api/admin/roles/{code}` : supprimer un rôle.
 - `GET /api/admin/roles/{code}/permissions` : voir les permissions d’un rôle.
 - `PUT /api/admin/roles/{code}/permissions` : associer/dissocier des permissions.
 - `GET /api/admin/subscriptions` : vue globale des abonnements par paroisse.
 
 ---
 
## parish-service (paroisses / unités ecclésiales)
**Tables utilisées**
- `unite_ecclesiale` : paroisses et unités ecclésiales (coordonnées, métadonnées).

**Endpoints utilisateur (fidèle)**
- `GET /api/parishes` : lister les paroisses avec recherche/filtre.
- `GET /api/parishes/{code}` : détail d’une paroisse (adresse, description, GPS).
- `GET /api/unites-ecclesiales` : lister les unités ecclésiales.
- `GET /api/unites-ecclesiales/{code}` : détail d’une unité ecclésiale.

**Endpoints administrateur**
- `POST /api/admin/parishes` : créer une paroisse (nom, coordonnées, description).
- `PUT /api/admin/parishes/{code}` : mettre à jour les informations d’une paroisse.
- `DELETE /api/admin/parishes/{code}` : supprimer une paroisse.
- `POST /api/admin/unites-ecclesiales` : créer une unité ecclésiale.
- `PUT /api/admin/unites-ecclesiales/{code}` : mettre à jour une unité ecclésiale.
- `DELETE /api/admin/unites-ecclesiales/{code}` : supprimer une unité ecclésiale.
 
 ---
 
 ## communication-service (communiqués paroissiaux)
 **Tables utilisées**
 - `communiques_paroissiaux` : annonces/communiqués.
 - `type_communique` : typologies (messe, formation, festivités).
 - `piece_jointe_communique` : images/documents liés.
 - `unite_ecclesiale` (référence) : paroisse ciblée.
 
 **Endpoints utilisateur (fidèle)**
 - `GET /api/communiques` : lister les communiqués (filtre par type, dates, paroisse).
 - `GET /api/communiques/{code}` : détail d’un communiqué (texte + pièces jointes).
 - `GET /api/communiques/types` : lister les types de communiqués.
 
 **Endpoints administrateur**
 - `POST /api/admin/communiques` : créer un communiqué ciblé (paroisse/groupe/global).
 - `PUT /api/admin/communiques/{code}` : modifier un communiqué.
 - `DELETE /api/admin/communiques/{code}` : supprimer un communiqué.
 - `POST /api/admin/communiques/{code}/attachments` : ajouter une pièce jointe.
 - `DELETE /api/admin/communiques/{code}/attachments/{pj}` : retirer une pièce jointe.
 - `POST /api/admin/notifications/communiques/{code}` : déclencher une notification push.
 
 **Note** : la notification push est envoyée via un service externe (ex. FCM) ;
 le schéma SQL ne prévoit pas de table de stockage des notifications.
 
 ---
 
 ## worship-service (horaires des messes, confessions, intentions)
 **Tables utilisées**
 - `celebration_eucharistique` : types et occurrences de célébrations.
 - `programme_paroissial` : programmes paroissiaux liés aux célébrations.
 - `programme_celebration_eucharistique` : lien programme ↔ célébration.
 - `confessions` : créneaux et lieux des confessions.
 - `intention_messe` : intentions et versements associés aux messes.
 - `unite_ecclesiale` (référence) : paroisses.
 
 **Endpoints utilisateur (fidèle)**
 - `GET /api/messes` : lister les horaires des messes (jour, type, lieu, paroisse).
 - `GET /api/messes/{code}` : détail d’un horaire de messe.
 - `GET /api/messes/types` : lister les types de messes.
 - `GET /api/confessions` : lister les horaires de confession (lieu, créneau).
 - `GET /api/confessions/{code}` : détail d’un créneau de confession.
 - `GET /api/intentions` : consulter les intentions associées à une messe.
 
 **Endpoints administrateur**
 - `POST /api/admin/messes` : créer un horaire de messe (récurrence, type, lieu).
 - `PUT /api/admin/messes/{code}` : modifier un horaire.
 - `DELETE /api/admin/messes/{code}` : supprimer un horaire.
 - `POST /api/admin/confessions` : créer un créneau de confession.
 - `PUT /api/admin/confessions/{code}` : modifier un créneau.
 - `DELETE /api/admin/confessions/{code}` : supprimer un créneau.
 - `POST /api/admin/intentions` : enregistrer une intention de messe.
 - `PUT /api/admin/intentions/{code}` : modifier une intention.
 - `DELETE /api/admin/intentions/{code}` : supprimer une intention.
 
 ---
 
 ## activity-service (activités paroissiales)
 **Tables utilisées**
 - `activite` : activités paroissiales (date, lieu, fréquence, statut).
 - `type_activite` : catégories d’activités.
 - `unite_ecclesiale` (référence) : paroisses.
 
 **Endpoints utilisateur (fidèle)**
 - `GET /api/activites` : lister les activités (paroisse, type, période).
 - `GET /api/activites/{code}` : détail d’une activité.
 - `GET /api/activites/types` : lister les types d’activités.
 
 **Endpoints administrateur**
 - `POST /api/admin/activites` : créer une activité.
 - `PUT /api/admin/activites/{code}` : modifier une activité.
 - `DELETE /api/admin/activites/{code}` : supprimer une activité.
 - `POST /api/admin/activites/types` : créer un type d’activité.
 - `PUT /api/admin/activites/types/{code}` : modifier un type d’activité.
 - `DELETE /api/admin/activites/types/{code}` : supprimer un type d’activité.
 
 ---
 
 ## content-service (oratoires, lieux spirituels, objets saints)
 **Tables utilisées**
 - `lieux_saint` : oratoires/lieux spirituels (adresse, horaires, description, photo).
 - `objets_sacres` : objets saints (description, photo, localisation).
 - `unite_ecclesiale` (référence) : paroisses.
 
 **Endpoints utilisateur (fidèle)**
 - `GET /api/lieux-saints` : lister les lieux spirituels avec localisation.
 - `GET /api/lieux-saints/{code}` : détail d’un lieu (adresse, horaires, photos).
 - `GET /api/objets-sacres` : lister les objets saints disponibles.
 - `GET /api/objets-sacres/{code}` : détail d’un objet (usage, lieu).
 
 **Endpoints administrateur**
 - `POST /api/admin/lieux-saints` : créer un lieu spirituel.
 - `PUT /api/admin/lieux-saints/{code}` : modifier un lieu spirituel.
 - `DELETE /api/admin/lieux-saints/{code}` : supprimer un lieu spirituel.
 - `POST /api/admin/objets-sacres` : enregistrer un objet sacré.
 - `PUT /api/admin/objets-sacres/{code}` : modifier un objet sacré.
 - `DELETE /api/admin/objets-sacres/{code}` : supprimer un objet sacré.
 
 ---
 
 ## api-documentation-service (documentation)
 **Tables utilisées**
 - Aucune (service d’agrégation/présentation des docs).
 
 **Endpoints utilisateur (fidèle)**
 - `GET /api/docs/urls` : liste structurée des URLs Swagger par service.
 
 **Endpoints administrateur**
 - Identique à l’utilisateur (documentation publique).
 
 ---
 
 ## gateway-service (API Gateway)
 **Tables utilisées**
 - Aucune.
 
 **Endpoints utilisateur (fidèle)**
 - `GET /swagger-ui.html` : interface Swagger agrégée.
 - `GET /{service}/v3/api-docs` : accès aux OpenAPI des services.
 
 **Endpoints administrateur**
 - Identique à l’utilisateur (documentation publique).
 
 ---
 
 ## discovery-service / config-service
 **Tables utilisées**
 - Aucune (services d’infrastructure).
 
 **Endpoints utilisateur / administrateur**
 - Pas d’API métier exposée ; uniquement endpoints techniques (actuator).
