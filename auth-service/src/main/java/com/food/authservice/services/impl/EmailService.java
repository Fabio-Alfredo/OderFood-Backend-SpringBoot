package com.food.authservice.services.impl;

import com.food.authservice.exceptions.HttpError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoveryEmail(String to, String token) {
        try{
        String subject = "Recuperación de contraseña - Food Auth Service";
        String resetUrl = "https://tudominio.com/recover-password?token=" + token;

        String body = """
                Hola,

                Hemos recibido una solicitud para restablecer tu contraseña. 
                Para continuar con el proceso, haz clic en el siguiente enlace:

                %s

                Si no solicitaste esta recuperación, puedes ignorar este mensaje.

                Atentamente,
                El equipo de soporte.
                """.formatted(resetUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        } catch (MailException e) {
            System.out.println(e.getMessage());
           throw new HttpError(HttpStatus.NOT_FOUND, "Error al enviar mail de recuperacion");
        }
    }
}

