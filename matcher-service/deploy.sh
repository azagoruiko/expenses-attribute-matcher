#!/usr/bin/env bash

VER=$1

docker build -t "192.168.1.53:5001/attribute-matching:${VER}" .
docker push "192.168.1.53:5001/attribute-matching:${VER}"
export NOMAD_ADDR="http://10.7.0.1:4646"
/usr/bin/nomad job run attribute-matcher.nomad
