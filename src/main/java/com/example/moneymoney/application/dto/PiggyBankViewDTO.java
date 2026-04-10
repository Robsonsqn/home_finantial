package com.example.moneymoney.application.dto;

import com.example.moneymoney.domain.model.ContributionType;
import com.example.moneymoney.domain.model.PiggyBankType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record PiggyBankViewDTO(
        Long id,
        String name,
        String description,
        PiggyBankType type,
        ContributionType contributionType,
        LocalDate targetDate,
        BigDecimal currentBalance,
        BigDecimal myContribution,
        Double completionPercentage,
        Map<String, BigDecimal> breakdown // User name -> Amount contributed (or target vs contributed)
) {
}
