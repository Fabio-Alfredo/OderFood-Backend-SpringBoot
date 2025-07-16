package com.food.orderservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.orderservice.domain.OrderEvent;
import com.food.orderservice.domain.dto.order.UpdateStatusOrder;
import com.food.orderservice.domain.enums.StatusOrder;
import com.food.orderservice.service.contract.OrderService;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumerListener {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumerListener.class);
    private final OrderService orderService;
    private  final ObjectMapper objectMapper;

    public PaymentConsumerListener(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${kafka.topic.payment-create}",
            groupId = "order-service-group"
    )
    public void listen(OrderEvent<?> message) {
        try {

            LOGGER.info("Received message from payment service: {}", message.getEventType());
            switch (message.getEventType()) {
                case "confirm-payment":
                    UpdateStatusOrder statusOrder = convertTo(message.getData(), UpdateStatusOrder.class);
                    statusOrder.setStatus(StatusOrder.PAYMENT_CONFIRMED);
                    orderService.updateStatusOrder(statusOrder);
                    break;

                default:
                    LOGGER.warn("Unhandled event type: {}", message.getEventType());
            }
        } catch (Exception e) {
            LOGGER.error("Error processing message from payment service: {}", e.getMessage());
        }
    }

    private <T> T convertTo(Object data, Class<T> targetType) {
        return objectMapper.convertValue(data, targetType);
    }
}
