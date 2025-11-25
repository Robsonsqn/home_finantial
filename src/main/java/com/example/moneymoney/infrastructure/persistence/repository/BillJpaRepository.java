package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.BillJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BillJpaRepository extends JpaRepository<BillJpaEntity, Long> {
    List<BillJpaEntity> findByHouseAndDueDateBetween(HouseJpaEntity house, LocalDate start, LocalDate end);

    List<BillJpaEntity> findByUserAndHouseIsNullAndDueDateBetween(UserJpaEntity user, LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BillJpaEntity b WHERE b.house = :house AND b.dueDate BETWEEN :start AND :end")
    BigDecimal sumByHouseAndDueDateBetween(@Param("house") HouseJpaEntity house, @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BillJpaEntity b WHERE b.user = :user AND b.house IS NULL AND b.dueDate BETWEEN :start AND :end")
    BigDecimal sumByUserAndHouseIsNullAndDueDateBetween(@Param("user") UserJpaEntity user,
            @Param("start") LocalDate start, @Param("end") LocalDate end);
}
