#!/bin/bash

docker pull mysql:latest
docker run --name contact_registry_db -e MYSQL_USER=admin -e MYSQL_PASSWORD=my_password -e MYSQL_DB=my_db -d mysql
