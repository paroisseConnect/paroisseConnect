CREATE SCHEMA IF NOT EXISTS paroisse_schema;

CREATE TABLE IF NOT EXISTS paroisse_schema.type_communique (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paroisse_schema.unite_ecclesiale (
	code BIGINT PRIMARY KEY,
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
	code_modificateur BIGINT
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

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS contenu TEXT;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS date_publication TIMESTAMP;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS date_debut_affichage TIMESTAMP;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS date_fin_affichage TIMESTAMP;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS actif BOOLEAN;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS date_creation TIMESTAMP;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS date_modification TIMESTAMP;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS code_createur BIGINT;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS code_modificateur BIGINT;

ALTER TABLE paroisse_schema.communiques_paroissiaux
	ADD COLUMN IF NOT EXISTS code_unite_ecclesiale BIGINT;

DO $$
BEGIN
	IF NOT EXISTS (
		SELECT 1
		FROM pg_constraint
		WHERE conname = 'fk_communique_type'
	) THEN
		ALTER TABLE paroisse_schema.communiques_paroissiaux
			ADD CONSTRAINT fk_communique_type
			FOREIGN KEY (code_type)
			REFERENCES paroisse_schema.type_communique (code);
	END IF;

	IF NOT EXISTS (
		SELECT 1
		FROM pg_constraint
		WHERE conname = 'fk_communique_unite'
	) THEN
		ALTER TABLE paroisse_schema.communiques_paroissiaux
			ADD CONSTRAINT fk_communique_unite
			FOREIGN KEY (code_unite_ecclesiale)
			REFERENCES paroisse_schema.unite_ecclesiale (code);
	END IF;

	IF NOT EXISTS (
		SELECT 1
		FROM pg_constraint
		WHERE conname = 'fk_piece_jointe_communique'
	) THEN
		ALTER TABLE paroisse_schema.piece_jointe_communique
			ADD CONSTRAINT fk_piece_jointe_communique
			FOREIGN KEY (code_communique)
			REFERENCES paroisse_schema.communiques_paroissiaux (code)
			ON DELETE CASCADE;
	END IF;
END $$;
