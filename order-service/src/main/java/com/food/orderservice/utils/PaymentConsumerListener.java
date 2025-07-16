package com.food.orderservice.utils;

import com.food.orderservice.domain.OrderEvent;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumerListener {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumerListener.class);

    @KafkaListener(
            topics = "${kafka.topic.payment-create}",
            groupId = "order-service-group"
    )
    public void listen(OrderEvent<?> message) {
        try {

            LOGGER.info("Received message from payment service: {}", message);
            System.out.println("Processing message: " + message);
            // Aquí puedes agregar la lógica para procesar el mensaje recibido
        } catch (Exception e) {
            LOGGER.error("Error processing message from payment service: {}", e.getMessage());
        }
    }
}
