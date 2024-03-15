import { GrpcClientService } from "../common/GrpcClientService";
import { Event } from "../proto/Event";
import { RabbitMQ } from "../common/rabbitMQ";

export const handShakeRequest = async (
  req: {
    request: Event;
  },
  callback: any
) => {
  const client = new GrpcClientService().getClient();
  const rabbitMQ = new RabbitMQ("amqp://guest:guest@loclalhost:5672", "chat");
  const isValidUser = await client.verifyToken({}, (e, result) => {
    if (e) {
      return callback(e, null);
    } else {
      return true;
    }
  });
  const data: Event = req.request;
  if (isValidUser && data.exchange?.reciever) {
    rabbitMQ.sendToQueue(
      JSON.stringify(data.exchange),
      data.exchange?.reciever
    );
  }
  return data;
};
