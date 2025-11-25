package com.example.moneymoney.domain.model;

public class UserHouse {

    private Long id;
    private User user;
    private House house;
    private HouseRole role;

    public UserHouse() {
    }

    public UserHouse(User user, House house, HouseRole role) {
        this.user = user;
        this.house = house;
        this.role = role;
    }

    public UserHouse(Long id, User user, House house, HouseRole role) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public HouseRole getRole() {
        return role;
    }

    public void setRole(HouseRole role) {
        this.role = role;
    }
}
