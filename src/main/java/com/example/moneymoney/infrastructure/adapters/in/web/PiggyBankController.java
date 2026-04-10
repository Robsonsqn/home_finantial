package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.application.dto.PiggyBankCreateDTO;
import com.example.moneymoney.application.dto.PiggyBankViewDTO;
import com.example.moneymoney.application.port.in.CreatePiggyBankUseCase;
import com.example.moneymoney.application.port.in.DepositUseCase;
import com.example.moneymoney.application.port.in.GetPiggyBankUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/piggy-banks")
public class PiggyBankController {

    private final CreatePiggyBankUseCase createPiggyBankUseCase;
    private final DepositUseCase depositUseCase;
    private final GetPiggyBankUseCase getPiggyBankUseCase;

    public PiggyBankController(CreatePiggyBankUseCase createPiggyBankUseCase,
            DepositUseCase depositUseCase,
            GetPiggyBankUseCase getPiggyBankUseCase) {
        this.createPiggyBankUseCase = createPiggyBankUseCase;
        this.depositUseCase = depositUseCase;
        this.getPiggyBankUseCase = getPiggyBankUseCase;
    }

    @PostMapping
    public ResponseEntity<PiggyBankViewDTO> create(@RequestBody PiggyBankCreateDTO dto) {
        return ResponseEntity.ok(createPiggyBankUseCase.create(dto));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<PiggyBankViewDTO> deposit(@PathVariable Long id,
            @RequestBody Map<String, BigDecimal> payload) {
        BigDecimal amount = payload.get("amount");
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        return ResponseEntity.ok(depositUseCase.deposit(id, amount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PiggyBankViewDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getPiggyBankUseCase.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PiggyBankViewDTO>> getMyPiggyBanks() {
        return ResponseEntity.ok(getPiggyBankUseCase.getMyPiggyBanks());
    }

    @GetMapping("/house/{houseId}")
    public ResponseEntity<List<PiggyBankViewDTO>> getByHouseId(@PathVariable Long houseId) {
        return ResponseEntity.ok(getPiggyBankUseCase.getByHouseId(houseId));
    }
}
