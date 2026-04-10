package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.application.dto.HealthResponseDTO;
import com.example.moneymoney.application.port.in.CheckHealthUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final CheckHealthUseCase checkHealthUseCase;

    public HealthController(CheckHealthUseCase checkHealthUseCase) {
        this.checkHealthUseCase = checkHealthUseCase;
    }

    @GetMapping
    public ResponseEntity<HealthResponseDTO> checkHealth() {
        HealthResponseDTO response = checkHealthUseCase.checkHealth();
        if ("DOWN".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
