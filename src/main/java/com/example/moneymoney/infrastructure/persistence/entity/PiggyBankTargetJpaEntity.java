package com.example.moneymoney.infrastructure.persistence.entity;

import com.example.moneymoney.domain.model.PiggyBankTarget;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "piggy_bank_targets")
public class PiggyBankTargetJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piggy_bank_id", nullable = false)
    private PiggyBankJpaEntity piggyBank;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    public PiggyBankTargetJpaEntity() {
    }

    public PiggyBankTarget toDomain() {
        return new PiggyBankTarget(
                this.id,
                this.piggyBank != null ? this.piggyBank.toDomain() : null,
                this.user != null ? this.user.toDomain() : null,
                this.targetAmount);
    }

    public static PiggyBankTargetJpaEntity fromDomain(PiggyBankTarget domain) {
        PiggyBankTargetJpaEntity entity = new PiggyBankTargetJpaEntity();
        entity.setId(domain.getId());
        if (domain.getPiggyBank() != null) {
            entity.setPiggyBank(PiggyBankJpaEntity.fromDomain(domain.getPiggyBank()));
        }
        if (domain.getUser() != null) {
            entity.setUser(UserJpaEntity.fromDomain(domain.getUser()));
        }
        entity.setTargetAmount(domain.getTargetAmount());
        return entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PiggyBankJpaEntity getPiggyBank() {
        return piggyBank;
    }

    public void setPiggyBank(PiggyBankJpaEntity piggyBank) {
        this.piggyBank = piggyBank;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }
}
