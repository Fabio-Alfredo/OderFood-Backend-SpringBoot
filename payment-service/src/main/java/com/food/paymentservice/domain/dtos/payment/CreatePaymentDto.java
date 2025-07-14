package com.food.paymentservice.domain.dtos.payment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;


@Data
public class CreatePaymentDto {

    @NotEmpty(message = "Payment method ID cannot be empty")
    private UUID customerId;
    @NotEmpty(message = "Order ID cannot be empty")
    private UUID id;
    @NotEmpty(message = "Amount cannot be empty")
    private Double total;

}
