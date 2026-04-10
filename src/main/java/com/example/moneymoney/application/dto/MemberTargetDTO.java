package com.example.moneymoney.application.dto;

import java.math.BigDecimal;

public record MemberTargetDTO(
        Long userId,
        BigDecimal targetAmount) {
}
