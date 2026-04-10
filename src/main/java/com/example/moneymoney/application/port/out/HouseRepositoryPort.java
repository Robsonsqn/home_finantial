package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.House;
import java.util.Optional;

public interface HouseRepositoryPort {
    House save(House house);

    Optional<House> findById(Long id);

    java.util.List<House> findAll();

    boolean existsById(Long id);
}
