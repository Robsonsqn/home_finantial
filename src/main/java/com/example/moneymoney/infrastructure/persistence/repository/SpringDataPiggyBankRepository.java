package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.PiggyBankJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPiggyBankRepository extends JpaRepository<PiggyBankJpaEntity, Long> {
    List<PiggyBankJpaEntity> findByHouseId(Long houseId);

    List<PiggyBankJpaEntity> findByOwnerId(Long ownerId);
}
