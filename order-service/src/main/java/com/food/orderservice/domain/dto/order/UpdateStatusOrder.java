package com.food.orderservice.domain.dto.order;

import com.food.orderservice.domain.enums.StatusOrder;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusOrder {
    private UUID orderId;
    private StatusOrder status;
}
