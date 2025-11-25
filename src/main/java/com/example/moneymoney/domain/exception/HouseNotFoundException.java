package com.example.moneymoney.domain.exception;

public class HouseNotFoundException extends DomainException {
    public HouseNotFoundException(Long houseId) {
        super("House not found with ID: " + houseId);
    }
}
