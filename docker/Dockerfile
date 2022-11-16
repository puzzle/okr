FROM alpine:3.16

USER root

RUN apk update && apk add --upgrade curl && apk --no-cache add openjdk17

RUN adduser --home /app-root --uid 1001 --disabled-password poker
USER 1001

WORKDIR app-root

COPY --chown=1001 . .

#RUN ./mvnw clean package -P build-for-docker

ENTRYPOINT  ["java", "-jar", "backend/target/backend-0.1.0-SNAPSHOT.jar"]