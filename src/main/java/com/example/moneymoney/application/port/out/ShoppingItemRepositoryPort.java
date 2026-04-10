package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.ShoppingItem;
import java.util.List;
import java.util.Optional;

public interface ShoppingItemRepositoryPort {
    ShoppingItem save(ShoppingItem shoppingItem);

    Optional<ShoppingItem> findById(Long id);

    List<ShoppingItem> findAllActiveByHouseIdSortedByVotes(Long houseId);

    List<ShoppingItem> findAllByHouseIdSortedByVotes(Long houseId);

    Optional<ShoppingItem> findByNameAndHouseIdAndIsPurchasedFalse(String name, Long houseId);
}
