import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { ProtoGrpcType } from "../proto/service";
import { ServicesHandlers } from "../proto/com/services/Services";
import { GrpcClientService } from "../common/GrpcClientService";

export const loginService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";
  const client = new GrpcClientService().getClient();
  const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE));
  const grpcObject: any = grpc.loadPackageDefinition(packageDef);
  const packageObj = grpcObject.Services.service;

  return {
    login: async (req: any, callback: any) => {
      console.log("hit", req);
      let response = await client.Login(
        {
          ...req.request,
        },
        (e, result) => {
          console.log(e, result);
          if (e) {
            return callback(e, null);
          } else {
            return callback("", { ...result });
          }
        }
      );
      return response;
    },
  };
};
