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
    public Payment createPayment(CreatePaymentDto paymentDto) {
        try{
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (paymentDto.getAmount() * 100))
                    .setCurrency(paymentDto.getCurrency() != null ? paymentDto.getCurrency() : "usd")
                    .setPaymentMethod(paymentDto.getPaymentMethodId()) // ID como "pm_..."
                    .setConfirm(true)
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setDescription("Payment for order " + paymentDto.getOrderId())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Payment payment = new Payment();
            payment.setOrderId(paymentDto.getOrderId());
            payment.setAmount(paymentDto.getAmount());
            payment.setCurrency(paymentDto.getCurrency() != null ? paymentDto.getCurrency() : "usd");
            payment.setStripePaymentId(intent.getId());
            payment.setStatus(PaymentStatus.COMPLETED);


            return paymentRepository.save(payment);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
