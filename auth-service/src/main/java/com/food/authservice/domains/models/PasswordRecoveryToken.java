package com.food.authservice.domains.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="Password_recovery_token")
public class PasswordRecoveryToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 1024)
    private String token;
    @Column(nullable = false)
    private Boolean canActive = true;
    private Date timestamp = Date.from(Instant.now());

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;


}