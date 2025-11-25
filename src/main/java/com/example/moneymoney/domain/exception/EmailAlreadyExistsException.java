package com.example.moneymoney.domain.exception;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
