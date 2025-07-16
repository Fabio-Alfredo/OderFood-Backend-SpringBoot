package com.food.paymentservice.domain.dtos.payment;

import com.food.paymentservice.domain.enums.PaymentStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatus {
    private UUID orderId;
    private PaymentStatus status;
}
