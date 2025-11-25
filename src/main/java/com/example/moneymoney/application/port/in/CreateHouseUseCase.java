package com.example.moneymoney.application.port.in;

import com.example.moneymoney.application.dto.HouseCreateDTO;
import com.example.moneymoney.application.dto.HouseResponseDTO;
import com.example.moneymoney.domain.model.User;

public interface CreateHouseUseCase {
    HouseResponseDTO createHouse(HouseCreateDTO dto, User creator);
}
