package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.MemberTargetDTO;
import com.example.moneymoney.application.dto.PiggyBankCreateDTO;
import com.example.moneymoney.application.dto.PiggyBankViewDTO;
import com.example.moneymoney.application.port.in.CreatePiggyBankUseCase;
import com.example.moneymoney.application.port.in.DepositUseCase;
import com.example.moneymoney.application.port.in.GetPiggyBankUseCase;
import com.example.moneymoney.application.port.out.LoadUserPort;
import com.example.moneymoney.application.port.out.PiggyBankRepositoryPort;
import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.model.*;
import com.example.moneymoney.infrastructure.persistence.repository.HouseJpaRepository; // Direct dependency for now, should be a port
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PiggyBankService implements CreatePiggyBankUseCase, DepositUseCase, GetPiggyBankUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;
    private final LoadUserPort loadUserPort;
    private final HouseJpaRepository houseRepository; // Should be a port
    private final UserRepositoryPort userRepository;

    public PiggyBankService(PiggyBankRepositoryPort piggyBankRepository, LoadUserPort loadUserPort,
            HouseJpaRepository houseRepository, UserRepositoryPort userRepository) {
        this.piggyBankRepository = piggyBankRepository;
        this.loadUserPort = loadUserPort;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PiggyBankViewDTO create(PiggyBankCreateDTO dto) {
        User currentUser = loadUserPort.loadUser();
        PiggyBank piggyBank = new PiggyBank();
        piggyBank.setName(dto.name());
        piggyBank.setDescription(dto.description());
        piggyBank.setType(dto.type());
        piggyBank.setContributionType(dto.contributionType());
        piggyBank.setTargetDate(dto.targetDate());
        piggyBank.setOwner(currentUser);

        if (dto.type() == PiggyBankType.HOUSE) {
            if (dto.houseId() == null) {
                throw new IllegalArgumentException("House ID is required for HOUSE piggy banks");
            }
            // TODO: Use a port for HouseRepository
            House house = houseRepository.findById(dto.houseId())
                    .orElseThrow(() -> new RuntimeException("House not found")).toDomain();
            piggyBank.setHouse(house);
        }

        PiggyBank savedPiggyBank = piggyBankRepository.save(piggyBank);

        if (dto.type() == PiggyBankType.HOUSE && dto.contributionType() == ContributionType.MANDATORY) {
            if (dto.targets() == null || dto.targets().isEmpty()) {
                throw new IllegalArgumentException("Targets are required for MANDATORY HOUSE piggy banks");
            }
            for (MemberTargetDTO targetDTO : dto.targets()) {
                User user = userRepository.findById(targetDTO.userId())
                        .orElseThrow(() -> new RuntimeException("User not found: " + targetDTO.userId()));

                PiggyBankTarget target = new PiggyBankTarget();
                target.setPiggyBank(savedPiggyBank);
                target.setUser(user);
                target.setTargetAmount(targetDTO.targetAmount());
                piggyBankRepository.saveTarget(target);
            }
        }

        return toViewDTO(savedPiggyBank);
    }

    @Override
    @Transactional
    public PiggyBankViewDTO deposit(Long piggyBankId, BigDecimal amount) {
        User currentUser = loadUserPort.loadUser();
        PiggyBank piggyBank = piggyBankRepository.findById(piggyBankId)
                .orElseThrow(() -> new RuntimeException("Piggy Bank not found"));

        if (piggyBank.getType() == PiggyBankType.PERSONAL
                && !piggyBank.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only deposit to your own personal piggy bank");
        }
        // TODO: Add check for HOUSE membership

        PiggyBankDeposit deposit = new PiggyBankDeposit();
        deposit.setAmount(amount);
        deposit.setDate(LocalDateTime.now());
        deposit.setPiggyBank(piggyBank);
        deposit.setUser(currentUser);
        piggyBankRepository.saveDeposit(deposit);

        return toViewDTO(piggyBank);
    }

    @Override
    public PiggyBankViewDTO getById(Long id) {
        PiggyBank piggyBank = piggyBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piggy Bank not found"));
        return toViewDTO(piggyBank);
    }

    @Override
    public List<PiggyBankViewDTO> getByHouseId(Long houseId) {
        return piggyBankRepository.findByHouseId(houseId).stream()
                .map(this::toViewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PiggyBankViewDTO> getMyPiggyBanks() {
        User currentUser = loadUserPort.loadUser();
        return piggyBankRepository.findByOwnerId(currentUser.getId()).stream()
                .map(this::toViewDTO)
                .collect(Collectors.toList());
    }

    private PiggyBankViewDTO toViewDTO(PiggyBank piggyBank) {
        BigDecimal currentBalance = piggyBankRepository.getTotalBalance(piggyBank.getId());
        User currentUser = loadUserPort.loadUser();
        BigDecimal myContribution = piggyBankRepository.getUserContribution(piggyBank.getId(), currentUser.getId());

        // Simple completion percentage logic (can be improved)
        Double completionPercentage = 0.0;
        // If there is a total target amount (sum of individual targets or a global
        // target if added later)

        Map<String, BigDecimal> breakdown = new HashMap<>();
        // Populate breakdown if needed

        return new PiggyBankViewDTO(
                piggyBank.getId(),
                piggyBank.getName(),
                piggyBank.getDescription(),
                piggyBank.getType(),
                piggyBank.getContributionType(),
                piggyBank.getTargetDate(),
                currentBalance,
                myContribution,
                completionPercentage,
                breakdown);
    }
}
