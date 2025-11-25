package com.example.moneymoney.application.dto;

import com.example.moneymoney.domain.model.BillType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BillCreateDTO {
    private String description;
    private BigDecimal amount;
    private LocalDate dueDate;
    private BillType type;

    public BillCreateDTO() {
    }

    public BillCreateDTO(String description, BigDecimal amount, LocalDate dueDate, BillType type) {
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }
}
