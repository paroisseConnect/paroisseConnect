#!/usr/bin/env bash
# Script de test des endpoints du user-service (comme Postman).
# Prérequis : user-service démarré (ex: docker-compose up -d postgres discovery-service config-service user-service)
# Via gateway (JWT) : BASE_URL=http://localhost:8088/user-service  -> utilise Authorization: Bearer <token>
# Direct (X-User-Id) : BASE_URL=http://localhost:8081               -> utilise X-User-Id

set -e
BASE_URL="${BASE_URL:-http://localhost:8081}"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

ok()  { echo -e "${GREEN}[OK]${NC} $1"; }
fail() { echo -e "${RED}[FAIL]${NC} $1"; exit 1; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }

# Via gateway = utiliser JWT Bearer ; direct = utiliser X-User-Id
USE_GATEWAY=false
case "$BASE_URL" in
  *8088*|*gateway* ) USE_GATEWAY=true ;;
esac

echo "=== Tests user-service @ $BASE_URL (mode: $([ "$USE_GATEWAY" = true ] && echo 'gateway/JWT' || echo 'direct/X-User-Id') ) ==="

# --- Health ---
echo ""
echo "--- Actuator health ---"
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/actuator/health")
code=$(echo "$r" | tail -n1)
body=$(echo "$r" | sed '$d')
[ "$code" = "200" ] && ok "GET /actuator/health -> $code" || fail "GET /actuator/health -> $code (expected 200)"

# --- Auth : register ---
echo ""
echo "--- Auth : register ---"
REGISTER_JSON='{"username":"testuser'$(date +%s)'","noms":"Dupont","prenoms":"Jean","dateNaissance":"1990-01-01","contact1":"+33600000000","contact2":"","password":"Test123!","addresse":"Paris"}'
r=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/register" -H "Content-Type: application/json" -d "$REGISTER_JSON")
code=$(echo "$r" | tail -n1)
body=$(echo "$r" | sed '$d')
[ "$code" = "201" ] && ok "POST /api/auth/register -> $code" || fail "POST /api/auth/register -> $code (body: $body)"

# Extraire le code utilisateur pour les tests (ex: "code":1)
USER_CODE=$(echo "$body" | grep -o '"code":[0-9]*' | head -1 | cut -d: -f2)
[ -z "$USER_CODE" ] && warn "Impossible d'extraire code utilisateur du register"
echo "    User code: $USER_CODE"

# --- Auth : login ---
echo ""
echo "--- Auth : login ---"
USERNAME=$(echo "$REGISTER_JSON" | grep -o '"username":"[^"]*"' | cut -d'"' -f4)
LOGIN_JSON="{\"username\":\"$USERNAME\",\"password\":\"Test123!\"}"
r=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/login" -H "Content-Type: application/json" -d "$LOGIN_JSON")
code=$(echo "$r" | tail -n1)
body=$(echo "$r" | sed '$d')
[ "$code" = "200" ] && ok "POST /api/auth/login -> $code" || fail "POST /api/auth/login -> $code (body: $body)"

