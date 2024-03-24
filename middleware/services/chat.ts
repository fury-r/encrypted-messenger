import { RabbitMQ } from "../common/rabbitMQ";
import { Metadata } from "@grpc/grpc-js";
import { UserRequest } from "../proto/UserRequest";
export const send = async (req: any, callback: any) => {
  // const client = new GrpcClientService().getClient();
  // const producer = new KafkaSetup().getKafkaProducer();
  const rabbitMQ = new RabbitMQ("amqp://guest:guest@localhost:5672", "chat");
  // const data: Event = req.request as Event;

  const metadata = new Metadata();

  metadata.add("authorization", req.metadata.get("authorization")[0]);

  const request: UserRequest = {
    phoneNumber:
      req.request.message?.message?.reciever || req.request.exchange?.reciever,
  };
  console.log(request);
  // const response = await client.getUser(request, metadata, (e, res) => {
  //   if (e) {
  //     console.log(e, request);
  //     console.log(res);
  //     return callback(e, null);
  //   } else {
  //     console.log(req.request, request);
  //     const publicKey = res?.pubKey;
  //     console.log(res?.pubKey, "here");
  //     // 2nd level of encryption
  //     const encryptedMessage = encryptData(
  //       JSON.stringify(req.request),
  //       publicKey || ""
  //     );
  //     console.log(encryptedMessage, "s");

  //   }
  //   return callback(null, {});
  // });

  if (req.request?.message?.message?.reciever) {
    console.log("Adding to Message broker");

    rabbitMQ.sendToQueue(JSON.stringify(req.request), req.request?.reciever);
  } else {
    console.log(" not ", req.request);
  }
  return callback(null, "");

  // const response: any = await client.VerifyToken(
  //   {
  //     token: req.request,
  //   },
  //   (e, result) => {
  //     if (e) {
  //       return callback(e, null);
  //     } else {
  //       return result;
  //     }
  //   }
  // );
  // if (response?.user) {
  //   // rabbitMQ.sendToQueue(JSON.stringify(req.request));
  //   // await producer?.send({
  //   //   topic: req.request.to,
  //   //   messages: [
  //   //     {
  //   //       value: req.request.message,
  //   //     },
  //   //   ],
  //   // });
  // } else {
  //   return callback("Invalid token", null);
  // }
};
