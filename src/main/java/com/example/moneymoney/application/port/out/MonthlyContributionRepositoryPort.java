package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.MonthlyContribution;
import com.example.moneymoney.domain.model.User;
import java.util.List;

public interface MonthlyContributionRepositoryPort {
    void deleteByHouseAndMonthAndYear(House house, int month, int year);

    MonthlyContribution save(MonthlyContribution contribution);

    void flush();

    java.util.Optional<MonthlyContribution> findByHouseAndUserAndMonthAndYear(House house,
            com.example.moneymoney.domain.model.User user, int month, int year);
}
