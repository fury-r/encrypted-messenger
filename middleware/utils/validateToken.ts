import { GrpcClientService } from "../common/GrpcClientService";
import * as grpc from "@grpc/grpc-js";

export const validateToken = async (req: any, callback: any) => {
  const client = new GrpcClientService().getClient();

  const response = await client.VerifyToken(
    {
      ...req.request,
    },
    (e, result) => {
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
    }
  );
  return response;
};
