package com.example.moneymoney.application.port.in;

import com.example.moneymoney.application.dto.PiggyBankCreateDTO;
import com.example.moneymoney.application.dto.PiggyBankViewDTO;

public interface CreatePiggyBankUseCase {
    PiggyBankViewDTO create(PiggyBankCreateDTO dto);
}
