import * as grpc from "@grpc/grpc-js";

export const helloService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";

  return {
    sayHello: async (req: any, callback: any) => {
      console.log("HHHHH");
      console.log(req.request);
      return callback();
    },
  };
};
