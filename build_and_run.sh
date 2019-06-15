#!/usr/bin/env bash

# Build Java artifact
mvn clean package
mvn jar:jar

# Build Docker image
docker build --tag zackrbrown .

# Create Docker container
docker create -p 443:443 -p 80:80 zackrbrown