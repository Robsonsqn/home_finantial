package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.ShoppingItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingItemJpaRepository extends JpaRepository<ShoppingItemJpaEntity, Long> {

    @Query("SELECT i FROM ShoppingItemJpaEntity i " +
            "LEFT JOIN i.votes v " +
            "WHERE i.house.id = :houseId AND i.isPurchased = false " +
            "GROUP BY i " +
            "ORDER BY COUNT(v) DESC, i.name ASC")
    List<ShoppingItemJpaEntity> findAllActiveByHouseIdSortedByVotes(@Param("houseId") Long houseId);

    @Query("SELECT i FROM ShoppingItemJpaEntity i " +
            "LEFT JOIN i.votes v " +
            "WHERE i.house.id = :houseId " +
            "GROUP BY i " +
            "ORDER BY i.isPurchased ASC, COUNT(v) DESC, i.name ASC")
    List<ShoppingItemJpaEntity> findAllByHouseIdSortedByVotes(@Param("houseId") Long houseId);

    Optional<ShoppingItemJpaEntity> findByNameAndHouseIdAndIsPurchasedFalse(String name, Long houseId);
}
