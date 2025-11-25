package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.application.dto.FinanceSummaryDTO;
import com.example.moneymoney.application.service.FinanceService;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/users/me/finances")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping
    public ResponseEntity<FinanceSummaryDTO> getMonthlySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        UserJpaEntity userEntity = (UserJpaEntity) userDetails;
        User user = toDomain(userEntity);

        LocalDate now = LocalDate.now();
        int targetMonth = month != null ? month : now.getMonthValue();
        int targetYear = year != null ? year : now.getYear();

        FinanceSummaryDTO summary = financeService.getMonthlySummary(user, targetMonth, targetYear);
        return ResponseEntity.ok(summary);
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPasswordHash(),
                entity.getIncome());
    }
}
