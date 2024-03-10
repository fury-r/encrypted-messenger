import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { ProtoGrpcType } from "../proto/service";
import { ServicesHandlers } from "../proto/com/services/Services";
import { GrpcClientService } from "../common/GrpcClientService";

export const otpService = (): grpc.UntypedServiceImplementation => {
  const client = new GrpcClientService().getClient();

  return {
    otp: async (req: any, callback: any) => {
      console.log(req.request);
      let response = await client.Otp(
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
