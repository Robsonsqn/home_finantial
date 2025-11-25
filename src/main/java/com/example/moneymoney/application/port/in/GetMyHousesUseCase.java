package com.example.moneymoney.application.port.in;

import com.example.moneymoney.application.dto.HouseResponseDTO;
import com.example.moneymoney.domain.model.User;
import java.util.List;

public interface GetMyHousesUseCase {
    List<HouseResponseDTO> getMyHouses(User user);
}
