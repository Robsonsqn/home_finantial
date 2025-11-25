package com.example.moneymoney.domain.model;

public class House {

    private Long id;
    private String name;

    public House() {
    }

    public House(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public House(String name) {
        this.name = name;
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
