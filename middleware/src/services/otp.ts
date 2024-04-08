import { GrpcClientService } from "../common/GrpcClientService";

export const otp = async (req: any, callback: any) => {
  console.log(req.request);
  const client = new GrpcClientService().getClient();
  const response = await client.Otp(
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
