import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { GrpcClientService } from "../common/GrpcClientService";

export const verifyTokenService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";

  const client = new GrpcClientService().getClient();
  const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE));
  const grpcObject: any = grpc.loadPackageDefinition(packageDef);

  return {
    verifyToken: async (req: any, callback: any) => {
      console.log(req.request, "verifyToken");
      let response = await client.VerifyToken(
        {
          ...req.request,
        },
        (e, result) => {
          if (e) {
            return callback(e, null);
          } else {
            return callback("", { ...result });
          }
        }
      );
      console.log(response, "verifyToken");
      return response;
    },
  };
};
