import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { GrpcClientService } from "../common/GrpcClientService";

export const contactsService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";

  const client = new GrpcClientService().getClient();
  const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE));
  const grpcObject: any = grpc.loadPackageDefinition(packageDef);

  return {
    validateContacts: async (req: any, callback: any) => {
      let response = await client.ValidateContacts(
        {
          ...req.request,
        },
        (e, result) => {
          if (e) {
            return callback(e, null);
          } else {
            console.log(result);
            return callback("", { ...result });
          }
        }
      );
      return response;
    },
  };
};
