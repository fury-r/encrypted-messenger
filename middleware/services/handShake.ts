import { GrpcClientService } from "../common/GrpcClientService";
import { Event } from "../proto/Event";
import { RabbitMQ } from "../common/rabbitMQ";
import { Metadata } from "@grpc/grpc-js";

export const handShakeRequest = async (req: any, callback: any) => {
  const client = new GrpcClientService().getClient();
  const rabbitMQ = new RabbitMQ("amqp://guest:guest@loclalhost:5672", "chat");
  const metadata = new Metadata();
  metadata.add("authorization", req.metadata.get("authorization")[0]);
  const isValidUser = await client.verifyToken({}, metadata, (e, _) => {
    if (e) {
      return callback(e, null);
    } else {
      return true;
    }
  });
  const data: Event = req.request;
  if (isValidUser && data?.reciever) {
    rabbitMQ.sendToQueue(JSON.stringify(data.exchange), data?.reciever);
  }
  return data;
};
