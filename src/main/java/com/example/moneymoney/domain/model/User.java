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
        validateName(name);
        validateEmail(email);
        validateIncome(income);
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.income = income;
    }

    public User(String name, String email, String passwordHash, BigDecimal income) {
        validateName(name);
        validateEmail(email);
        validateIncome(income);
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.income = income;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name too long");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void validateIncome(BigDecimal income) {
        if (income != null && income.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }
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

    public void updateProfile(String newName, BigDecimal newIncome) {
        if (newName != null) {
            validateName(newName);
            this.name = newName;
        }
        if (newIncome != null) {
            validateIncome(newIncome);
            this.income = newIncome;
        }
    }

    public boolean hasIncome() {
        return income != null && income.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getDisplayName() {
        return name != null ? name : email;
    }
}
