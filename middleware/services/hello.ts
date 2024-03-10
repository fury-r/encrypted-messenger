import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { GrpcClientService } from "../common/GrpcClientService";

export const helloService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";

  return {
    sayHello: async (req: any, callback: any) => {
      console.log("HHHHH");
      console.log(req.request);
      return callback();
    },
  };
};
