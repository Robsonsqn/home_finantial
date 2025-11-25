package com.example.moneymoney.application.dto;

import java.math.BigDecimal;

public class ProfileUpdateDTO {
    private BigDecimal income;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
