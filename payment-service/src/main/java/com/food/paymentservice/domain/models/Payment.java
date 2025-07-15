package com.food.paymentservice.domain.models;

import com.food.paymentservice.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private Double amount;
    private String currency;
    private String stripePaymentId;
    private String paymentMethodId;
    private String description;
    private PaymentStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();
}
