# OKR-Tool Backend

This is the Spring Boot backend for the OKR-Tool.

## DEVELOPING

Build only the Backend
- Build backend without frontend: `mvn clean package`

Build Frontend + Backend together
- Build Frontend for production im `frontend` dir: `npm run build`
- Build Backend with frontend: `mvn clean package -P build-for-docker`
- Setup DB
- Start Backend `java -jar {path to .jar file}`

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
