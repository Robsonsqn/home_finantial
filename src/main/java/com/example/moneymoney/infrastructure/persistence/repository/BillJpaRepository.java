package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.BillJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface BillJpaRepository extends JpaRepository<BillJpaEntity, Long> {
    List<BillJpaEntity> findByHouseAndDueDateBetween(HouseJpaEntity house, LocalDate start, LocalDate end);

    List<BillJpaEntity> findByUserAndHouseIsNullAndDueDateBetween(UserJpaEntity user, LocalDate start, LocalDate end);
}
