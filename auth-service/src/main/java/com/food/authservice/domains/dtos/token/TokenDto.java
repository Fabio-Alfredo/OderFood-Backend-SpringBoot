package com.food.authservice.domains.dtos.token;

import com.food.authservice.domains.models.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {
    private String token;

    public TokenDto(Token token){
        this.token = token.getToken();
    }
}
