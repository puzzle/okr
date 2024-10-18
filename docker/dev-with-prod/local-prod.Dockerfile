FROM alpine:3.19

USER root

RUN apk update && apk add --upgrade curl && apk --no-cache add openjdk17 inotify-tools

RUN adduser --home /app-root --uid 1001 --disabled-password okr
USER 1001

WORKDIR app-root/backend

ENTRYPOINT ["/bin/sh", "-c", "export BACKEND_VERSION=$(find . -type f -name 'backend-*.jar' -print -quit | sed -n 's/.*backend-\\(.*\\)\\.jar/\\1/p'); if ! unzip -p backend-${BACKEND_VERSION}.jar META-INF/MANIFEST.MF | grep -q 'Main-Class:'; then echo 'Error: no main manifest attribute, exiting.'; exit 1; fi; java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005 -jar backend-${BACKEND_VERSION}.jar  & pid=$!; while true; do inotifywait -e modify backend-${BACKEND_VERSION}.jar; exit 1; done"]