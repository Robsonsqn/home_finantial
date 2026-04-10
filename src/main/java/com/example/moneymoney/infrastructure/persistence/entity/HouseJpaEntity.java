package com.example.moneymoney.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "houses")
public class HouseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public HouseJpaEntity() {
    }

    public HouseJpaEntity(String name) {
        this.name = name;
    }

    public HouseJpaEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public com.example.moneymoney.domain.model.House toDomain() {
        return new com.example.moneymoney.domain.model.House(
                this.id,
                this.name);
    }

    public static HouseJpaEntity fromDomain(com.example.moneymoney.domain.model.House house) {
        HouseJpaEntity entity = new HouseJpaEntity();
        entity.setId(house.getId());
        entity.setName(house.getName());
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
}
