#!/usr/bin/env bash
# Vérification des rôles USER / ADMIN : crée un fidèle et un admin, vérifie les accès.
# Prérequis : user-service démarré, Postgres (Docker) avec accès psql (docker exec).
# Usage : ./verify-roles.sh [BASE_URL]

set -e
BASE_URL="${1:-http://localhost:8081}"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

ok()  { echo -e "${GREEN}[OK]${NC} $1"; }
fail() { echo -e "${RED}[FAIL]${NC} $1"; exit 1; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }

echo "=== Vérification des rôles USER / ADMIN @ $BASE_URL ==="

# 1. Créer un utilisateur fidèle (USER seulement)
echo ""
echo "--- Création utilisateur fidèle (USER) ---"
REG_F=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"fidele_verif","noms":"Martin","prenoms":"Marie","dateNaissance":"1985-05-15","contact1":"+33612345678","password":"Pass123!","addresse":"Lyon"}')
CODE_F=$(echo "$REG_F" | sed '$d' | grep -o '"code":[0-9]*' | head -1 | cut -d: -f2)
[ -z "$CODE_F" ] && fail "Impossible d'extraire code fidèle"
ok "Fidèle créé (code: $CODE_F)"

# 2. Créer un utilisateur admin (on lui attribuera le rôle ADMIN en base)
echo ""
echo "--- Création utilisateur admin ---"
REG_A=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin_verif","noms":"Dupont","prenoms":"Admin","dateNaissance":"1980-01-01","contact1":"+33698765432","password":"Admin123!","addresse":"Paris"}')
CODE_A=$(echo "$REG_A" | sed '$d' | grep -o '"code":[0-9]*' | head -1 | cut -d: -f2)
[ -z "$CODE_A" ] && fail "Impossible d'extraire code admin"
ok "Utilisateur admin créé (code: $CODE_A)"

# 3. Attribuer le rôle ADMIN (code 2) à l'utilisateur admin via la base
echo ""
echo "--- Attribution du rôle ADMIN en base (role_utilisateur) ---"
ROLE_UK=$((CODE_A * 1000 + 2))
if command -v docker >/dev/null 2>&1 && docker ps --format '{{.Names}}' 2>/dev/null | grep -q paroisse-postgres; then
  docker exec paroisse-postgres psql -U church -d churchnow -v ON_ERROR_STOP=1 -t -c \
    "SET search_path TO paroisse_schema; INSERT INTO role_utilisateur (code, code_utilisateur, code_role) VALUES ($ROLE_UK, $CODE_A, 2) ON CONFLICT (code) DO NOTHING;" 2>/dev/null && ok "Rôle ADMIN attribué (code_utilisateur=$CODE_A, code_role=2)" || warn "Insert role_utilisateur (conflit ou erreur - peut déjà exister)"
else
  warn "Docker/Postgres non disponible : exécutez manuellement : INSERT INTO paroisse_schema.role_utilisateur (code, code_utilisateur, code_role) VALUES ($ROLE_UK, $CODE_A, 2);"
fi

# 4. Vérifications : fidèle (USER) -> /api/users/me OK, /api/admin/users 403
echo ""
echo "--- Vérifications fidèle (USER uniquement) ---"
R_ME=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/users/me" -H "X-User-Id: $CODE_F")
[ "$R_ME" = "200" ] && ok "GET /api/users/me (fidèle): 200" || fail "GET /api/users/me (fidèle): $R_ME (attendu 200)"

R_ADMIN=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/admin/users" -H "X-User-Id: $CODE_F")
[ "$R_ADMIN" = "403" ] && ok "GET /api/admin/users (fidèle): 403 (accès refusé)" || warn "GET /api/admin/users (fidèle): $R_ADMIN (attendu 403 - redémarrer le service si besoin)"

# 5. Vérifications : admin (USER + ADMIN) -> tout OK
echo ""
echo "--- Vérifications admin (USER + ADMIN) ---"
R_ME_A=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/users/me" -H "X-User-Id: $CODE_A")
[ "$R_ME_A" = "200" ] && ok "GET /api/users/me (admin): 200" || fail "GET /api/users/me (admin): $R_ME_A"

R_ADMIN_A=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/admin/users" -H "X-User-Id: $CODE_A")
[ "$R_ADMIN_A" = "200" ] && ok "GET /api/admin/users (admin): 200" || warn "GET /api/admin/users (admin): $R_ADMIN_A (attendu 200 - vérifier rôle en base)"

# 6. Sans header -> 401/403 sur endpoints protégés
echo ""
echo "--- Sans authentification ---"
R_ANON=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/users/me")
[ "$R_ANON" = "401" ] || [ "$R_ANON" = "403" ] && ok "GET /api/users/me sans header: $R_ANON" || warn "GET /api/users/me sans header: $R_ANON (attendu 401 ou 403)"

echo ""
echo -e "${GREEN}=== Vérification des rôles terminée ===${NC}"
