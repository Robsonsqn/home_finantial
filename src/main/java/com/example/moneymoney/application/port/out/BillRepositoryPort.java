package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.Bill;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepositoryPort {
    Bill save(Bill bill);

    Optional<Bill> findById(Long id);

    List<Bill> findByHouseAndDueDateBetween(House house, LocalDate start, LocalDate end);

    List<Bill> findByUserAndHouseIsNullAndDueDateBetween(User user, LocalDate start, LocalDate end);

    BigDecimal sumByHouseAndDueDateBetween(House house, LocalDate start, LocalDate end);

    BigDecimal sumByUserAndHouseIsNullAndDueDateBetween(User user, LocalDate start, LocalDate end);
}
