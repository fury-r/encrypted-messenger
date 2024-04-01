import * as grpc from "@grpc/grpc-js";
import { GrpcClientService } from "../common/GrpcClientService";

export const verifyToken = async (req: any, callback: any) => {
  const metadata = new grpc.Metadata();
  metadata.add("authorization", req.metadata.get("authorization")[0]);
  const client = new GrpcClientService(undefined).getClient();

  const response = await client.VerifyToken({}, metadata, (e, result) => {
    if (e) {
      return callback(
        {
          code: grpc.status.UNAUTHENTICATED,
          details: "Unauthorized - Invalid token",
        },
        null
      );
    } else {
      return callback("", { ...result });
    }
  });
  return response;
};
