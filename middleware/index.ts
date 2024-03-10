import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { ProtoGrpcType } from "./proto/service";
import { ServicesHandlers } from "./proto/com/services/Services";
import { loginService } from "./services/login";
import { registerService } from "./services/register";
import { otpService } from "./services/otp";

const PORT = 8081;
const PROTO_FILE = "../protobuf/service/service.proto";
const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE));
const grpcObject = grpc.loadPackageDefinition(
  packageDef
) as unknown as ProtoGrpcType;
import axios from "axios";
import { GrpcClientService } from "./common/GrpcClientService";
import { contactsService } from "./services/contacts";
import { savePubKeyService } from "./services/savePubkey";
import { verifyTokenService } from "./services/verifyToken";
import { chatService } from "./services/chat";
const TestConnecton = async () => {
  const client = new GrpcClientService().getClient();

  console.log("hit");
  await client.login(
    {
      usePassword: true,
      password: "test",
      phoneNumber: "+919158907407",
    },
    (e) => {
      console.log(e);
      return "";
    }
  );
};
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
      console.log(`Server running at http://0.0.0.0:${port}`);
      server.start();
    }
  );
};

function getServer() {
  const server = new grpc.Server();
  const loginServiceObj: any = loginService();
  const registerServiceObj: any = registerService();
  const otpServiceObj: any = otpService();
  const chatServiceObj: any = chatService();

  const contactsServiceObj: any = contactsService();
  const savePubKeyObj: any = savePubKeyService();
  const verifyTokenObj: any = verifyTokenService();
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
    ...loginServiceObj,
    ...registerServiceObj,
    ...otpServiceObj,
    ...contactsServiceObj,
    ...savePubKeyObj,
    ...verifyTokenObj,
    ...chatServiceObj,
  });
  return server;
}
main();
