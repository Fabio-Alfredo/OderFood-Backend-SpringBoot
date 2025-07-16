package com.food.authservice.domains.enums;


public enum TokenType {

    RECOVERY_PASSWORD,
    AUTHENTICATION;

    public static TokenType fromString(String type) {
        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.name().equalsIgnoreCase(type)) {
                return tokenType;
            }
        }
        throw new IllegalArgumentException("Unknown token type: " + type);
    }
}
