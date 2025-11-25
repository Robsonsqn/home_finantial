package com.example.moneymoney.infrastructure.security;

import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private final UserRepositoryPort userRepository;

    public AuthenticationHelper(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
