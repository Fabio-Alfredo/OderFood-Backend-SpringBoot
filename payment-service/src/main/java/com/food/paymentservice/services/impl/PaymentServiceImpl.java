package com.food.paymentservice.services.impl;

import com.food.paymentservice.Exceptions.HttpError;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment confirmPayment(CreatePaymentDto paymentDto) {
        return null;
    }

    @Override
    public Payment createPayment(CreatePaymentDto paymentDto) {
        try{
            Payment payment = new Payment();
            payment.setUserId(paymentDto.getCustomerId());
            payment.setOrderId(paymentDto.getId());
            payment.setAmount(paymentDto.getTotal());
            payment.setStatus(PaymentStatus.PENDING);

            return paymentRepository.save(payment);
        }catch (HttpError e){
            throw  e;
        }
    }
}
