package com.example.moneymoney.application.port.in;

import com.example.moneymoney.application.dto.PiggyBankViewDTO;
import java.util.List;

public interface GetPiggyBankUseCase {
    PiggyBankViewDTO getById(Long id);

    List<PiggyBankViewDTO> getByHouseId(Long houseId);

    List<PiggyBankViewDTO> getMyPiggyBanks();
}
