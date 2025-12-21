//package com.language_practice_server.server_demo.config;
//
//import com.language_practice_server.server_demo.kafka.KafkaEventProducer;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import static org.mockito.Mockito.mock;
//
//@TestConfiguration
//public class TestKafkaConfig {
//
//    @Bean
//    @Primary
//    public ProducerFactory<String, Object> producerFactory() {
//        return mock(ProducerFactory.class);
//    }
//
//    @Bean
//    @Primary
//    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
//        return mock(KafkaTemplate.class);
//    }
//
//    @Bean
//    @Primary
//    public KafkaEventProducer kafkaEventProducer(KafkaTemplate<String, Object> template) {
//        return mock(KafkaEventProducer.class);
//    }
//
//}
