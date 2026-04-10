package com.example.moneymoney.infrastructure.security;

import com.example.moneymoney.application.port.out.LoadUserPort;
import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserAdapter implements LoadUserPort {

    private final UserRepositoryPort userRepository;

    public SecurityUserAdapter(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserJpaEntity) {
            return ((UserJpaEntity) principal).toDomain();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("Unknown principal type");
    }
}
