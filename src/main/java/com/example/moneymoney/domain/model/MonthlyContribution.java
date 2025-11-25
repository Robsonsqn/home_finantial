package com.example.moneymoney.domain.model;

import java.math.BigDecimal;

public class MonthlyContribution {

    private Long id;
    private int month;
    private int year;
    private BigDecimal percentage;
    private User user;
    private House house;

    public MonthlyContribution() {
    }

    public MonthlyContribution(int month, int year, BigDecimal percentage, User user, House house) {
        this.month = month;
        this.year = year;
        this.percentage = percentage;
        this.user = user;
        this.house = house;
    }

    public MonthlyContribution(Long id, int month, int year, BigDecimal percentage, User user, House house) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.percentage = percentage;
        this.user = user;
        this.house = house;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
