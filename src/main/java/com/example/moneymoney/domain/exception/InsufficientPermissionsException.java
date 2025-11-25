package com.example.moneymoney.domain.exception;

public class InsufficientPermissionsException extends DomainException {
    public InsufficientPermissionsException(String message) {
        super(message);
    }
}
