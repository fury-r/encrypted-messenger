import amqp from "amqplib";

export class RabbitMQ {
  url: any;
  queue: string;
  connection: amqp.Connection | null = null;
  channel: amqp.Channel | null = null;
  constructor(url: string, queue: string) {
    this.url = url;
    this.queue = queue;
    this.setConnection().then(() => this.createChannel());
  }

  async setConnection() {
    this.connection = await amqp.connect(this.url);
    console.log("Connected to RabbitMQ");
  }

  async createChannel() {
    this.channel = await this.connection!.createChannel();
    console.log("Channel created for RabbitMQ");
  }

  async createExchange(exchange: string) {
    await this.channel!.assertExchange(exchange, "chat", {
      durable: false,
      autoDelete: false,
      arguments: "",
    });
  }
  async sendToQueue(message: string, queue: string) {
    console.log("send to queue",queue)
    await this.channel!.assertQueue(queue, {
      durable: false,
      arguments: "",
      autoDelete: false,
      exclusive: false,
    });
    this.channel!.sendToQueue(queue, Buffer.from(message));
    console.log("Data sent to RabbitMQ");
  }

  closeConnection() {
    console.log("Connection closed");
    this.connection!.close();
  }
}
