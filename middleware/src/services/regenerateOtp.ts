import { GrpcClientService } from "../common/GrpcClientService";

export const regenerateOtp = async (req: any, callback: any) => {
  const client = new GrpcClientService(undefined).getClient();

  const response = await client.regenerateOtp(
    {
      ...req.request,
    },
    (e, result) => {
      console.log(e, result);
      if (e) {
        return callback(e, null);
      } else {
        console.log(result);
        return callback("", result);
      }
    }
  );
  return response;
};
