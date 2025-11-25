package com.example.moneymoney.domain.model;

import com.example.moneymoney.domain.exception.InsufficientPermissionsException;

public class UserHouse {

    private Long id;
    private User user;
    private House house;
    private HouseRole role;

    public UserHouse() {
    }

    public UserHouse(User user, House house, HouseRole role) {
        if (user == null) {
            throw new com.example.moneymoney.domain.exception.InvalidUserHouseException("User cannot be null");
        }
        if (house == null) {
            throw new com.example.moneymoney.domain.exception.InvalidUserHouseException("House cannot be null");
        }
        if (role == null) {
            throw new com.example.moneymoney.domain.exception.InvalidUserHouseException("Role cannot be null");
        }
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

    public boolean isAdmin() {
        return this.role == HouseRole.ADMIN;
    }

    public boolean isMember() {
        return this.role == HouseRole.MEMBER;
    }

    public void validateCanInviteMembers() {
        if (!isAdmin()) {
            throw new InsufficientPermissionsException("Only admins can invite members");
        }
    }

    public void validateCanRemoveMembers() {
        if (!isAdmin()) {
            throw new InsufficientPermissionsException("Only admins can remove members");
        }
    }
}
