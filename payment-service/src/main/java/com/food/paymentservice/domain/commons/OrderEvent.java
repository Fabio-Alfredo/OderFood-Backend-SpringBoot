package com.food.paymentservice.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent<T> {
    private String eventType;
    private T data;
}
