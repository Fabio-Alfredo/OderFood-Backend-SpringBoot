package com.food.paymentservice.services.contrat;

import com.food.paymentservice.domain.models.Payment;

public interface StripeService {

    String createPaymentStripe(Payment payment);

}
