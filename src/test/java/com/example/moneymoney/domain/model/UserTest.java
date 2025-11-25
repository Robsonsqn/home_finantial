package com.example.moneymoney.domain.model;

import com.example.moneymoney.domain.exception.InvalidEmailException;
import com.example.moneymoney.domain.exception.InvalidIncomeException;
import com.example.moneymoney.domain.exception.InvalidUserNameException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User("John Doe", "john@example.com", "hash", new BigDecimal("1000"));
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
        assertThrows(InvalidUserNameException.class, () -> {
            new User("", "john@example.com", "hash", new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> {
            new User("John Doe", "invalid-email", "hash", new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionForNegativeIncome() {
        assertThrows(InvalidIncomeException.class, () -> {
            new User("John Doe", "john@example.com", "hash", new BigDecimal("-100"));
        });
    }
}
