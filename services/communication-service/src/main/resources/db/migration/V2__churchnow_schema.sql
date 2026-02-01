CREATE SCHEMA IF NOT EXISTS paroisse_schema;

CREATE TABLE IF NOT EXISTS paroisse_schema.abonnement_unite_ecclesiale (
	code BIGINT NOT NULL,
	code_unite_ecclesiale BIGINT,
	code_utilisateur BIGINT,
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.activite (
	code BIGINT NOT NULL,
	libelle VARCHAR(255),
	date_activite VARCHAR(255),
	description VARCHAR(255),
	frequence VARCHAR(255),
	date_debut VARCHAR(255),
	date_fin VARCHAR(255),
	statut INTEGER,
	lieu VARCHAR(255),
	code_unite_ecclesiale BIGINT,
	code_type_activite BIGINT,
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.celebration_eucharistique (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	granularite VARCHAR(255),
	code_horaire VARCHAR(255),
	code_type VARCHAR(255),
	code_unite_ecclesiale BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.communiques_paroissiaux (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	code_type BIGINT,
	code_unite_ecclesiale BIGINT,
	contenu TEXT,
	date_publication TIMESTAMP,
	date_debut_affichage TIMESTAMP,
	date_fin_affichage TIMESTAMP,
	actif BOOLEAN,
	date_creation TIMESTAMP,
	date_modification TIMESTAMP,
	code_createur BIGINT,
	code_modificateur BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.piece_jointe_communique (
	code BIGSERIAL PRIMARY KEY,
	code_communique BIGINT NOT NULL,
	type VARCHAR(50),
	url VARCHAR(500),
	nom_fichier VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.confessions (
	code BIGSERIAL PRIMARY KEY,
	lieu VARCHAR(255),
	horaire_debut VARCHAR(255),
	horaire_fin VARCHAR(255),
	granularite VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.intention_messe (
	code BIGSERIAL PRIMARY KEY,
	intention_messe VARCHAR(1000),
	code_celebration_eucharistique BIGINT,
	montant_verse VARCHAR(255),
	date_versement VARCHAR(255),
	date_creation VARCHAR(255),
	date_modification VARCHAR(255),
	code_createur BIGINT,
	code_modificateur BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.langue_liturgique (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.lieux_saint (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	adresse VARCHAR(255),
	photo VARCHAR(255),
	code_unite_ecclesiale BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.objets_sacres (
	code BIGINT NOT NULL,
	libelle VARCHAR(255),
	description VARCHAR(255),
	photo VARCHAR(255),
	date_creation VARCHAR(255),
	date_modification VARCHAR(255),
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.permissions (
	code BIGINT NOT NULL,
	libelle VARCHAR(255),
	code_createur VARCHAR(255),
	code_modificateur VARCHAR(255),
	date_creation VARCHAR(255),
	date_modification VARCHAR(255),
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.programme_celebration_eucharistique (
	code BIGSERIAL PRIMARY KEY,
	code_programme BIGINT,
	code_celebration_eucharistique BIGINT,
	description VARCHAR(255),
	code_createur BIGINT,
	code_modificateur BIGINT,
	date_creation TIMESTAMP,
	date_modification TIMESTAMP,
	jour_semaine INTEGER,
	date_exception DATE
);

CREATE TABLE IF NOT EXISTS paroisse_schema.programme_paroissial (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	date_debut DATE,
	date_fin DATE
);

CREATE TABLE IF NOT EXISTS paroisse_schema.role_permission (
	code BIGSERIAL PRIMARY KEY,
	code_role BIGINT,
	code_permission BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.role_utilisateur (
	code BIGINT NOT NULL,
	code_utilisateur BIGINT,
	code_role BIGINT,
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.roles (
	code BIGINT NOT NULL,
	libelle VARCHAR(255),
	code_createur VARCHAR(255),
	code_modificateur VARCHAR(255),
	date_creation VARCHAR(255),
	date_modification VARCHAR(255),
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.type_activite (
	code BIGINT,
	libelle VARCHAR(255),
	description VARCHAR(255),
	date_creation VARCHAR(255),
	code_createur BIGINT,
	code_modificateur BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.type_communique (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.unite_ecclesiale (
	code BIGINT NOT NULL,
	libelle VARCHAR(255),
	description VARCHAR(255),
	titre VARCHAR(255),
	adresse VARCHAR(255),
	code_type BIGINT,
	longitude VARCHAR(255),
	latitude VARCHAR(255),
	altitude VARCHAR(255),
	fidele VARCHAR(255),
	date_creation_unite TIMESTAMP,
	statut_canonique VARCHAR(255),
	code_unite_parent BIGINT,
	activite VARCHAR(255),
	date_creation TIMESTAMP,
	date_modification TIMESTAMP,
	code_createur BIGINT,
	code_modificateur BIGINT,
	PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.utilisateurs (
	code BIGSERIAL PRIMARY KEY,
	username VARCHAR(255),
	noms VARCHAR(255),
	prenoms VARCHAR(255),
	date_naissance VARCHAR(255),
	contact_1 VARCHAR(255),
	contact_2 VARCHAR(255),
	password VARCHAR(255),
	addresse VARCHAR(255),
	code_createur VARCHAR(255),
	code_modificateur VARCHAR(255),
	date_creation VARCHAR(255),
	date_modification VARCHAR(255)
);
