package com.example.moneymoney.domain.exception;

public class BillNotFoundException extends DomainException {

    public BillNotFoundException(Long billId) {
        super("Bill not found with id: " + billId);
    }

    public BillNotFoundException(String message) {
        super(message);
    }
}
