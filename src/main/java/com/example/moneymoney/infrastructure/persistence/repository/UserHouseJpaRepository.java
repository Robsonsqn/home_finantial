package com.example.moneymoney.infrastructure.persistence.repository;

import com.example.moneymoney.infrastructure.persistence.entity.HouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserHouseJpaEntity;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserHouseJpaRepository extends JpaRepository<UserHouseJpaEntity, Long> {
    List<UserHouseJpaEntity> findByUser(UserJpaEntity user);

    List<UserHouseJpaEntity> findByHouse(HouseJpaEntity house);

    Optional<UserHouseJpaEntity> findByHouseAndUser(HouseJpaEntity house, UserJpaEntity user);

    boolean existsByHouseAndUser(HouseJpaEntity house, UserJpaEntity user);

    boolean existsByUserIdAndHouseId(Long userId, Long houseId);
}
