import { RabbitMQ } from "../common/rabbitMQ";
import { Metadata } from "@grpc/grpc-js";
import { UserRequest } from "../../proto/UserRequest";
import { GrpcClientService } from "../common/GrpcClientService";
export const send = async (req: any, callback: any) => {
  const client = new GrpcClientService().getClient();
  // const producer = new KafkaSetup().getKafkaProducer();
  const rabbitMQ = new RabbitMQ("amqp://guest:guest@localhost:5672", "chat");
  // const data: Event = req.request as Event;

  const metadata = new Metadata();

  metadata.add("authorization", req.metadata.get("authorization")[0]);

  // const request: UserRequest = {
  //   phoneNumber:
  //     req.request.message?.message?.reciever || req.request.exchange?.reciever,
  // };

  client.verifyToken({}, metadata, (e, _res) => {
    console.log(req.request)
    if (!e) {
      if (req.request?.reciever) {
        console.log("Adding to Message broker");

        rabbitMQ.sendToQueue(
          JSON.stringify(req.request),
          req.request?.reciever
        );
      } else {
        console.log(" not ", req.request);
      }
      return callback(null, "");
    }

    rabbitMQ.closeConnection();
  });

  console.log( req.request);

  if (req.request?.message?.message?.reciever) {
    console.log("Adding to Message broker");

    rabbitMQ.sendToQueue(JSON.stringify(req.request), req.request?.reciever);
  } else {
  }
  return callback(null, "");


};
