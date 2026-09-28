#!/usr/bin/env bash
# Corre db/seed-dev.sql contra el Postgres del docker-compose, vinculando las
# personas del seed con los usuarios `admin` y `basico` de Keycloak.
set -euo pipefail

KC_URL="${KEYCLOAK_SERVER_URL:-http://localhost:8081}"
KC_REALM="monitoreo-servicios"
KC_ADMIN_USER="${KC_ADMIN_USER:-admin}"
KC_ADMIN_PASSWORD="${KC_ADMIN_PASSWORD:-admin}"

cd "$(dirname "$0")"

token=$(curl -sf "$KC_URL/realms/master/protocol/openid-connect/token" \
  -d client_id=admin-cli -d grant_type=password \
  -d username="$KC_ADMIN_USER" -d password="$KC_ADMIN_PASSWORD" \
  | python3 -c 'import sys, json; print(json.load(sys.stdin)["access_token"])') || {
  echo "No se pudo obtener token de Keycloak en $KC_URL; las personas quedan sin keycloak_id" >&2
  token=""
}

kc_id() {
  [ -z "$token" ] && return 0
  curl -sf -H "Authorization: Bearer $token" \
    "$KC_URL/admin/realms/$KC_REALM/users?username=$1&exact=true" \
    | python3 -c 'import sys, json; u = json.load(sys.stdin); print(u[0]["id"] if u else "")'
}

kc_admin=$(kc_id admin)
kc_basico=$(kc_id basico)
echo "keycloak admin=${kc_admin:-<ninguno>} basico=${kc_basico:-<ninguno>}"

docker exec -i monitoreo-postgres psql -U tp -d tp -v ON_ERROR_STOP=1 \
  -v kc_admin="$kc_admin" -v kc_basico="$kc_basico" < seed-dev.sql
