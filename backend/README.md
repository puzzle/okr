# OKR-Tool Backend

This is the Spring Boot backend for the OKR-Tool.

## DEVELOPING

Build only the Backend
- Build backend without frontend: `mvn clean package`


Build Frontend + Backend together

NATIVE
- Build Frontend for production im `frontend` dir: `npm run build`
- Build Backend with frontend: `mvn clean package -P build-for-docker`
- Setup DB
- Start Backend `java -jar {path to .jar file}`

USING DOCKER
- cd into the root directory of the project
- cd `docker/local-prod`
- Run `docker compose up`
- You have to restart after every code change `docker compose down spring && docker compose up spring`
- Get the logs with `docker compose logs -f spring`
<br>

***!IMPORTANT!***
- If after the first start the backend is not reachable, restart the backend container with `docker compose restart spring`
- if any permission issues occur, delete all of the following folders
  - .angular
  - frontend/dist
  - frontend/node_modules
  - backend/target

Formatting:
- Check code formatting: `mvn formatter:validate`
- Format the code: `mvn formatter:format`

The `compile` goal execute also a `formatter:format` goal.

Verify the Backend for coverage check:
- `mvn clean verify`

## Formatting
We use the **formatter-maven-plugin** Plugin for formatting the Java code:
https://code.revelc.net/formatter-maven-plugin/

## Build
_tbd_
