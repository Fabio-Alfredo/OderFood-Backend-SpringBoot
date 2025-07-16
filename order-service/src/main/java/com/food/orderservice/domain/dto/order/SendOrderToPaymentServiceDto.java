package com.food.orderservice.domain.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOrderToPaymentServiceDto {

    @NotEmpty(message = "Payment method ID cannot be empty")
    private UUID customerId;
    @NotEmpty(message = "Order ID cannot be empty")
    private UUID orderId;
    @NotEmpty(message = "Amount cannot be empty")
    private Double amount;

}
