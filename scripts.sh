#  protoc --proto_path=../protobuf     --plugin=proto-gen-kotlin=C:/Jars/protoc-gen-grpc-kotlin-1.3.0-jdk8         --kotlin_out=./app/src/main/java   --java_out=./app/src/main/java     --grpc-kotlin_out=./app/src/main/java     ../protobuf/middleware/middleware.proto
yarn proto-loader-gen-types --grpcLib=@grpc/grpc-js --outDir=./middleware/data-types-ts/ ./protobuf/middleware/middleware.proto

#  protoc --ts_out=import_style=commonjs:./app/src/ --grpc-web_out=import_style=commonjs,mode=grpcwebtext:./app/src/ ./protobuf/middleware/middleware.proto

 yarn   --plugin=protoc-gen-ts=./node_modules/.bin/protoc-gen-ts --plugin=protoc-gen-grpc-web=./node_modules/.bin/grpc_tools_node_protoc_plugin --ts_out=import_style=commonjs,binary:./app/src/ --grpc-web_out=import_style=commonjs,mode=grpcwebtext:./app/src/ ./protobuf/middleware/middleware.proto



   protoc  --plugin=protoc-gen-ts=./node_modules/grpc-tools/bin/protoc-gen-ts.cmd --plugin=protoc-gen-grpc-web=./node_modules/.bin/grpc_tools_node_protoc_plugin --ts_out=import_style=commonjs,binary:./app/src/ --grpc-web_out=import_style=commonjs,mode=grpcwebtext:./app/src/ ./protobuf/middleware/middleware.proto



#generate grpc for kotlin
 protoc --plugin=protoc-gen-grpckt=script.sh   --grpckt_out="./app/src/main/java" --proto_path="../protobuf" "../protobuf/middleware/middleware.proto"
#  // protoc --proto_path=../protobuf     --plugin=protoc-gen-grpc-kotlin=/usr/local/bin/protoc-gen-grpc-kotlin-1.3.0-jdk8.jar  --kotlin_out=./app/src/main/java   --java_out=./app/src/main/java     --grpc-kotlin_out=./app/src/main/java     ../protobuf/middleware/middleware.proto

# sudo protoc --plugin=protoc-gen-grpckt=script.sh   --grpckt_out="./app/src/main/java" --proto_path="../protobuf" "../protobuf/middleware/middleware.proto"
# sudo protoc --plugin=protoc-gen-grpc-kotlin=script.sh   --grpc-kotlin_out="./app/src/main/java" --proto_path="../protobuf" "../protobuf/middleware/middleware.proto"

# sudo protoc --plugin=protoc-gen-grpc-kotlin=../../Downloads/grpc/compiler/build/install/compiler/bin/compiler    --kotlin_out=./app/src/main/java   --java_out=./app/src/main/java     --grpc-kotlin_out=./app/src/main/java --proto_path="../protobuf" "../protobuf/middleware/middleware.proto"



 protoc --go_out=pb --go_opt=paths=source_relative     --go-grpc_out=pb --go-grpc_opt=paths=source_relative  --proto_path="../protobuf" "../protobuf/middleware/*.proto"