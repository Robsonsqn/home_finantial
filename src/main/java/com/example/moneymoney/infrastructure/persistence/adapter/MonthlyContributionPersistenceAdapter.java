package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.MonthlyContributionRepositoryPort;
import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.MonthlyContribution;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.MonthlyContributionJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.MonthlyContributionJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class MonthlyContributionPersistenceAdapter implements MonthlyContributionRepositoryPort {

    private final MonthlyContributionJpaRepository repository;

    public MonthlyContributionPersistenceAdapter(MonthlyContributionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteByHouseAndMonthAndYear(House house, int month, int year) {
        HouseJpaEntity houseEntity = toEntity(house);
        repository.deleteByHouseAndMonthAndYear(houseEntity, month, year);
    }

    @Override
    public java.util.Optional<MonthlyContribution> findByHouseAndUserAndMonthAndYear(House house, User user, int month,
            int year) {
        HouseJpaEntity houseEntity = toEntity(house);
        UserJpaEntity userEntity = toEntity(user);
        return repository.findByHouseAndUserAndMonthAndYear(houseEntity, userEntity, month, year).map(this::toDomain);
    }

    @Override
    public MonthlyContribution save(MonthlyContribution contribution) {
        MonthlyContributionJpaEntity entity = toEntity(contribution);
        MonthlyContributionJpaEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    private MonthlyContributionJpaEntity toEntity(MonthlyContribution domain) {
        MonthlyContributionJpaEntity entity = new MonthlyContributionJpaEntity();
        entity.setId(domain.getId());
        entity.setMonth(domain.getMonth());
        entity.setYear(domain.getYear());
        entity.setPercentage(domain.getPercentage());
        entity.setUser(toEntity(domain.getUser()));
        entity.setHouse(toEntity(domain.getHouse()));
        return entity;
    }

    private MonthlyContribution toDomain(MonthlyContributionJpaEntity entity) {
        return new MonthlyContribution(entity.getId(), entity.getMonth(), entity.getYear(), entity.getPercentage(),
                toDomain(entity.getUser()), toDomain(entity.getHouse()));
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
