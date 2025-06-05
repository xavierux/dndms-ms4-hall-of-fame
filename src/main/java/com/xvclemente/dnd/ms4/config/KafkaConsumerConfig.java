package com.xvclemente.dnd.ms4.config;

import com.xvclemente.dnd.dtos.events.AventuraFinalizadaEvent;
import com.xvclemente.dnd.dtos.events.ResultadoCombateIndividualEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> targetType) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // KEY_DESERIALIZER y VALUE_DESERIALIZER se configuran en DefaultKafkaConsumerFactory

        JsonDeserializer<T> valueDeserializer = new JsonDeserializer<>(targetType, false);
        valueDeserializer.addTrustedPackages("*"); // o "com.xvclemente.dnd.dtos.events"

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResultadoCombateIndividualEvent> resultadoCombateIndividualEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ResultadoCombateIndividualEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(ResultadoCombateIndividualEvent.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AventuraFinalizadaEvent> aventuraFinalizadaEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AventuraFinalizadaEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(AventuraFinalizadaEvent.class));
        return factory;
    }
}