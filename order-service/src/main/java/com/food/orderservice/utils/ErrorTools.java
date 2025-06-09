package com.food.orderservice.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorTools {
    public Map<String, List<String>> mapErrors(List<FieldError> errors){
        Map<String, List<String>> errorsMap = new HashMap<>();

        errors.forEach(error ->{
            List<String>_erros = errorsMap.getOrDefault(error.getField(), new ArrayList<>());
            _erros.add(error.getDefaultMessage());
            errorsMap.put(error.getField(), _erros);
        });

        return errorsMap;
    }

}
