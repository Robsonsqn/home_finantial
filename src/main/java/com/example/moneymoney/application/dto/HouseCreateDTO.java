package com.example.moneymoney.application.dto;

import jakarta.validation.constraints.NotBlank;

public record HouseCreateDTO(@NotBlank String name) {
}
