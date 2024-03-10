import path from "path";
import { Kafka } from "kafkajs";

const { KAFKA_USERNAME: username, KAFKA_PASSWORD: password } = process.env;

export class KafkaSetup {
  sasl: any = null;
  ssl: any = null;
  kafka: Kafka | null = null;
  constructor() {
    this.sasl =
      username && password ? { username, password, mechainism: "plain" } : null;
    this.ssl = !!this.sasl;
    this.kafka = new Kafka({
      clientId: "chat-service",
      brokers: [process.env.KAFKA_BOOTSTRAP_SERVER!],
      ssl: this.ssl,
      sasl: this.sasl,
    });
  }

  getKafkaProducer() {
    return this.kafka?.producer();
  }
}
