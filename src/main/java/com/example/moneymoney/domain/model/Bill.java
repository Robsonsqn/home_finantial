package com.example.moneymoney.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate dueDate;
    private BillType type;
    private boolean isPaid;
    private House house;
    private User user;

    public Bill(Long id, String description, BigDecimal amount, LocalDate dueDate, BillType type, boolean isPaid,
            House house, User user) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
        this.isPaid = isPaid;
        this.house = house;
        this.user = user;
    }

    public Bill() {
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

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
