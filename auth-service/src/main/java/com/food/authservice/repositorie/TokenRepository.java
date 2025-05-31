package com.food.authservice.repositorie;

import com.food.authservice.domains.models.Token;
import com.food.authservice.domains.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token>findByUserAndIsValid(User user, Boolean isValid);

}
