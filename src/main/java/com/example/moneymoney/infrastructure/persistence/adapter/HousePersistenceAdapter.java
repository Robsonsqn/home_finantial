package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.HouseRepositoryPort;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.HouseJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HousePersistenceAdapter implements HouseRepositoryPort {

    private final HouseJpaRepository houseJpaRepository;

    public HousePersistenceAdapter(HouseJpaRepository houseJpaRepository) {
        this.houseJpaRepository = houseJpaRepository;
    }

    @Override
    public House save(House house) {
        HouseJpaEntity entity = toEntity(house);
        HouseJpaEntity savedEntity = houseJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<House> findById(Long id) {
        return houseJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<House> findAll() {
        return houseJpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return houseJpaRepository.existsById(id);
    }

    private HouseJpaEntity toEntity(House house) {
        return new HouseJpaEntity(house.getId(), house.getName());
    }

    private House toDomain(HouseJpaEntity entity) {
        return new House(entity.getId(), entity.getName());
    }
}
