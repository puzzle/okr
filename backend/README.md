# OKR-Tool Backend

This is the Spring Boot backend for the OKR-Tool.

## DEVELOPING

Formatting:

- Check code formatting: `mvn spotless:check`
- Format the code: `mvn spotless:apply`

We also run the formatter with `spotless:apply` in the `compile` goal.

Test and Run:

For the backend to run you have to start the Database and the Keycloak services. [Start DB and Keycloak](../docker/README.md)

- You can run the backend with a run config found in the `.run` folder
- Verify the Backend for coverage check: `mvn clean verify`
- Run backend Tests: `mvn test`

Build:

- Build backend without frontend: `mvn clean package`

## Formatting
We use the ***spotless*** Plugin for formatting the Java code:
https://github.com/diffplug/spotless

### How to update the spotless configuration using intelij:
- Open `Go to Settings -> Editor -> Code styles -> Java` 
- Select the default project config and export it to a file
- Then make the changes you want to the code style config ***HIT APPLY*** and then export it to a file
- Then run the following command `git --no-pager diff --no-index -U0 default.xml changed.xml | egrep '^\+' | diff-so-fancy` to see the changes
- then copy all additions to the formatter file in the backend project

