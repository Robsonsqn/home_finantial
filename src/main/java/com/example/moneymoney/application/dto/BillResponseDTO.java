package com.example.moneymoney.application.dto;

import com.example.moneymoney.domain.model.BillType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BillResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate dueDate;
    private BillType type;
    private boolean isPaid;
    private Long houseId;
    private Long userId;

    public BillResponseDTO() {
    }

    public BillResponseDTO(Long id, String description, BigDecimal amount, LocalDate dueDate, BillType type,
            boolean isPaid, Long houseId, Long userId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
        this.isPaid = isPaid;
        this.houseId = houseId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
