package com.food.paymentservice.domain.models;

import com.food.paymentservice.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private Double amount;
    private String currency;
    private String stripePaymentId;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}
