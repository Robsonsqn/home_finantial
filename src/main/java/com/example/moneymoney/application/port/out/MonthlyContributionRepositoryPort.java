package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.MonthlyContribution;
import java.util.List;

public interface MonthlyContributionRepositoryPort {
    void deleteByHouseAndMonthAndYear(House house, int month, int year);

    MonthlyContribution save(MonthlyContribution contribution);

    void flush();
}
