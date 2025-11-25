package com.example.moneymoney.application.port.out;

import com.example.moneymoney.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User save(User user);
}
