package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.BillResponseDTO;
import com.example.moneymoney.application.dto.FinanceSummaryDTO;
import com.example.moneymoney.application.port.out.BillRepositoryPort;
import com.example.moneymoney.application.port.out.MonthlyContributionRepositoryPort;
import com.example.moneymoney.application.port.out.UserHouseRepositoryPort;
import com.example.moneymoney.domain.model.Bill;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.MonthlyContribution;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.domain.model.UserHouse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final BillRepositoryPort billRepository;
    private final UserHouseRepositoryPort userHouseRepository;
    private final MonthlyContributionRepositoryPort monthlyContributionRepository;
    private final ContributionService contributionService;

    public FinanceService(BillRepositoryPort billRepository,
            UserHouseRepositoryPort userHouseRepository,
            MonthlyContributionRepositoryPort monthlyContributionRepository,
            ContributionService contributionService) {
        this.billRepository = billRepository;
        this.userHouseRepository = userHouseRepository;
        this.monthlyContributionRepository = monthlyContributionRepository;
        this.contributionService = contributionService;
    }

    @Transactional(readOnly = true)
    public FinanceSummaryDTO getMonthlySummary(User user, int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        // 1. Personal Bills
        BigDecimal personalTotal = billRepository.sumByUserAndHouseIsNullAndDueDateBetween(user, start, end);
        if (personalTotal == null)
            personalTotal = BigDecimal.ZERO;

        List<BillResponseDTO> personalBills = billRepository.findByUserAndHouseIsNullAndDueDateBetween(user, start, end)
                .stream().map(this::toDTO).collect(Collectors.toList());

        // 2. Find User's House (Assuming 1 house for now)
        List<UserHouse> userHouses = userHouseRepository.findByUser(user);
        if (userHouses.isEmpty()) {
            return new FinanceSummaryDTO(personalTotal, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    personalTotal, personalBills, Collections.emptyList());
        }
        House house = userHouses.get(0).getHouse(); // Taking the first one

        // 3. House Bills
        BigDecimal houseTotalExpenses = billRepository.sumByHouseAndDueDateBetween(house, start, end);
        if (houseTotalExpenses == null)
            houseTotalExpenses = BigDecimal.ZERO;

        List<BillResponseDTO> houseBills = billRepository.findByHouseAndDueDateBetween(house, start, end)
                .stream().map(this::toDTO).collect(Collectors.toList());

        // 4. Determine Percentage
        BigDecimal percentage;
        Optional<MonthlyContribution> contribution = monthlyContributionRepository
                .findByHouseAndUserAndMonthAndYear(house, user, month, year);
        if (contribution.isPresent()) {
            percentage = contribution.get().getPercentage();
        } else {
            percentage = contributionService.calculateCurrentPercentage(user, house);
        }
        // 5. Calculate Shares
        BigDecimal userHouseShare = houseTotalExpenses.multiply(percentage).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal grandTotal = personalTotal.add(userHouseShare);

        return new FinanceSummaryDTO(
                personalTotal,
                houseTotalExpenses,
                percentage,
                userHouseShare,
                grandTotal,
                personalBills,
                houseBills);
    }

    private BillResponseDTO toDTO(Bill bill) {
        return new BillResponseDTO(
                bill.getId(),
                bill.getDescription(),
                bill.getAmount(),
                bill.getDueDate(),
                bill.getType(),
                bill.isPaid(),
                bill.getHouse() != null ? bill.getHouse().getId() : null,
                bill.getUser() != null ? bill.getUser().getId() : null);
    }
}
