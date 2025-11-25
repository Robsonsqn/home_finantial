package com.example.moneymoney.application.service;

import com.example.moneymoney.application.port.out.MonthlyContributionRepositoryPort;
import com.example.moneymoney.application.port.out.UserHouseRepositoryPort;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.MonthlyContribution;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.domain.model.UserHouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ContributionService {

    private static final Logger logger = LoggerFactory.getLogger(ContributionService.class);
    private final MonthlyContributionRepositoryPort monthlyContributionRepository;
    private final UserHouseRepositoryPort userHouseRepository;

    public ContributionService(MonthlyContributionRepositoryPort monthlyContributionRepository,
            UserHouseRepositoryPort userHouseRepository) {
        this.monthlyContributionRepository = monthlyContributionRepository;
        this.userHouseRepository = userHouseRepository;
    }

    @Transactional
    public void calculateForHouse(House house, int month, int year) {
        logger.info("Starting contribution calculation for House ID: {}, Month: {}, Year: {}", house.getId(), month,
                year);

        monthlyContributionRepository.deleteByHouseAndMonthAndYear(house, month, year);
        monthlyContributionRepository.flush();

        List<UserHouse> members = userHouseRepository.findByHouse(house);
        if (members.isEmpty()) {
            logger.warn("No members found for House ID: {}. Skipping calculation.", house.getId());
            return;
        }

        BigDecimal totalIncome = BigDecimal.ZERO;
        for (UserHouse uh : members) {
            totalIncome = totalIncome.add(uh.getUser().getIncome());
        }
        logger.info("Total income for House ID: {} is {}", house.getId(), totalIncome);

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("Total income is ZERO. Setting contributions to ZERO for all members.");
            for (UserHouse uh : members) {
                saveContribution(uh.getUser(), house, month, year, BigDecimal.ZERO);
            }
        } else {
            for (UserHouse uh : members) {
                BigDecimal userIncome = uh.getUser().getIncome();
                BigDecimal percentage = userIncome.divide(totalIncome, 4, RoundingMode.HALF_EVEN);
                logger.debug("User ID: {} Income: {} Percentage: {}", uh.getUser().getId(), userIncome, percentage);
                saveContribution(uh.getUser(), house, month, year, percentage);
            }
        }
        logger.info("Contribution calculation completed for House ID: {}", house.getId());
    }

    private void saveContribution(User user, House house, int month, int year, BigDecimal percentage) {
        MonthlyContribution contribution = new MonthlyContribution(month, year, percentage, user, house);
        monthlyContributionRepository.save(contribution);
    }
}
