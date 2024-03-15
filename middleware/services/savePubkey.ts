import { GrpcClientService } from "../common/GrpcClientService";
import { validateToken } from "../utils/validateToken";

export const savePubKeys = async (req: any, callback: any) => {
  validateToken(req, callback);
  const client = new GrpcClientService().getClient();

  console.log("savePubKey", req.request);
  const response = await client.savePubKey(
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
};
