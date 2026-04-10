package com.example.moneymoney.domain.model;

import java.time.LocalDate;
import java.util.List;

public class PiggyBank {
    private Long id;
    private String name;
    private String description;
    private PiggyBankType type;
    private ContributionType contributionType;
    private LocalDate targetDate;
    private House house;
    private User owner;
    private List<PiggyBankDeposit> deposits;
    private List<PiggyBankTarget> targets;

    public PiggyBank() {
    }

    public PiggyBank(Long id, String name, String description, PiggyBankType type, ContributionType contributionType,
            LocalDate targetDate, House house, User owner, List<PiggyBankDeposit> deposits,
            List<PiggyBankTarget> targets) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.contributionType = contributionType;
        this.targetDate = targetDate;
        this.house = house;
        this.owner = owner;
        this.deposits = deposits;
        this.targets = targets;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PiggyBankType getType() {
        return type;
    }

    public void setType(PiggyBankType type) {
        this.type = type;
    }

    public ContributionType getContributionType() {
        return contributionType;
    }

    public void setContributionType(ContributionType contributionType) {
        this.contributionType = contributionType;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<PiggyBankDeposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<PiggyBankDeposit> deposits) {
        this.deposits = deposits;
    }

    public List<PiggyBankTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<PiggyBankTarget> targets) {
        this.targets = targets;
    }
}
