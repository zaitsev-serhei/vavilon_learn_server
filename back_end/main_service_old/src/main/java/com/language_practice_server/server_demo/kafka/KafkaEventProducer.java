package com.language_practice_server.server_demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventProducer {
    //add logger
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, String key,Object event) {

        try{
            kafkaTemplate.send(topic, key, event);
        } catch (Exception exception){
            System.out.println("Error publishing Kafka");
        }
    }
}
