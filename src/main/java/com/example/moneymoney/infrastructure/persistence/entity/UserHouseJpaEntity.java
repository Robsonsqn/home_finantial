package com.example.moneymoney.infrastructure.persistence.entity;

import com.example.moneymoney.domain.model.HouseRole;
import jakarta.persistence.*;

@Entity
@Table(name = "user_houses")
public class UserHouseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private HouseJpaEntity house;

    @Enumerated(EnumType.STRING)
    private HouseRole role;

    public UserHouseJpaEntity() {
    }

    public UserHouseJpaEntity(UserJpaEntity user, HouseJpaEntity house, HouseRole role) {
        this.user = user;
        this.house = house;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public HouseRole getRole() {
        return role;
    }

    public void setRole(HouseRole role) {
        this.role = role;
    }
}
