package com.food.paymentservice.utils;

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

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    public KafkaConsumerListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "#{'${kafka.topic.order-created}'}",
            groupId = "payment-service-group"
    )
    public void listen(CreatePaymentDto paymentDto){

        try{
            Payment payment = paymentService.createPayment(paymentDto);
            System.out.println("Payment created successfully: " + payment);
            LOGGER.info("Received payment creation request: {}", paymentDto);

        } catch (Exception e) {
            LOGGER.error("Error processing payment creation request: {}", e.getMessage());
        }


    }
}
