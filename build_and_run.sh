#!/usr/bin/env bash

# Build Java artifact
mvn clean package
mvn jar:jar

# Build Docker image
docker build --tag zackrbrown .

# Create Docker container
docker create \
--mount "type=bind,src=/etc/mindy/config,dst=/app/config" \
--mount "type=bind,src=${PWD}/src/main/resources/static,dst=/app/static" \
-p 443:443 -p 80:80 zackrbrown
