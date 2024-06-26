import { Metadata } from "@grpc/grpc-js";
import { GrpcClientService } from "./common/GrpcClientService";
import { AuthResponse } from "../proto-types/AuthResponse";
import { RabbitMQ } from "./common/rabbitMQ";
import express from "express";
import { Server } from "socket.io";
import amqp from "amqplib";

import http from "http";
import { configDotenv } from "dotenv";
configDotenv();

const app = express();

export const server = http.createServer(app);

const io = new Server(server);
const client = new GrpcClientService().getClient();
let channel: amqp.Channel;

const rabbitMQClient = new RabbitMQ("chat");
io.on("connection", (socket) => {
  const token = socket.handshake.headers.authorization || "";

  socket.on("message", async (...args: any) => {
    const clientAddress = `${args.remoteAddress}:${args.remotePort}`;
    console.log(`new client connected: ${clientAddress} ${args}`);
    const metadata = new Metadata();
    channel = rabbitMQClient.channel as amqp.Channel;
    metadata.add("authorization", `Bearer ${token}`);

    let user: AuthResponse | undefined = undefined;

    client.verifyToken(
      {},
      metadata,
      async (e, res: AuthResponse | undefined) => {
        if (e || res?.error) {
          return e;
        } else {
          user = res;
          console.log("user", user);
          if (user) {
            const queue = (user as AuthResponse).user?.phoneNumber;
            // || (user  AuthResponse).user?.phoneNumber;
            if (queue) {
              console.log("connected to queue");
              await channel.assertQueue(queue, { durable: false });
              await channel.consume(
                queue,
                (message: amqp.ConsumeMessage | null) => {
                  if (message) {
                    console.log(message.content);
                    io.emit("message", message.content);
                    channel.ack(message, true);
                  }
                }
              );
            }
          }
        }
      }
    );
  });
});

app.get("/", (_: any, res) => {
  console.log("hey");
  return res.send({ response: "Hello world" });
});
