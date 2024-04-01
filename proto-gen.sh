#!/usr/bin/env sh
npx proto-loader-gen-types --grpcLib=@grpc/grpc-js --outDir=ts-proto/ ../protobuf/service/*.proto 