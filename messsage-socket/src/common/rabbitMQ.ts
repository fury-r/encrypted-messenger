import amqp from "amqplib";

export class RabbitMQ {
  url: any;
  queue: string;
  connection: amqp.Connection | null = null;
  channel: amqp.Channel | null = null;
  constructor(queue: string) {
    this.url =
      process.env.RABBITMQ_URL ||
      `amqp://${process.env.RABBITMQ_USERNAME || "guest"}:${process.env.RABBITMQ_PASSWORD || "guest"}@${process.env.RABBITMQ_HOST || "localhost"}:5672/`;
    this.queue = queue;
    this.setConnection();
  }

  async setConnection() {
    this.connection = await amqp.connect(this.url);
    console.log("Connected to RabbitMQ");
    this.channel = await this.connection.createChannel();
  }

  closeConnection() {
    this.connection!.close();
  }
}
