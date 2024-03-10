import path from "path";
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";
import { GrpcClientService } from "../common/GrpcClientService";
import { KafkaSetup } from "../common/kafka";
import { Event } from "../proto/Event";
import { RabbitMQ } from "../common/rabbitMQ";

export const chatService = (): grpc.UntypedServiceImplementation => {
  const PROTO_FILE = "../../protobuf/service/service.proto";

  const client = new GrpcClientService().getClient();
  const packageDef = protoLoader.loadSync(path.resolve(__dirname, PROTO_FILE));
  const grpcObject: any = grpc.loadPackageDefinition(packageDef);
  const producer = new KafkaSetup().getKafkaProducer();
  const rabbitMQ = new RabbitMQ("amqp://guest:guest@localhost:5672", "chat");
  return {
    //TODO: Currently hardcoded to handle event of type message
    send: async (
      req: {
        request: Event;
      },
      callback: any
    ) => {
      const data: Event = req.request as Event;
      console.log("hit", data);

      delete req.request.token;
      if (req.request?.message?.message?.reciever) {
        rabbitMQ.sendToQueue(
          JSON.stringify(req.request),
          req.request?.message?.message?.reciever
        );
        console.log("Adding to Message broker");
      } else {
        console.log(" not ", req.request);
      }

      return callback(null, {});
      let response: any = await client.VerifyToken(
        {
          token: req.request.token,
        },
        (e, result) => {
          if (e) {
            return callback(e, null);
          } else {
            return result;
          }
        }
      );
      if (response?.user) {
        // rabbitMQ.sendToQueue(JSON.stringify(req.request));
        // await producer?.send({
        //   topic: req.request.to,
        //   messages: [
        //     {
        //       value: req.request.message,
        //     },
        //   ],
        // });
      } else {
        return callback("Invalid token", null);
      }
    },
  };
};
