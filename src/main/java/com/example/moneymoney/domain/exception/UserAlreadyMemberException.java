package com.example.moneymoney.domain.exception;

public class UserAlreadyMemberException extends DomainException {

    public UserAlreadyMemberException(String message) {
        super(message);
    }

    public UserAlreadyMemberException(String email, String houseName) {
        super("User with email '" + email + "' is already a member of house '" + houseName + "'");
    }
}
