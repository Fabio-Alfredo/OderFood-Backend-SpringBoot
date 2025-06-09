package com.food.orderservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${kafka.topic.order-created}")
    private String generateTopicName;

    @Bean
    public NewTopic generateTopic(){

        Map<String, String> config = new HashMap<>();
        // Elimina los mensajes antiguos de la partición
        config.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
        // Configura el tiempo de retención de los mensajes en la partición
        config.put(TopicConfig.RETENTION_MS_CONFIG, "86400000" // 1 dia
        );
        // Configura el tamaño máximo de la partición
        config.put(TopicConfig.SEGMENT_BYTES_CONFIG, "1073741824"); // 1 GB
        // Configura el tamaño máximo de los mensajes en la partición
        config.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "1048576" // 1 MB
        );

        return TopicBuilder.name(generateTopicName)
                .partitions(1)
                .replicas(1)
                .configs(config)
                .build();

    }
}
