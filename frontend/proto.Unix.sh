#1
sudo protoc  --proto_path=../protobuf/service     --plugin=protoc-gen-kotlin=./script.sh --kotlin_out=./app/src/main/java  --experimental_allow_proto3_optional   --java_out=./app/src/main/java   ../protobuf/service/*.proto  
sudo protoc  --plugin=protoc-gen-grpc-kotlin=./tools/protobuf/compiler   --experimental_allow_proto3_optional  --grpc-kotlin_out="./app/src/main/java"  --proto_path="../protobuf/service" ../protobuf/service/*.proto   
sudo protoc  --plugin=protoc-gen-java=./tools/protobuf/compiler   --experimental_allow_proto3_optional   --java_out=./app/src/main/java --proto_path="../protobuf/service" ../protobuf/service/*.proto  
sudo protoc  --plugin=protoc-gen-grpc-java=./protoc-gen-grpc-java-1.56.0-windows-x86_64.exe   --experimental_allow_proto3_optional --grpc-java_out="./app/src/main/java"  --proto_path="../protobuf/service" ../protobuf/service/*.proto












# sudo protoc --plugin=protoc-gen-grpc-java=./protoc-gen-grpc-java-1.56.0-linux-aarch_64.exe  --grpc-java_out="./app/src/main/java"  --proto_path="../protobuf" "../protobuf/service/service.proto"
# sudo protoc --plugin=protoc-gen-kotlin=../../Downloads/grpc/compiler/build/install/compiler/bin/compiler    --kotlin_out=./app/src/main/java --proto_path="../protobuf" "../protobuf/service/service.proto"


#windows command 
#  protoc --proto_path=../protobuf/service     --plugin=protoc-gen-grpc-kotlin=./protoc-gen-grpc-kotlin-1.3.0-jdk8.jar  --kotlin_out=./app/src/main/java   --java_out=./app/src/main/java      ../protobuf/service/service.proto


# sudo protoc --go_out="./types"   --proto_path="./protobuf" "../protobuf/service/*.proto"




