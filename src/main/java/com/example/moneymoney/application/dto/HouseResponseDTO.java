package com.example.moneymoney.application.dto;

import com.example.moneymoney.domain.model.HouseRole;

public record HouseResponseDTO(Long id, String name, HouseRole userRole) {
}
