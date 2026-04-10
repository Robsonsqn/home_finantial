package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.PiggyBank;
import com.example.moneymoney.domain.model.PiggyBankDeposit;
import com.example.moneymoney.domain.model.PiggyBankTarget;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PiggyBankRepositoryPort {
    PiggyBank save(PiggyBank piggyBank);

    Optional<PiggyBank> findById(Long id);

    List<PiggyBank> findByHouseId(Long houseId);

    List<PiggyBank> findByOwnerId(Long ownerId);

    PiggyBankTarget saveTarget(PiggyBankTarget target);

    List<PiggyBankTarget> findTargetsByPiggyBankId(Long piggyBankId);

    PiggyBankDeposit saveDeposit(PiggyBankDeposit deposit);

    List<PiggyBankDeposit> findDepositsByPiggyBankId(Long piggyBankId);

    BigDecimal getTotalBalance(Long piggyBankId);

    BigDecimal getUserContribution(Long piggyBankId, Long userId);
}
