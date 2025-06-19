package com.food.paymentservice.domain.dtos.payment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;


@Data
public class CreatePaymentDto {
    @NotEmpty(message = "Order ID cannot be empty")
    private UUID orderId;
    @NotEmpty(message = "User ID cannot be empty")
    private String paymentMethodId;
    private String currency;
    @NotEmpty(message = "Amount cannot be empty")
    private Double amount;

}
