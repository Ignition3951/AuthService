package com.utk.authservice.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

//     This class will contain methods to send messages to Kafka topics
//     You can use KafkaTemplate to send messages
//     For example:
     private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

     public MessageProducer(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
         this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
     }

     public void sendMessage(String topic, Object message) {
         multiTypeKafkaTemplate.send(topic, message);
     }
}
