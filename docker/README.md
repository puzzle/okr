## Prepare keycloak setup

```shell
cd docker

# Change perms of mounted volume
chmod 0777 ./keycloak_data_*

# Append to hosts file 
sudo sh -c 'echo "127.0.0.1       keycloakIdp\n127.0.0.1       keycloakBroker" >> /etc/hosts'f
```
