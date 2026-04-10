package com.example.moneymoney.infrastructure.persistence.adapter;

import com.example.moneymoney.application.port.out.ShoppingItemRepositoryPort;
import com.example.moneymoney.domain.model.ShoppingItem;
import com.example.moneymoney.infrastructure.persistence.entity.ShoppingItemJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.ShoppingItemJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ShoppingItemPersistenceAdapter implements ShoppingItemRepositoryPort {

    private final ShoppingItemJpaRepository repository;

    public ShoppingItemPersistenceAdapter(ShoppingItemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShoppingItem save(ShoppingItem shoppingItem) {
        ShoppingItemJpaEntity entity = ShoppingItemJpaEntity.fromDomain(shoppingItem);
        ShoppingItemJpaEntity savedEntity = repository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ShoppingItem> findById(Long id) {
        return repository.findById(id).map(ShoppingItemJpaEntity::toDomain);
    }

    @Override
    public List<ShoppingItem> findAllActiveByHouseIdSortedByVotes(Long houseId) {
        return repository.findAllActiveByHouseIdSortedByVotes(houseId).stream()
                .map(ShoppingItemJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingItem> findAllByHouseIdSortedByVotes(Long houseId) {
        return repository.findAllByHouseIdSortedByVotes(houseId).stream()
                .map(ShoppingItemJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShoppingItem> findByNameAndHouseIdAndIsPurchasedFalse(String name, Long houseId) {
        return repository.findByNameAndHouseIdAndIsPurchasedFalse(name, houseId)
                .map(ShoppingItemJpaEntity::toDomain);
    }
}
