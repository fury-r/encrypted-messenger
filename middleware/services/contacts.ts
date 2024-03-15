import { GrpcClientService } from "../common/GrpcClientService";
import { validateToken } from "../utils/validateToken";

export const validateContacts = async (req: any, callback: any) => {
  validateToken(req, callback);
  const client = new GrpcClientService().getClient();

  const response = await client.ValidateContacts(
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
};
