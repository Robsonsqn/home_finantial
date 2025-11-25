package com.example.moneymoney.infrastructure.security;

import com.example.moneymoney.application.port.out.TokenGeneratorPort;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final JwtService jwtService;

    public JwtTokenAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generateToken(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setName(user.getName());
        entity.setIncome(user.getIncome());

        return jwtService.generateToken(entity);
    }
}
