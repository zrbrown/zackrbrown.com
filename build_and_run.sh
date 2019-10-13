#!/usr/bin/env bash

docker run \
--mount "type=bind,src=/etc/mindy/config,dst=/app/config" \
--mount "type=bind,src=${PWD}/src/main/resources/static,dst=/app/static" \
-p 443:443 -p 80:80 zackrbrown
