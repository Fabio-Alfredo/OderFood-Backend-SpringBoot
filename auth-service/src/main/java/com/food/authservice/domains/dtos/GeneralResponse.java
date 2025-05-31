package com.food.authservice.domains.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
    private String message;
    private Object data;

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus httpStatus, String message, Object data){
        return new ResponseEntity<>(
                new GeneralResponse(message, data),
                httpStatus
        );
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus httpStatus, String message) {
        return getResponse(httpStatus, message, null);
    }

    public static ResponseEntity<GeneralResponse>getResponse(HttpStatus httpStatus, Object data){
        return getResponse(httpStatus, httpStatus.getReasonPhrase(), data);
    }
}