# Token JWT (pour mode gateway)
TOKEN=$(echo "$body" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
[ -z "$USER_CODE" ] && USER_CODE=1

# Header d'auth : gateway = Bearer JWT, direct = X-User-Id
auth_header() {
  if [ "$USE_GATEWAY" = true ] && [ -n "$TOKEN" ]; then
    echo "Authorization: Bearer $TOKEN"
  else
    echo "X-User-Id: $USER_CODE"
  fi
}
AUTH_H="$(auth_header)"

# --- Users : me (nécessite X-User-Id ou Bearer JWT) ---
echo ""
echo "--- Users : me ---"
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/users/me" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/users/me -> $code" || fail "GET /api/users/me -> $code"

# Sans header : doit échouer (400 en direct, 401 via gateway)
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/users/me")
code=$(echo "$r" | tail -n1)
[ "$code" = "400" ] || [ "$code" = "401" ] && ok "GET /api/users/me sans auth -> $code (attendu)" || warn "GET /api/users/me sans auth -> $code (attendu 400/401)"

# --- Users : update me ---
echo ""
echo "--- Users : update me ---"
PATCH_JSON='{"noms":"Dupont","prenoms":"Jean-Marie","contact1":"+33600000001"}'
r=$(curl -s -w "\n%{http_code}" -X PATCH "$BASE_URL/api/users/me" -H "Content-Type: application/json" -H "$AUTH_H" -d "$PATCH_JSON")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "PATCH /api/users/me -> $code" || fail "PATCH /api/users/me -> $code"

# --- Subscriptions : list ---
echo ""
echo "--- Subscriptions : list ---"
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/subscriptions" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/subscriptions -> $code" || fail "GET /api/subscriptions -> $code"

# --- Subscriptions : create (code arbitraire pour test) ---
echo ""
echo "--- Subscriptions : create ---"
SUB_CODE=$((USER_CODE * 1000 + 1))
SUB_JSON="{\"code\":$SUB_CODE,\"codeUniteEcclesiale\":1}"
r=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/subscriptions" -H "Content-Type: application/json" -H "$AUTH_H" -d "$SUB_JSON")
code=$(echo "$r" | tail -n1)
[ "$code" = "201" ] && ok "POST /api/subscriptions -> $code" || warn "POST /api/subscriptions -> $code (peut échouer si code_unite_ecclesiale=1 absent)"

# --- Subscriptions : delete (avec X-User-Id = sécurité) ---
echo ""
echo "--- Subscriptions : delete (avec X-User-Id) ---"
r=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/api/subscriptions/$SUB_CODE" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "204" ] && ok "DELETE /api/subscriptions/$SUB_CODE (X-User-Id) -> $code" || warn "DELETE /api/subscriptions/$SUB_CODE -> $code"

# Sans auth : doit échouer (400 ou 401)
r=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/api/subscriptions/99999")
code=$(echo "$r" | tail -n1)
[ "$code" = "400" ] || [ "$code" = "401" ] && ok "DELETE /api/subscriptions/xxx sans auth -> $code (attendu)" || warn "DELETE sans auth -> $code"

# --- Admin : users (via gateway : Bearer requis) ---
echo ""
echo "--- Admin : users ---"
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/admin/users" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/admin/users -> $code" || fail "GET /api/admin/users -> $code"

r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/admin/users/$USER_CODE" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/admin/users/$USER_CODE -> $code" || fail "GET /api/admin/users/{code} -> $code"

# --- Admin : subscriptions ---
echo ""
echo "--- Admin : subscriptions ---"
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/admin/subscriptions" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/admin/subscriptions -> $code" || fail "GET /api/admin/subscriptions -> $code"

# --- Admin : roles ---
echo ""
echo "--- Admin : roles ---"
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/admin/roles" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/admin/roles -> $code" || fail "GET /api/admin/roles -> $code"

# POST role (code 99 si pas utilisé)
r=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/admin/roles" -H "Content-Type: application/json" -H "$AUTH_H" -d '{"code":99,"libelle":"Role Test"}')
code=$(echo "$r" | tail -n1)
[ "$code" = "201" ] && ok "POST /api/admin/roles -> $code" || warn "POST /api/admin/roles -> $code"

# GET permissions d'un rôle
r=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/admin/roles/99/permissions" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "GET /api/admin/roles/99/permissions -> $code" || warn "GET /api/admin/roles/99/permissions -> $code"

# PUT permissions
r=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/api/admin/roles/99/permissions" -H "Content-Type: application/json" -H "$AUTH_H" -d '{"permissionCodes":[]}')
code=$(echo "$r" | tail -n1)
[ "$code" = "200" ] && ok "PUT /api/admin/roles/99/permissions -> $code" || warn "PUT /api/admin/roles/99/permissions -> $code"

# DELETE role
r=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/api/admin/roles/99" -H "$AUTH_H")
code=$(echo "$r" | tail -n1)
[ "$code" = "204" ] && ok "DELETE /api/admin/roles/99 -> $code" || warn "DELETE /api/admin/roles/99 -> $code"

echo ""
echo -e "${GREEN}=== Tous les tests principaux sont passés ===${NC}"
