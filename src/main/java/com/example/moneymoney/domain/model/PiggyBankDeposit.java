package com.example.moneymoney.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PiggyBankDeposit {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime date;
    private PiggyBank piggyBank;
    private User user;

    public PiggyBankDeposit() {
    }

    public PiggyBankDeposit(Long id, BigDecimal amount, LocalDateTime date, PiggyBank piggyBank, User user) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.piggyBank = piggyBank;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public PiggyBank getPiggyBank() {
        return piggyBank;
    }

    public void setPiggyBank(PiggyBank piggyBank) {
        this.piggyBank = piggyBank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
