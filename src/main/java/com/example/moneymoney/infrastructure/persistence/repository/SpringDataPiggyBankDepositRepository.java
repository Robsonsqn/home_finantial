package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.PiggyBankDepositJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SpringDataPiggyBankDepositRepository extends JpaRepository<PiggyBankDepositJpaEntity, Long> {
    List<PiggyBankDepositJpaEntity> findByPiggyBankId(Long piggyBankId);

    @Query("SELECT SUM(d.amount) FROM PiggyBankDepositJpaEntity d WHERE d.piggyBank.id = :piggyBankId")
    BigDecimal getTotalBalance(Long piggyBankId);

    @Query("SELECT SUM(d.amount) FROM PiggyBankDepositJpaEntity d WHERE d.piggyBank.id = :piggyBankId AND d.user.id = :userId")
    BigDecimal getUserContribution(Long piggyBankId, Long userId);
}
