package com.example.moneymoney.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class UserJpaEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    private BigDecimal income;

    public UserJpaEntity() {
    }

    public UserJpaEntity(String name, String email, String passwordHash, BigDecimal income) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.income = income;
    }

    public com.example.moneymoney.domain.model.User toDomain() {
        return new com.example.moneymoney.domain.model.User(
                this.id,
                this.name,
                this.email,
                this.passwordHash,
                this.income);
    }

    public static UserJpaEntity fromDomain(com.example.moneymoney.domain.model.User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setIncome(user.getIncome());
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

    // UserDetails Implementation

    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
