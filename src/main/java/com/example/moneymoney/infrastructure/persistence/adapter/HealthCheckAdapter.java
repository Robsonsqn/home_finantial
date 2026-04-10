package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.HealthCheckPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckAdapter implements HealthCheckPort {

    private final JdbcTemplate jdbcTemplate;

    public HealthCheckAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isDatabaseUp() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
