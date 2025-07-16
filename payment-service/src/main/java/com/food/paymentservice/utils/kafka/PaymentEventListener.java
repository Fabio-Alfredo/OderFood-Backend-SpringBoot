package com.food.paymentservice.utils.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.paymentservice.domain.commons.OrderEvent;
import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.dtos.payment.UpdateStatus;
import com.food.paymentservice.domain.enums.PaymentStatus;
import com.food.paymentservice.services.contrat.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventListener.class);
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    public PaymentEventListener(PaymentService paymentService, ObjectMapper objectMapper) {
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
                    System.out.println("Received payment creation request: " + paymentDto.getOrderId() + ", Amount: " + paymentDto.getAmount());
                    paymentService.createPayment(paymentDto);
                    break;
                case "cancel-order":
                    UpdateStatus updateStatus = convertTo(orderEvent.getData(), UpdateStatus.class);
                    updateStatus.setStatus(PaymentStatus.CANCELLED);
                    paymentService.updatePaymentStatus(updateStatus);
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
