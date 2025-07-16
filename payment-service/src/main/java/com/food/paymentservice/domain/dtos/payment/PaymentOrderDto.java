package com.food.paymentservice.domain.dtos.payment;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentOrderDto {
    private UUID orderId;
    private String status;
}
