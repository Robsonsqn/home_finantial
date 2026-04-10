package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.PiggyBankTargetJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPiggyBankTargetRepository extends JpaRepository<PiggyBankTargetJpaEntity, Long> {
    List<PiggyBankTargetJpaEntity> findByPiggyBankId(Long piggyBankId);
}
