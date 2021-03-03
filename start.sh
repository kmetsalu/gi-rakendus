#!/bin/bash

cd backend && mvn clean install -DskipTests && cd ..

docker-compose down --remove-orphans
docker-compose build
docker-compose up