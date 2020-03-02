#!/bin/sh
cd ../../
docker build -f docker/native/Dockerfile . -t micronaut && docker run -p 9080:9080 micronaut
