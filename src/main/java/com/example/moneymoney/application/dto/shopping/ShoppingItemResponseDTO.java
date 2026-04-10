package com.example.moneymoney.application.dto.shopping;

public record ShoppingItemResponseDTO(
                Long id,
                String name,
                int voteCount,
                boolean iVoted,
                boolean isPurchased) {
}
