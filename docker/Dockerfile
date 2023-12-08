FROM alpine:3.19

USER root

RUN apk update && apk add --upgrade curl && apk --no-cache add openjdk17

RUN adduser --home /app-root --uid 1001 --disabled-password okr
USER 1001

WORKDIR app-root/backend

COPY --chown=1001 backend/target .

ENTRYPOINT ["/bin/sh", "-c", "export BACKEND_VERSION=$(find . -type f -name 'backend-*.jar' -print -quit | sed -n 's/.*backend-\\(.*\\)\\.jar/\\1/p'); java -jar backend-${BACKEND_VERSION}.jar"]