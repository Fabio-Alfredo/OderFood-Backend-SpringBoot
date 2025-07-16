package com.food.paymentservice.services.contrat;

import com.food.paymentservice.domain.dtos.auth.UserDto;
import com.food.paymentservice.domain.dtos.payment.ConfirmPaymentDto;
import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.dtos.payment.UpdateStatus;
import com.food.paymentservice.domain.models.Payment;

import java.util.UUID;

public interface PaymentService {
    Payment confirmPayment(ConfirmPaymentDto paymentDto, UserDto user);
    Payment createPayment(CreatePaymentDto paymentDto);
    Payment findByOrderId(UUID orderId);
    Payment updatePaymentStatus(UpdateStatus updateStatus);
}
