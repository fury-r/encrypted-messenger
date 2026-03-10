import { Metadata } from "@grpc/grpc-js";
import { GrpcClientService } from "../common/GrpcClientService";

export const rotateKey = async (req: any, callback: any) => {
  const client = new GrpcClientService(undefined).getClient();
  const metadata = new Metadata();

  metadata.add("authorization", req.metadata.get("authorization")[0]);
  const response = await client.rotateKey(
    {
      ...req.request,
    },
    metadata,
    (e, result) => {
      if (e) {
        return callback(e, null);
      } else {
        return callback("", result);
      }
    }
  );
  return response;
};
