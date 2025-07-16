package com.food.paymentservice.services.impl;

import com.food.paymentservice.Exceptions.HttpError;
import com.food.paymentservice.domain.dtos.auth.UserDto;
import com.food.paymentservice.domain.dtos.payment.ConfirmPaymentDto;
import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.enums.PaymentStatus;
import com.food.paymentservice.domain.models.Payment;
import com.food.paymentservice.repositories.PaymentRepository;
import com.food.paymentservice.services.contrat.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${kafka.topic.payment-create}")
    private String paymentCreateTopic;

    private final PaymentRepository paymentRepository;
    private final StripeServiceImpl stripeService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository, StripeServiceImpl stripeService, KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.stripeService = stripeService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Payment confirmPayment(ConfirmPaymentDto paymentDto, UserDto user) {
        try{

            Payment payment = paymentRepository.findByOrderId(paymentDto.getOrderId());
            if(payment == null || payment.getStatus() != PaymentStatus.PENDING) {
                throw new HttpError(HttpStatus.NOT_FOUND, "Payment not found or already confirmed." );
            }

            if(user.getId() == null || !user.getId().equals(payment.getUserId())) {
                throw new HttpError(HttpStatus.UNAUTHORIZED, "User not authorized to confirm this payment." );
            }

           payment.setDescription("Payment for order " + paymentDto.getOrderId() + " confirmed." );
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setCurrency(paymentDto.getCurrency());
            payment.setPaymentMethodId(paymentDto.getPaymentMethodId());

            String PaymentId = stripeService.createPaymentStripe(payment);
            payment.setStripePaymentId(PaymentId);


            return paymentRepository.save(payment);
        }catch (HttpError e) {
            throw e;
        }
    }

    @Override
    public Payment createPayment(CreatePaymentDto paymentDto) {
        try{

            if(paymentRepository.existsByOrderId(paymentDto.getId())) {
                throw new HttpError(HttpStatus.CONFLICT, "Payment already exists for this order.");
            }
            Payment payment = new Payment();
            payment.setUserId(paymentDto.getCustomerId());
            payment.setOrderId(paymentDto.getId());
            payment.setAmount(paymentDto.getTotal());
            payment.setStatus(PaymentStatus.PENDING);

            Payment newPayment = paymentRepository.save(payment);

            System.out.println("Payment created with ID: " + newPayment);

            return newPayment;
        }catch (HttpError e){
            throw  e;
        }
    }

    @Override
    public Payment findByOrderId(UUID orderId) {
        return null;
    }


}
