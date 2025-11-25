package com.example.moneymoney.domain.model;

import java.math.BigDecimal;

public class User {

    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private BigDecimal income;

    public User() {
    }

    public User(Long id, String name, String email, String passwordHash, BigDecimal income) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.income = income;
    }

    public User(String name, String email, String passwordHash, BigDecimal income) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.income = income;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
