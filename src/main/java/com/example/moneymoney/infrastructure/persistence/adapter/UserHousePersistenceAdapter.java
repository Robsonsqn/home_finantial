package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.UserHouseRepositoryPort;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.HouseRole;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.domain.model.UserHouse;
import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserHouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.UserHouseJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserHousePersistenceAdapter implements UserHouseRepositoryPort {

    private final UserHouseJpaRepository userHouseJpaRepository;

    public UserHousePersistenceAdapter(UserHouseJpaRepository userHouseJpaRepository) {
        this.userHouseJpaRepository = userHouseJpaRepository;
    }

    @Override
    public UserHouse save(UserHouse userHouse) {
        UserHouseJpaEntity entity = toEntity(userHouse);
        UserHouseJpaEntity savedEntity = userHouseJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public List<UserHouse> findByUser(User user) {
        UserJpaEntity userEntity = toEntity(user);
        return userHouseJpaRepository.findByUser(userEntity).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserHouse> findByHouse(House house) {
        HouseJpaEntity houseEntity = toEntity(house);
        return userHouseJpaRepository.findByHouse(houseEntity).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserHouse> findByHouseAndUser(House house, User user) {
        HouseJpaEntity houseEntity = toEntity(house);
        UserJpaEntity userEntity = toEntity(user);
        return userHouseJpaRepository.findByHouseAndUser(houseEntity, userEntity).map(this::toDomain);
    }

    @Override
    public boolean existsByHouseAndUser(House house, User user) {
        HouseJpaEntity houseEntity = toEntity(house);
        UserJpaEntity userEntity = toEntity(user);
        return userHouseJpaRepository.existsByHouseAndUser(houseEntity, userEntity);
    }

    @Override
    public boolean existsByUserIdAndHouseId(Long userId, Long houseId) {
        return userHouseJpaRepository.existsByUserIdAndHouseId(userId, houseId);
    }

    @Override
    public void delete(UserHouse userHouse) {
        UserHouseJpaEntity entity = toEntity(userHouse);
        userHouseJpaRepository.delete(entity);
    }

    private UserHouseJpaEntity toEntity(UserHouse domain) {
        UserHouseJpaEntity entity = new UserHouseJpaEntity();
        entity.setId(domain.getId());
        entity.setUser(toEntity(domain.getUser()));
        entity.setHouse(toEntity(domain.getHouse()));
        entity.setRole(domain.getRole());
        return entity;
    }

    private UserHouse toDomain(UserHouseJpaEntity entity) {
        return new UserHouse(entity.getId(), toDomain(entity.getUser()), toDomain(entity.getHouse()), entity.getRole());
    }

    private UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setIncome(user.getIncome());
        return entity;
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPasswordHash(),
                entity.getIncome());
    }

    private HouseJpaEntity toEntity(House house) {
        return new HouseJpaEntity(house.getId(), house.getName());
    }

    private House toDomain(HouseJpaEntity entity) {
        return new House(entity.getId(), entity.getName());
    }
}
