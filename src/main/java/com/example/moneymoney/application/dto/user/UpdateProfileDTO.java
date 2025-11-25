package com.example.moneymoney.application.dto.user;

import java.math.BigDecimal;

public class UpdateProfileDTO {
    private String name;
    private BigDecimal income;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
