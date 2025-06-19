package com.food.paymentservice.services.contrat;

import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.models.Payment;

public interface PaymentService {
    Payment createPayment(CreatePaymentDto paymentDto);
}
