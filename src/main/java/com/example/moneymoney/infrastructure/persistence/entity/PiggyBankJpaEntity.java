package com.example.moneymoney.infrastructure.persistence.entity;

import com.example.moneymoney.domain.model.ContributionType;
import com.example.moneymoney.domain.model.PiggyBank;
import com.example.moneymoney.domain.model.PiggyBankType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "piggy_banks")
public class PiggyBankJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PiggyBankType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType contributionType;

    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private HouseJpaEntity house;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserJpaEntity owner;

    @OneToMany(mappedBy = "piggyBank", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PiggyBankDepositJpaEntity> deposits = new ArrayList<>();

    @OneToMany(mappedBy = "piggyBank", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PiggyBankTargetJpaEntity> targets = new ArrayList<>();

    public PiggyBankJpaEntity() {
    }

    public PiggyBank toDomain() {
        return new PiggyBank(
                this.id,
                this.name,
                this.description,
                this.type,
                this.contributionType,
                this.targetDate,
                this.house != null ? this.house.toDomain() : null,
                this.owner != null ? this.owner.toDomain() : null,
                new ArrayList<>(), // Deposits are loaded separately or lazily
                new ArrayList<>() // Targets are loaded separately or lazily
        );
    }

    public static PiggyBankJpaEntity fromDomain(PiggyBank domain) {
        PiggyBankJpaEntity entity = new PiggyBankJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setType(domain.getType());
        entity.setContributionType(domain.getContributionType());
        entity.setTargetDate(domain.getTargetDate());
        if (domain.getHouse() != null) {
            entity.setHouse(HouseJpaEntity.fromDomain(domain.getHouse()));
        }
        if (domain.getOwner() != null) {
            entity.setOwner(UserJpaEntity.fromDomain(domain.getOwner()));
        }
        return entity;
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

    public HouseJpaEntity getHouse() {
        return house;
    }

    public void setHouse(HouseJpaEntity house) {
        this.house = house;
    }

    public UserJpaEntity getOwner() {
        return owner;
    }

    public void setOwner(UserJpaEntity owner) {
        this.owner = owner;
    }

    public List<PiggyBankDepositJpaEntity> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<PiggyBankDepositJpaEntity> deposits) {
        this.deposits = deposits;
    }

    public List<PiggyBankTargetJpaEntity> getTargets() {
        return targets;
    }

    public void setTargets(List<PiggyBankTargetJpaEntity> targets) {
        this.targets = targets;
    }
}
