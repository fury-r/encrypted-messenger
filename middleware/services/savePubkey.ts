import { Metadata } from "@grpc/grpc-js";
import { GrpcClientService } from "../common/GrpcClientService";
import { validateToken } from "../utils/validateToken";

export const savePubKey = async (req: any, callback: any) => {
  validateToken(req, callback);
  const metadata = new Metadata();
  metadata.add("authorization", req.metadata.get("authorization")[0]);
  const client = new GrpcClientService(undefined).getClient();

  console.log("savePubKey", req.request);
  const response = await client.savePubKey(
    {
      ...req.request,
    },
    metadata,
    (e, result) => {
      if (e) {
        return callback(e, null);
      } else {
        return callback("", { ...result });
      }
    }
  );
  return response;
};
