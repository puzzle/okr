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
- Check code formatting: `mvn spotless:check`
- Format the code: `mvn spotless:apply`

We also run the formatter with `spotless:apply` in the `compile` goal.

Verify the Backend for coverage check:
- `mvn clean verify`

## Formatting
We use the ***spotless*** Plugin for formatting the Java code:
https://github.com/diffplug/spotless

### How to update the spotless configuration using intelij:
- Open `Go to Settings -> Editor -> Code styles -> Java` 
- Select the default project config and export it to a file
- Then make the changes you want to the code style config ***HIT APPLY*** and then export it to a file
- Then run the following command `git --no-pager diff --no-index -U0 default.xml changed.xml | egrep '^.[[[:digit:]]+m\+' | less -R | diff-so-fancy` to see the changes
-  
- then copy all additions to the formatter file in the backend project
## Build
_tbd_
