package com.example.moneymoney.application.dto;

import com.example.moneymoney.domain.model.ContributionType;
import com.example.moneymoney.domain.model.PiggyBankType;
import java.time.LocalDate;
import java.util.List;

public record PiggyBankCreateDTO(
        String name,
        String description,
        PiggyBankType type,
        ContributionType contributionType,
        LocalDate targetDate,
        Long houseId, // Required if type is HOUSE
        List<MemberTargetDTO> targets // Required if contributionType is MANDATORY
) {
}
