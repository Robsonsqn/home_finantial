package com.example.moneymoney.application.port.in;

import com.example.moneymoney.application.dto.PiggyBankViewDTO;
import java.math.BigDecimal;

public interface DepositUseCase {
    PiggyBankViewDTO deposit(Long piggyBankId, BigDecimal amount);
}
