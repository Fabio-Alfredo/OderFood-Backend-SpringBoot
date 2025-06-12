package com.food.orderservice.domain.dto.common;

import lombok.Data;

@Data
public class ApisResponse<T> {
    private String message;
    private T data;
}
