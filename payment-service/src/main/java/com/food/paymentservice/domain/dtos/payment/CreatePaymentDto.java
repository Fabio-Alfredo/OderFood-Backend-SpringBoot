package com.food.paymentservice.domain.dtos.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePaymentDto {

    @NotEmpty(message = "Payment method ID cannot be empty")
    private UUID customerId;
    @NotEmpty(message = "Order ID cannot be empty")
    private UUID id;
    @NotEmpty(message = "Amount cannot be empty")
    private Double total;

}
