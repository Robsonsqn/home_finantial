package com.example.moneymoney.domain.model;

import com.example.moneymoney.domain.exception.InvalidUserHouseException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserHouseTest {

    @Test
    void shouldCreateUserHouseSuccessfully() {
        User user = new User("John", "john@example.com", "hash", new BigDecimal("1000"));
        House house = new House("My House");
        UserHouse userHouse = new UserHouse(user, house, HouseRole.ADMIN);

        assertNotNull(userHouse);
        assertEquals(user, userHouse.getUser());
        assertEquals(house, userHouse.getHouse());
        assertEquals(HouseRole.ADMIN, userHouse.getRole());
    }

    @Test
    void shouldThrowExceptionForNullUser() {
        House house = new House("My House");
        assertThrows(InvalidUserHouseException.class, () -> {
            new UserHouse(null, house, HouseRole.ADMIN);
        });
    }

    @Test
    void shouldThrowExceptionForNullHouse() {
        User user = new User("John", "john@example.com", "hash", new BigDecimal("1000"));
        assertThrows(InvalidUserHouseException.class, () -> {
            new UserHouse(user, null, HouseRole.ADMIN);
        });
    }

    @Test
    void shouldThrowExceptionForNullRole() {
        User user = new User("John", "john@example.com", "hash", new BigDecimal("1000"));
        House house = new House("My House");
        assertThrows(InvalidUserHouseException.class, () -> {
            new UserHouse(user, house, null);
        });
    }
}
