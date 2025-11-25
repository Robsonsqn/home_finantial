package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.MonthlyContributionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyContributionJpaRepository extends JpaRepository<MonthlyContributionJpaEntity, Long> {
    void deleteByHouseAndMonthAndYear(HouseJpaEntity house, int month, int year);
}
