import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { ProtoGrpcType } from "../proto/service";
import { ServicesHandlers } from "../proto/com/services/Services";
import { GrpcClientService } from "../common/GrpcClientService";

export const registerService = (): grpc.UntypedServiceImplementation => {
  const client = new GrpcClientService().getClient();

  return {
    register: async (req: any, callback: any) => {
      console.log(req.request);
      let response = await client.Register(
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
