package com.example.moneymoney.domain.model;

public class House {

    private Long id;
    private String name;

    public House() {
    }

    public House(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public House(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new com.example.moneymoney.domain.exception.InvalidHouseNameException("House name cannot be empty");
        }
        if (name.length() < 3) {
            throw new com.example.moneymoney.domain.exception.InvalidHouseNameException(
                    "House name must have at least 3 characters");
        }
        if (name.length() > 50) {
            throw new com.example.moneymoney.domain.exception.InvalidHouseNameException("House name is too long");
        }
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

    public void rename(String newName) {
        validateName(newName);
        this.name = newName;
    }
}
