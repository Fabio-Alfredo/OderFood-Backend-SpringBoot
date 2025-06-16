package com.food.orderservice.domain.enums;

public enum StatusOrder {
    CREATED,
    SENT_TO_KITCHEN,
    COMPLETED,
    DELIVERED,
    PENDING,
    PAYMENT_PENDING,
    PAYMENT_CONFIRMED,
    CANCELLED;

    public static StatusOrder fromString(String status){
        for (StatusOrder statusOrder: StatusOrder.values()){
            if(statusOrder.name().equalsIgnoreCase(status)) {
                return statusOrder;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
