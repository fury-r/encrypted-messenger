
protoc --plugin=protoc-gen-kotlin=./tools/protobuf/script.sh --experimental_allow_proto3_optional --kotlin_out=./app/src/main/java --java_out=./app/src/main/java --proto_path=../protobuf/service ../protobuf/service/*.proto
protoc --plugin=protoc-gen-grpc-kotlin=./tools/protobuf/compiler/build/install/compiler/bin/compiler --experimental_allow_proto3_optional --grpc-kotlin_out=./app/src/main/java --proto_path=../protobuf/service ../protobuf/service/*.proto
protoc --plugin=protoc-gen-java=./tools/protobuf/compiler/build/install/compiler/bin/compiler --experimental_allow_proto3_optional --java_out=./app/src/main/java --proto_path=../protobuf/service ../protobuf/service/*.proto
protoc --plugin=protoc-gen-grpc-java=./tools/protobuf/protoc-gen-grpc-java-1.56.0-windows-x86_64.exe --experimental_allow_proto3_optional --grpc-java_out=./app/src/main/java --proto_path=../protobuf/service ../protobuf/service/*.proto



 














