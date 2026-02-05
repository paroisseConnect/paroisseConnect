-- Types de communiqué par défaut pour les tests et la production
INSERT INTO paroisse_schema.type_communique (code, libelle, description) VALUES
	(1, 'Messe', 'Horaires et annonces de messes'),
	(2, 'Formation', 'Formations et catéchèse'),
	(3, 'Festivités', 'Événements et festivités paroissiales')
ON CONFLICT (code) DO NOTHING;

SELECT setval(
	pg_get_serial_sequence('paroisse_schema.type_communique', 'code'),
	(SELECT COALESCE(MAX(code), 1) FROM paroisse_schema.type_communique)
);
