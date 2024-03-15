#!/usr/bin/env sh

npx proto-loader-gen-types --grpcLib=@grpc/grpc-js --outDir=proto/ ../protobuf/service/*.proto 