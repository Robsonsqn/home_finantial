package com.example.moneymoney.application.dto.user;

import java.math.BigDecimal;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        BigDecimal income) {
    public static UserResponseDTO fromDomain(
            com.example.moneymoney.domain.model.User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getIncome());
    }
}
