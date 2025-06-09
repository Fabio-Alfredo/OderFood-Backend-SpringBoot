package com.food.orderservice.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpError extends RuntimeException {

    private final HttpStatus httpStatus;

    public HttpError(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
