package com.food.orderservice.configuration.kafka.provider;

import com.food.orderservice.domain.model.Order;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProviderConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig(){
        Map<String, Object>properties = new HashMap<>();

        // Configuración del serializador de claves
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Configuración del serializador de valores
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Configuración del serializador de valores
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // Configuración del tipo de contenido del mensaje
        properties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return properties;
    }

    @Bean
    public ProducerFactory<String, Order> providerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, Order> kafkaTemplate(ProducerFactory<String, Order> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
