package com.food.paymentservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.paymentservice.domain.commons.OrderEvent;
import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.models.Payment;
import com.food.paymentservice.services.contrat.PaymentService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerListener.class);
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    public KafkaConsumerListener(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "#{'${kafka.topic.order-created}'}",
            groupId = "payment-service-group"
    )
    public void listen(OrderEvent<?> orderEvent){

        try{
            LOGGER.info("Received order event: {}", orderEvent.getEventType());
            switch (orderEvent.getEventType()){
                case "create-order":
                    CreatePaymentDto paymentDto = convertTo(orderEvent.getData(), CreatePaymentDto.class);
                    paymentService.createPayment(paymentDto);
                    break;

                default:
                    LOGGER.warn("Unhandled event type: {}", orderEvent.getEventType());
            }

        } catch (Exception e) {
            LOGGER.error("Error processing payment creation request: {}", e.getMessage());
        }


    }

    private <T> T convertTo(Object data, Class<T> targetType) {
        return objectMapper.convertValue(data, targetType);
    }
}
