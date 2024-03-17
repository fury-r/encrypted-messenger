import { GrpcClientService } from "../common/GrpcClientService";
import { KafkaSetup } from "../common/kafka";
import { Event } from "../proto/Event";
import { RabbitMQ } from "../common/rabbitMQ";
import { validateToken } from "../utils/validateToken";
import { encryptMessage } from "../utils/rsa";
import { Metadata } from "@grpc/grpc-js";

export const send = async (req: any, callback: any) => {
  validateToken(req, (_, res: any) => {});

  const client = new GrpcClientService(undefined).getClient();
  const producer = new KafkaSetup().getKafkaProducer();
  const rabbitMQ = new RabbitMQ("amqp://guest:guest@localhost:5672", "chat");
  const data: Event = req.request as Event;

  let pubKey: string | undefined;
  client.getUser(
    {
      phoneNumber:
        req.request.message?.message?.reciever ||
        req.request.exchange?.reciever,
    },
    (_, res) => {
      pubKey = res?.pubKey;
    }
  );
  // 2nd level of encryption
  const encryptedMessage = encryptMessage(
    JSON.stringify(req.request),
    pubKey || ""
  );
  if (req.request?.message?.message?.reciever) {
    console.log("Adding to Message broker");

    rabbitMQ.sendToQueue(
      encryptedMessage,
      req.request?.message?.message?.reciever
    );
  } else {
    console.log(" not ", req.request);
  }

  return callback(null, {});
  const response: any = await client.VerifyToken(
    {
      token: req.request,
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
};
