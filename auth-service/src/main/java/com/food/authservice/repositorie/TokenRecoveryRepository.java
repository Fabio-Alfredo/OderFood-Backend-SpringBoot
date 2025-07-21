package com.food.authservice.repositorie;

import com.food.authservice.domains.models.PasswordRecoveryToken;
import com.food.authservice.domains.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRecoveryRepository extends JpaRepository<PasswordRecoveryToken, UUID> {
    Optional<PasswordRecoveryToken> findByUserAndTokenAndCanActive(User user, String token, Boolean canActive);
}
