import { GrpcClientService } from "../common/GrpcClientService";

export const login = async (req: any, callback: any) => {
  console.log("hit", req);
  const client = new GrpcClientService().getClient();
  const response = await client.Login(
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
