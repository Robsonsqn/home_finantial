package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.House;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.domain.model.UserHouse;
import java.util.List;
import java.util.Optional;

public interface UserHouseRepositoryPort {
    UserHouse save(UserHouse userHouse);

    List<UserHouse> findByUser(User user);

    List<UserHouse> findByHouse(House house);

    Optional<UserHouse> findByHouseAndUser(House house, User user);

    boolean existsByHouseAndUser(House house, User user);

    void delete(UserHouse userHouse);
}
