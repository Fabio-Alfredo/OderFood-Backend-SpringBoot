package com.food.paymentservice.domain.dtos.payment;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmPaymentDto {

    private UUID orderId;
    private String currency;
    private String description;
    private String paymentMethodId;

}
