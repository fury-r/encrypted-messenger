import * as grpc from "@grpc/grpc-js";
import { GrpcClientService } from "../common/GrpcClientService";

export const verifyToken = async (req: any, callback: any) => {
  console.log(req.request, "verifyToken");
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
