import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { GrpcClientService } from "../common/GrpcClientService";

export const savePubKeyService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";

  const client = new GrpcClientService().getClient();
  const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE));
  const grpcObject: any = grpc.loadPackageDefinition(packageDef);

  return {
    savePubKey: async (req: any, callback: any) => {
      console.log("savePubKey", req.request);
      let response = await client.savePubKey(
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
      return response;
    },
  };
};
