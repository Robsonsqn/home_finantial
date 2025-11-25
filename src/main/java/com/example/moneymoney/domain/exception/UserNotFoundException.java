package com.example.moneymoney.domain.exception;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}
