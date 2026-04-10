package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.PiggyBankRepositoryPort;
import com.example.moneymoney.domain.model.PiggyBank;
import com.example.moneymoney.domain.model.PiggyBankDeposit;
import com.example.moneymoney.domain.model.PiggyBankTarget;
import com.example.moneymoney.infrastructure.persistence.entity.PiggyBankDepositJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.PiggyBankJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.PiggyBankTargetJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.SpringDataPiggyBankDepositRepository;
import com.example.moneymoney.infrastructure.persistence.repository.SpringDataPiggyBankRepository;
import com.example.moneymoney.infrastructure.persistence.repository.SpringDataPiggyBankTargetRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PiggyBankPersistenceAdapter implements PiggyBankRepositoryPort {

    private final SpringDataPiggyBankRepository piggyBankRepository;
    private final SpringDataPiggyBankTargetRepository targetRepository;
    private final SpringDataPiggyBankDepositRepository depositRepository;

    public PiggyBankPersistenceAdapter(SpringDataPiggyBankRepository piggyBankRepository,
            SpringDataPiggyBankTargetRepository targetRepository,
            SpringDataPiggyBankDepositRepository depositRepository) {
        this.piggyBankRepository = piggyBankRepository;
        this.targetRepository = targetRepository;
        this.depositRepository = depositRepository;
    }

    @Override
    public PiggyBank save(PiggyBank piggyBank) {
        PiggyBankJpaEntity entity = PiggyBankJpaEntity.fromDomain(piggyBank);
        return piggyBankRepository.save(entity).toDomain();
    }

    @Override
    public Optional<PiggyBank> findById(Long id) {
        return piggyBankRepository.findById(id).map(PiggyBankJpaEntity::toDomain);
    }

    @Override
    public List<PiggyBank> findByHouseId(Long houseId) {
        return piggyBankRepository.findByHouseId(houseId).stream()
                .map(PiggyBankJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PiggyBank> findByOwnerId(Long ownerId) {
        return piggyBankRepository.findByOwnerId(ownerId).stream()
                .map(PiggyBankJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PiggyBankTarget saveTarget(PiggyBankTarget target) {
        PiggyBankTargetJpaEntity entity = PiggyBankTargetJpaEntity.fromDomain(target);
        return targetRepository.save(entity).toDomain();
    }

    @Override
    public List<PiggyBankTarget> findTargetsByPiggyBankId(Long piggyBankId) {
        return targetRepository.findByPiggyBankId(piggyBankId).stream()
                .map(PiggyBankTargetJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PiggyBankDeposit saveDeposit(PiggyBankDeposit deposit) {
        PiggyBankDepositJpaEntity entity = PiggyBankDepositJpaEntity.fromDomain(deposit);
        return depositRepository.save(entity).toDomain();
    }

    @Override
    public List<PiggyBankDeposit> findDepositsByPiggyBankId(Long piggyBankId) {
        return depositRepository.findByPiggyBankId(piggyBankId).stream()
                .map(PiggyBankDepositJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalBalance(Long piggyBankId) {
        BigDecimal balance = depositRepository.getTotalBalance(piggyBankId);
        return balance != null ? balance : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getUserContribution(Long piggyBankId, Long userId) {
        BigDecimal contribution = depositRepository.getUserContribution(piggyBankId, userId);
        return contribution != null ? contribution : BigDecimal.ZERO;
    }
}
