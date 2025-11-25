package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.User;

public interface TokenGeneratorPort {
    String generateToken(User user);
}
