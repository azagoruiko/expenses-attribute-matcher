#!/usr/bin/env bash

VER=$1
REPO="192.168.0.21:9999/docker"

pushd ..
mvn clean install
popd

docker build -t $REPO/attribute-matcher:$VER .
docker push $REPO/attribute-matcher:$VER

export NOMAD_ADDR="http://192.168.0.21:4646"
nomad job run ./attribute-matcher.nomad
