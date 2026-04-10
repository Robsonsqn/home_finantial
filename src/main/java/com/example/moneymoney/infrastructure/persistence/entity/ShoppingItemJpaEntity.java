package com.example.moneymoney.infrastructure.persistence.entity;

import com.example.moneymoney.domain.model.ShoppingItem;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "shopping_items")
public class ShoppingItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isPurchased;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private HouseJpaEntity house;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private UserJpaEntity createdBy;

    @ManyToMany
    @JoinTable(name = "shopping_item_votes", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserJpaEntity> votes = new HashSet<>();

    public ShoppingItemJpaEntity() {
    }

    public ShoppingItemJpaEntity(Long id, String name, boolean isPurchased, HouseJpaEntity house,
            UserJpaEntity createdBy, Set<UserJpaEntity> votes) {
        this.id = id;
        this.name = name;
        this.isPurchased = isPurchased;
        this.house = house;
        this.createdBy = createdBy;
        this.votes = votes != null ? votes : new HashSet<>();
    }

    public ShoppingItem toDomain() {
        return new ShoppingItem(
                this.id,
                this.name,
                this.isPurchased,
                this.house.toDomain(),
                this.createdBy.toDomain(),
                this.votes.stream().map(UserJpaEntity::toDomain).collect(Collectors.toSet()));
    }

    public static ShoppingItemJpaEntity fromDomain(ShoppingItem domain) {
        ShoppingItemJpaEntity entity = new ShoppingItemJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setPurchased(domain.isPurchased());

        if (domain.getHouse() != null) {
            entity.setHouse(HouseJpaEntity.fromDomain(domain.getHouse()));
        }

        if (domain.getCreatedBy() != null) {
            entity.setCreatedBy(UserJpaEntity.fromDomain(domain.getCreatedBy()));
        }

        if (domain.getVotes() != null) {
            entity.setVotes(domain.getVotes().stream()
                    .map(UserJpaEntity::fromDomain)
                    .collect(Collectors.toSet()));
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

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public HouseJpaEntity getHouse() {
        return house;
    }

    public void setHouse(HouseJpaEntity house) {
        this.house = house;
    }

    public UserJpaEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserJpaEntity createdBy) {
        this.createdBy = createdBy;
    }

    public Set<UserJpaEntity> getVotes() {
        return votes;
    }

    public void setVotes(Set<UserJpaEntity> votes) {
        this.votes = votes;
    }
}
