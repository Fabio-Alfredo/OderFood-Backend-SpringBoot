package com.food.paymentservice.services.impl;

import com.food.paymentservice.Exceptions.HttpError;
import com.food.paymentservice.domain.commons.OrderEvent;
import com.food.paymentservice.domain.dtos.auth.UserDto;
import com.food.paymentservice.domain.dtos.payment.ConfirmPaymentDto;
import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.dtos.payment.PaymentOrderDto;
import com.food.paymentservice.domain.dtos.payment.UpdateStatus;
import com.food.paymentservice.domain.enums.PaymentStatus;
import com.food.paymentservice.domain.models.Payment;
import com.food.paymentservice.repositories.PaymentRepository;
import com.food.paymentservice.services.contrat.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${kafka.topic.payment-create}")
    private String paymentCreateTopic;

    private final PaymentRepository paymentRepository;
    private final StripeServiceImpl stripeService;
    private final KafkaTemplate<String, OrderEvent<?>> kafkaTemplate;
    private final ModelMapper modelMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, StripeServiceImpl stripeService, KafkaTemplate<String, OrderEvent<?>> kafkaTemplate, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.stripeService = stripeService;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
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

            payment.setCurrency(paymentDto.getCurrency());
            payment.setPaymentMethodId(paymentDto.getPaymentMethodId());
            System.out.println("Payment details: " + payment.toString());
           payment.setDescription("Payment for order " + paymentDto.getOrderId() + " confirmed." );
            payment.setStatus(PaymentStatus.COMPLETED);

            String PaymentId = stripeService.createPaymentStripe(payment);
            payment.setStripePaymentId(PaymentId);

            PaymentOrderDto paymentOrderDto = modelMapper.map(payment, PaymentOrderDto.class);
            OrderEvent<PaymentOrderDto> orderEvent = new OrderEvent<>("confirm-payment", paymentOrderDto);
            kafkaTemplate.send(paymentCreateTopic, orderEvent);

            return paymentRepository.save(payment);
        }catch (HttpError e) {
            throw e;
        }
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public Payment createPayment(CreatePaymentDto paymentDto) {
        try{

            if(paymentRepository.existsByOrderId(paymentDto.getOrderId())) {
                throw new HttpError(HttpStatus.CONFLICT, "Payment already exists for this order.");
            }

            Payment payment = new Payment();
            payment.setOrderId(paymentDto.getOrderId());
            payment.setUserId(paymentDto.getCustomerId());
            payment.setAmount(paymentDto.getAmount());
            payment.setStatus(PaymentStatus.PENDING);



            return paymentRepository.save(payment);
        }catch (HttpError e){
            throw  e;
        }
    }

    @Override
    public Payment findByOrderId(UUID orderId) {
        return null;
    }

    @Override
    public Payment updatePaymentStatus(UpdateStatus updateStatus) {
        try{
           Payment payment = paymentRepository.findByOrderId(updateStatus.getOrderId());
           if(payment == null) {
               throw new HttpError(HttpStatus.NOT_FOUND, "Payment not found for the given order ID.");
           }

           if(payment.getStatus() == PaymentStatus.COMPLETED) {
               throw new HttpError(HttpStatus.BAD_REQUEST, "Cannot update status of a completed payment.");
           }
           payment.setStatus(updateStatus.getStatus());

           return paymentRepository.save(payment);
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public List<Payment> findAllByUser(UserDto user) {
        try{
            List<Payment>payments = paymentRepository.findByUserId(user.getId());
            if(payments.isEmpty()) {
                throw new HttpError(HttpStatus.NOT_FOUND, "No payments found for the user.");
            }
            return payments;
        }catch (HttpError e) {
            throw e;
        }
    }


}
