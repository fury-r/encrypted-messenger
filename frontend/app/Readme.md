# run proto  for middleware
protoc --proto_path=../protobuf        --plugin=proto-gen-kotlin=C:/Jars/protoc-gen-grpc-kotlin-1.3.0-jdk8         --kotlin_out=./app/src/main/java   --java_out=./app/src/main/java       ../protobuf/middleware/middleware.proto
