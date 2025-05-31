package com.food.authservice.domains.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "tokens")
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Date timestamp;
    private Boolean isValid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    public Token(User user, String token){
        this.user = user;
        this.token = token;
        this.timestamp = Date.from(Instant.now());
        this.isValid = true;
    }

}
