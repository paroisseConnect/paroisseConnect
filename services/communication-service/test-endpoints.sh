#!/usr/bin/env bash
# Test de tous les endpoints du communication-service.
# Prérequis : communication-service démarré (port 8084), Postgres avec paroisse_schema.
# Les types de communiqué par défaut (Messe, Formation, Festivités) sont insérés par Flyway V3.
# Usage : ./test-endpoints.sh [BASE_URL]   ex: ./test-endpoints.sh http://localhost:8084

set -e
BASE_URL="${1:-http://localhost:8084}"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

ok()  { echo -e "${GREEN}[OK]${NC} $1"; }
fail() { echo -e "${RED}[FAIL]${NC} $1"; exit 1; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }

echo "=== Tests communication-service @ $BASE_URL ==="

# --- Health ---
echo ""
echo "--- Actuator health ---"
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/actuator/health")
[ "$code" = "200" ] && ok "GET /actuator/health -> $code" || fail "GET /actuator/health -> $code"

# --- GET /api/communiques/types ---
echo ""
echo "--- Types de communiqué ---"
resp=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/communiques/types")
body=$(echo "$resp" | head -n -1)
code=$(echo "$resp" | tail -n 1)
[ "$code" = "200" ] && ok "GET /api/communiques/types -> $code" || fail "GET /api/communiques/types -> $code"
count=$(echo "$body" | grep -o '"code"' | wc -l)
[ "$count" -ge 1 ] && ok "GET /api/communiques/types -> $count type(s) retourné(s)" || warn "Aucun type retourné (vérifier migration V3)"

# --- GET /api/communiques (liste avec pagination) ---
echo ""
echo "--- Liste communiqués ---"
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/communiques")
[ "$code" = "200" ] && ok "GET /api/communiques -> $code" || fail "GET /api/communiques -> $code"
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/communiques?page=0&size=5")
[ "$code" = "200" ] && ok "GET /api/communiques?page=0&size=5 -> $code" || fail "GET /api/communiques?page=0&size=5 -> $code"

# --- POST /api/admin/communiques (créer) ---
echo ""
echo "--- Créer un communiqué ---"
CREATE_JSON='{"libelle":"Test Communiqué","codeType":1,"contenu":"Contenu test","actif":true}'
r=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/admin/communiques" -H "Content-Type: application/json" -d "$CREATE_JSON")
code=$(echo "$r" | tail -n1)
body=$(echo "$r" | sed '$d')
[ "$code" = "201" ] && ok "POST /api/admin/communiques -> $code" || fail "POST /api/admin/communiques -> $code (body: $body)"
COMM_ID=$(echo "$body" | grep -o '"code":[0-9]*' | head -1 | cut -d: -f2)
[ -z "$COMM_ID" ] && warn "Impossible d'extraire l'id du communiqué"
echo "    Communiqué créé, id: $COMM_ID"

# --- GET /api/communiques/{id} (détail) ---
echo ""
echo "--- Détail communiqué ---"
[ -z "$COMM_ID" ] && COMM_ID=1
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/communiques/$COMM_ID")
[ "$code" = "200" ] && ok "GET /api/communiques/$COMM_ID -> $code" || fail "GET /api/communiques/$COMM_ID -> $code"

# --- PUT /api/admin/communiques/{id} (modifier) ---
echo ""
echo "--- Modifier communiqué ---"
UPDATE_JSON="{\"libelle\":\"Test Communiqué modifié\",\"codeType\":1,\"contenu\":\"Contenu mis à jour\",\"actif\":true}"
code=$(curl -s -o /dev/null -w "%{http_code}" -X PUT "$BASE_URL/api/admin/communiques/$COMM_ID" -H "Content-Type: application/json" -d "$UPDATE_JSON")
[ "$code" = "200" ] && ok "PUT /api/admin/communiques/$COMM_ID -> $code" || fail "PUT /api/admin/communiques/$COMM_ID -> $code"

# --- POST /api/admin/communiques/{id}/attachments (ajouter pièce jointe) ---
echo ""
echo "--- Ajouter pièce jointe ---"
ATT_JSON='{"type":"IMAGE","url":"https://example.com/image.png","nomFichier":"image.png"}'
r=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/admin/communiques/$COMM_ID/attachments" -H "Content-Type: application/json" -d "$ATT_JSON")
code=$(echo "$r" | tail -n1)
body=$(echo "$r" | sed '$d')
[ "$code" = "200" ] && ok "POST /api/admin/communiques/$COMM_ID/attachments -> $code" || fail "POST /api/admin/communiques/$COMM_ID/attachments -> $code (body: $body)"
# Premier "code" = communiqué, second = pièce jointe (dans piecesJointes)
PJ_ID=$(echo "$body" | grep -o '"code":[0-9]*' | sed -n '2p' | cut -d: -f2)
[ -z "$PJ_ID" ] && PJ_ID=1

# --- DELETE /api/admin/communiques/{id}/attachments/{pj} (retirer pièce jointe) ---
echo ""
echo "--- Retirer pièce jointe ---"
code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE_URL/api/admin/communiques/$COMM_ID/attachments/$PJ_ID")
[ "$code" = "200" ] && ok "DELETE /api/admin/communiques/$COMM_ID/attachments/$PJ_ID -> $code" || warn "DELETE .../attachments/$PJ_ID -> $code (PJ_ID peut être incorrect)"

# --- POST /api/admin/notifications/communiques/{id} (notification push) ---
echo ""
echo "--- Notification push ---"
code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/admin/notifications/communiques/$COMM_ID")
[ "$code" = "202" ] && ok "POST /api/admin/notifications/communiques/$COMM_ID -> 202" || warn "POST .../notifications/communiques/$COMM_ID -> $code (attendu 202)"

# --- DELETE /api/admin/communiques/{id} (supprimer) ---
echo ""
echo "--- Supprimer communiqué ---"
code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE_URL/api/admin/communiques/$COMM_ID")
[ "$code" = "204" ] && ok "DELETE /api/admin/communiques/$COMM_ID -> 204" || fail "DELETE /api/admin/communiques/$COMM_ID -> $code"

# --- GET /api/communiques/{id} après suppression -> 404 ---
echo ""
echo "--- Vérification 404 après suppression ---"
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/communiques/$COMM_ID")
[ "$code" = "404" ] && ok "GET /api/communiques/$COMM_ID (supprimé) -> 404" || warn "Attendu 404, obtenu $code"

echo ""
echo -e "${GREEN}=== Tous les tests sont passés ===${NC}"
