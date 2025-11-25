package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.user.UpdateProfileDTO;
import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.exception.UserNotFoundException;
import com.example.moneymoney.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User updateUserProfile(Long userId, UpdateProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateProfile(dto.getName(), dto.getIncome());

        return userRepository.save(user);
    }
}
