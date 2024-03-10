import path from "path";
import { Kafka ,} from "kafkajs";



const { KAFKA_USERNAME: username, KAFKA_PASSWORD: password } = process.env 



export class KafkaSetup{
    sasl:any=null
    ssl: any = null;
    constructor() {
        this.sasl=username && password?{username,password,mechainism:"plain"}:null
        this.ssl=!!this.sasl
    }

    getKafka() {
        return new Kafka({
            clientId: 'npm-slack-notifier',
            brokers: [process.env.KAFKA_BOOTSTRAP_SERVER],
            ssl: this.ssl,
          sasl: this.sasl
          })
    }


}