import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { configDotenv } from "dotenv";
configDotenv();

const PORT = process.env.SERVER_PORT || 8081;

import {
  blockUser,
  handShakeRequest,
  login,
  otp,
  register,
  savePubKey,
  send,
  validateContacts,
  verifyToken,
} from "./src/services";

const main = async () => {
  console.log("starting up middleware");
  // await TestConnecton();
  const server = getServer();

  server.bindAsync(
    `0.0.0.0:${PORT}`,
    grpc.ServerCredentials.createInsecure(),
    (err, port) => {
      if (err) {
        console.error(err);
        return;
      }
      console.log(`Middleware running on http://0.0.0.0:${port}`);
      server.start();
    }
  );
};

function getServer() {
  const server = new grpc.Server();

  const PROTO_FILE = "../protobuf/service/service.proto";

  const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE), {
    keepCase: true,
    longs: String,
    enums: String,
    defaults: true,
    oneofs: true,
  });
  const grpcObject: any = grpc.loadPackageDefinition(packageDef);
  const packageObj = grpcObject.Services.service;

  server.addService(packageObj, {
    login,
    validateContacts,
    handShakeRequest,
    register,
    otp,
    savePubKey,
    verifyToken,
    send,
    blockUser,
  });
  return server;
}
main();
