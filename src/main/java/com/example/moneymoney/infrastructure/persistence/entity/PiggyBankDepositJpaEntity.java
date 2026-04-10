package com.example.moneymoney.infrastructure.persistence.entity;

import com.example.moneymoney.domain.model.PiggyBankDeposit;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "piggy_bank_deposits")
public class PiggyBankDepositJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "piggy_bank_id", nullable = false)
    private PiggyBankJpaEntity piggyBank;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    public PiggyBankDepositJpaEntity() {
    }

    public PiggyBankDeposit toDomain() {
        return new PiggyBankDeposit(
                this.id,
                this.amount,
                this.date,
                this.piggyBank != null ? this.piggyBank.toDomain() : null,
                this.user != null ? this.user.toDomain() : null);
    }

    public static PiggyBankDepositJpaEntity fromDomain(PiggyBankDeposit domain) {
        PiggyBankDepositJpaEntity entity = new PiggyBankDepositJpaEntity();
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount());
        entity.setDate(domain.getDate());
        if (domain.getPiggyBank() != null) {
            entity.setPiggyBank(PiggyBankJpaEntity.fromDomain(domain.getPiggyBank()));
        }
        if (domain.getUser() != null) {
            entity.setUser(UserJpaEntity.fromDomain(domain.getUser()));
        }
        return entity;
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
}
