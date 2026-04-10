package com.example.moneymoney.domain.model;

import java.math.BigDecimal;

public class PiggyBankTarget {
    private Long id;
    private PiggyBank piggyBank;
    private User user;
    private BigDecimal targetAmount;

    public PiggyBankTarget() {
    }

    public PiggyBankTarget(Long id, PiggyBank piggyBank, User user, BigDecimal targetAmount) {
        this.id = id;
        this.piggyBank = piggyBank;
        this.user = user;
        this.targetAmount = targetAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }
}
