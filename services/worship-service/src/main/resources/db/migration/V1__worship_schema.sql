CREATE SCHEMA IF NOT EXISTS paroisse_schema;

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

CREATE TABLE IF NOT EXISTS paroisse_schema.celebration_eucharistique (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	granularite VARCHAR(255),
	code_horaire VARCHAR(255),
	code_type VARCHAR(255),
	code_unite_ecclesiale BIGINT
);

CREATE TABLE IF NOT EXISTS paroisse_schema.programme_paroissial (
	code BIGSERIAL PRIMARY KEY,
	libelle VARCHAR(255),
	date_debut DATE,
	date_fin DATE
);

CREATE TABLE IF NOT EXISTS paroisse_schema.programme_celebration_eucharistique (
	code BIGSERIAL PRIMARY KEY,
	code_programme BIGINT,
	code_celebration_eucharistique BIGINT,
	description VARCHAR(255),
	jour_semaine INTEGER,
	date_exception DATE,
	code_createur BIGINT,
	code_modificateur BIGINT,
	date_creation TIMESTAMP,
	date_modification TIMESTAMP
);

ALTER TABLE paroisse_schema.celebration_eucharistique
	ADD COLUMN IF NOT EXISTS code_unite_ecclesiale BIGINT;

ALTER TABLE paroisse_schema.programme_celebration_eucharistique
	ADD COLUMN IF NOT EXISTS jour_semaine INTEGER;

ALTER TABLE paroisse_schema.programme_celebration_eucharistique
	ADD COLUMN IF NOT EXISTS date_exception DATE;

DO $$
BEGIN
	IF NOT EXISTS (
		SELECT 1
		FROM pg_constraint
		WHERE conname = 'fk_celebration_unite'
	) THEN
		ALTER TABLE paroisse_schema.celebration_eucharistique
			ADD CONSTRAINT fk_celebration_unite
			FOREIGN KEY (code_unite_ecclesiale)
			REFERENCES paroisse_schema.unite_ecclesiale (code);
	END IF;

	IF NOT EXISTS (
		SELECT 1
		FROM pg_constraint
		WHERE conname = 'fk_programme_celebration_programme'
	) THEN
		ALTER TABLE paroisse_schema.programme_celebration_eucharistique
			ADD CONSTRAINT fk_programme_celebration_programme
			FOREIGN KEY (code_programme)
			REFERENCES paroisse_schema.programme_paroissial (code);
	END IF;

	IF NOT EXISTS (
		SELECT 1
		FROM pg_constraint
		WHERE conname = 'fk_programme_celebration_celebration'
	) THEN
		ALTER TABLE paroisse_schema.programme_celebration_eucharistique
			ADD CONSTRAINT fk_programme_celebration_celebration
			FOREIGN KEY (code_celebration_eucharistique)
			REFERENCES paroisse_schema.celebration_eucharistique (code);
	END IF;
END $$;
