import { GrpcClientService } from "../common/GrpcClientService";

export const register = async (req: any, callback: any) => {
  console.log(req.request);
  const client = new GrpcClientService().getClient();

  const response = await client.Register(
    {
      ...req.request,
    },

    (e, result) => {
      console.log(e, result);
      if (e) {
        return callback(e, null);
      } else {
        return callback("", { ...result });
      }
    }
  );
  return response;
};
