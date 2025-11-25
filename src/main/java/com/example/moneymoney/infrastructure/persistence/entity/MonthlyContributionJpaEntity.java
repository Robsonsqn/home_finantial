package com.example.moneymoney.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "monthly_contributions")
public class MonthlyContributionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;
    private int year;
    private BigDecimal percentage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private HouseJpaEntity house;

    public MonthlyContributionJpaEntity() {
    }

    public MonthlyContributionJpaEntity(int month, int year, BigDecimal percentage, UserJpaEntity user,
            HouseJpaEntity house) {
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

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public HouseJpaEntity getHouse() {
        return house;
    }

    public void setHouse(HouseJpaEntity house) {
        this.house = house;
    }
}
