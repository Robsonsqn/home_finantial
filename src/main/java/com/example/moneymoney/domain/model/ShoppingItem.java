package com.example.moneymoney.domain.model;

import java.util.HashSet;
import java.util.Set;

public class ShoppingItem {

    private Long id;
    private String name;
    private boolean isPurchased;
    private House house;
    private User createdBy;
    private Set<User> votes = new HashSet<>();

    public ShoppingItem() {
    }

    public ShoppingItem(Long id, String name, boolean isPurchased, House house, User createdBy, Set<User> votes) {
        this.id = id;
        this.name = name;
        this.isPurchased = isPurchased;
        this.house = house;
        this.createdBy = createdBy;
        this.votes = votes != null ? votes : new HashSet<>();
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

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Set<User> getVotes() {
        return votes;
    }

    public void setVotes(Set<User> votes) {
        this.votes = votes;
    }

    public void addVote(User user) {
        this.votes.add(user);
    }

    public void removeVote(User user) {
        this.votes.remove(user);
    }
}
