#!/usr/bin/env sh
#!/usr/bin/bash

DIR="$(cd "$(dirname "$0")" >/dev/null 2>&1 && pwd)"
java -jar $DIR/protoc-gen-grpc-kotlin-1.3.0-jdk8.jar "$@"