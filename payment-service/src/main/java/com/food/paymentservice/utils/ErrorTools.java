package com.food.paymentservice.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorTools {

    public Map<String, List<String>>mapErrors(List<FieldError>errors){
        Map<String, List<String>> errorsMap = new HashMap<>();

        errors.forEach(e ->{
            List<String>_errors = errorsMap.getOrDefault(e.getField(), new ArrayList<>());
            _errors.add(e.getDefaultMessage());
            errorsMap.put(e.getField(), _errors);
        });

        return errorsMap;
    }
}
