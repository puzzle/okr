FROM alpine:3.16

USER root

RUN apk update && apk add --upgrade curl && apk --no-cache add openjdk17

RUN adduser --home /app-root --uid 1001 --disabled-password okr
USER 1001

WORKDIR app-root/backend

COPY --chown=1001 backend/target .

ENTRYPOINT  ["java", "-jar", "backend-0.1.0-SNAPSHOT.jar"]
