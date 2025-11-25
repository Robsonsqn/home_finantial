package com.example.moneymoney.domain.model;

import com.example.moneymoney.domain.exception.InvalidHouseNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {

    @Test
    void shouldCreateHouseSuccessfully() {
        House house = new House("My House");
        assertNotNull(house);
        assertEquals("My House", house.getName());
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
        assertThrows(InvalidHouseNameException.class, () -> {
            new House("");
        });
    }

    @Test
    void shouldThrowExceptionForShortName() {
        assertThrows(InvalidHouseNameException.class, () -> {
            new House("Hi");
        });
    }

    @Test
    void shouldThrowExceptionForLongName() {
        String longName = "a".repeat(51);
        assertThrows(InvalidHouseNameException.class, () -> {
            new House(longName);
        });
    }
}
