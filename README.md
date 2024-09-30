# OKR-Tool

This is an open source application for managing OKRs, developed by the team of apprentices at Puzzle ITC.

This project contains two parts:

## Setup
Add subdomains to hosts file:
```shell
sudo sh -c 'echo "127.0.0.1       pitc.okr.localhost\n127.0.0.1       acme.okr.localhost" >> /etc/hosts'
```

## Frontend

[Frontend description](frontend/README.md)


## Backend
[Backend description](backend/README.md)

## Docker

Start docker container in docker folder.

Path to folder from repository root `cd docker`

Type `docker-compose up` in terminal to start up the docker container, `docker-compose down` to shut the container down.

## Users
All users PITC
```json
{
  "gl": {
    "username": "gl",
    "password": "gl",
    "name": "Jaya Norris"
  },
  "bl": {
    "username": "bl",
    "password": "bl",
    "name": "Esha Harris"
  },
  "bl-mob": {
    "username": "bl-mob",
    "password": "bl",
    "name": "BL Mobility"
  },
  "bl-mid": {
    "username": "bl-mid",
    "password": "bl",
    "name": "BL Mid"
  },
  "bl-sys": {
    "username": "bl-sys",
    "password": "bl",
    "name": "BL Sys"
  },
  "bl-ruby": {
    "username": "bl-ruby",
    "password": "bl",
    "name": "BL Ruby"
  },
  "bbt": {
    "username": "bbt",
    "password": "bbt",
    "name": "Ashleigh Russell"
  },
  "member": {
    "username": "member",
    "password": "member",
    "name": "Abraham Woodard"
  }
}
```
All users ACME
```json
{
  "gl": {
    "username": "gl-acme",
    "password": "gl",
    "name": "Jaya Norris"
  },
  "bl": {
    "username": "bl-acme",
    "password": "bl",
    "name": "Esha Harris"
  },
  "member": {
    "username": "member-acme",
    "password": "member",
    "name": "Abraham Woodard"
  }
}
```


