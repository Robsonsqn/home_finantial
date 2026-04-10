package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.HealthResponseDTO;
import com.example.moneymoney.application.port.in.CheckHealthUseCase;
import com.example.moneymoney.application.port.out.HealthCheckPort;
import org.springframework.stereotype.Service;

@Service
public class HealthService implements CheckHealthUseCase {

    private final HealthCheckPort healthCheckPort;

    public HealthService(HealthCheckPort healthCheckPort) {
        this.healthCheckPort = healthCheckPort;
    }

    @Override
    public HealthResponseDTO checkHealth() {
        boolean isDbUp = healthCheckPort.isDatabaseUp();
        
        if (isDbUp) {
            return new HealthResponseDTO("UP", "Application and Database are fully functional");
        } else {
            return new HealthResponseDTO("DOWN", "Database is unreachable");
        }
    }
}
