#!/usr/bin/env bash

VER=$1
REPO="10.8.0.5:9999/docker"
#
#pushd ..
#mvn clean install
#popd

docker build -t $REPO/attribute-matching:$VER .
docker push $REPO/attribute-matching:$VER

export NOMAD_ADDR="http://10.8.0.1:4646"
nomad job run ./attribute-matcher.nomad
