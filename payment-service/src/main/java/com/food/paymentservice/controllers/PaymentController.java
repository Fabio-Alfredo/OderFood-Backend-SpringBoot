package com.food.paymentservice.controllers;

import com.food.paymentservice.Exceptions.HttpError;
import com.food.paymentservice.domain.commons.GeneralResponse;
import com.food.paymentservice.domain.dtos.auth.UserDto;
import com.food.paymentservice.domain.dtos.payment.ConfirmPaymentDto;
import com.food.paymentservice.domain.dtos.payment.CreatePaymentDto;
import com.food.paymentservice.domain.models.Payment;
import com.food.paymentservice.services.contrat.AuthService;
import com.food.paymentservice.services.contrat.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthService authService;

    public PaymentController(PaymentService paymentService, AuthService authService) {
        this.paymentService = paymentService;
        this.authService = authService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> confirmPayment(@RequestBody ConfirmPaymentDto paymentDto){
        try{
            UserDto user = authService.getUserAuthenticated();
            Payment payment = paymentService.confirmPayment(paymentDto, user);

            return GeneralResponse.getResponse(HttpStatus.ACCEPTED, "Payment created successfully", payment);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getHttpStatus(), e.getMessage());
        }
    }


}

