package com.food.paymentservice.repositories;

import com.food.paymentservice.domain.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByOrderId(UUID orderId);
    Boolean existsByOrderId(UUID orderId);
}
