FROM alpine:3.20

USER root

RUN apk update && apk add --upgrade curl && apk --no-cache add openjdk17 inotify-tools

RUN adduser --home /app-root --uid 1001 --disabled-password okr
USER 1001

WORKDIR app-root/backend

ENTRYPOINT ["/bin/sh", "-c", "export BACKEND_VERSION=$(find . -type f -name 'backend-*.jar' -print -quit | sed -n 's/.*backend-\\(.*\\)\\.jar/\\1/p'); while true; do java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005 -jar backend-${BACKEND_VERSION}.jar  & pid=$!; inotifywait -e modify backend-${BACKEND_VERSION}.jar; kill -9 $pid; echo 'JAR updated. Restarting...'; done"]