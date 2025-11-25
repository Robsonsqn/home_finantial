package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.user.UpdateProfileDTO;
import com.example.moneymoney.domain.model.User;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserJpaRepository userRepository;

    @Autowired
    public UserService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserProfile(Long userId) {
        UserJpaEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDomain(entity);
    }

    public User updateUserProfile(Long userId, UpdateProfileDTO dto) {
        UserJpaEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getIncome() != null) {
            entity.setIncome(dto.getIncome());
        }

        UserJpaEntity savedEntity = userRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    private User mapToDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getIncome());
    }
}
