-- Rôles par défaut pour le contrôle d'accès (USER, ADMIN)
-- USER : tout fidèle authentifié ; ADMIN : équipe paroissiale (gestion utilisateurs, rôles, abonnements)
INSERT INTO paroisse_schema.roles (code, libelle) VALUES (1, 'USER'), (2, 'ADMIN')
ON CONFLICT (code) DO NOTHING;
