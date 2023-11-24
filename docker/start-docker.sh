# Replace variables with your actual Keycloak server details
KEYCLOAK_URL=http://localhost:8544
REALM_NAME=pitc
CLIENT_ID=register-new-user
USERNAME=test
PASSWORD=aa
CLIENT_SECRET=secret

# Authenticate to get an access token
RESPONSE=$(curl -X POST "${KEYCLOAK_URL}/realms/pitc/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=${USERNAME}" \
  -d "password=${PASSWORD}" \
  -d "client_id=${CLIENT_ID}" \
  -d "client_secret=${CLIENT_SECRET}" \
  -d "grant_type=password")

echo "${RESPONSE}"

# Create a test user
#curl -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
#  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
#  -H "Content-Type: application/json" \
#  -d '{
#    "username": "testuser",
#    "email": "testuser@example.com",
#    "enabled": true,
#    "credentials": [{
#      "type": "password",
#      "value": "password123",
#      "temporary": false
#    }]
#  }'
