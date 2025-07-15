package com.food.paymentservice.services.impl;

import com.food.paymentservice.domain.models.Payment;
import com.food.paymentservice.services.contrat.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {
    @Override
    public String createPaymentStripe(Payment payment) {
            long amountInCents = (long) (payment.getAmount() * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(payment.getCurrency())
                    // No confirmamos aquí, solo creamos
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            try {
                PaymentIntent paymentIntent = PaymentIntent.create(params);
                return paymentIntent.getClientSecret();  // Esto se envía al frontend
            } catch (StripeException e) {
                throw new RuntimeException("Error creating payment intent: " + e.getMessage(), e);
            }
        }
}
