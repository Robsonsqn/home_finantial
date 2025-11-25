package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.domain.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final com.example.moneymoney.application.service.UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public UserController(com.example.moneymoney.application.service.UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getCurrentUser(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        // Assuming the username is the email or some identifier we can use to fetch the
        // user
        // Or if UserDetails IS the entity, we can cast it, but better to fetch fresh or
        // map it.
        // Since UserJpaEntity implements UserDetails and has getId(), let's try to cast
        // or use username.
        // But to be safe and clean, let's fetch by username (email).
        // Actually, UserService has getUserProfile(Long id).
        // UserDetails doesn't guarantee getId(). UserJpaEntity does.
        if (userDetails instanceof com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity) {
            Long userId = ((com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity) userDetails)
                    .getId();
            return userService.getUserProfile(userId);
        }
        // Fallback or error
        throw new RuntimeException("Unknown user principal type");
    }

    @GetMapping("/me/profile")
    public User getProfile(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return getCurrentUser(userDetails);
    }

    @org.springframework.web.bind.annotation.PutMapping("/me/profile")
    public User updateProfile(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody com.example.moneymoney.application.dto.user.UpdateProfileDTO dto) {
        if (userDetails instanceof com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity) {
            Long userId = ((com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity) userDetails)
                    .getId();
            return userService.updateUserProfile(userId, dto);
        }
        throw new RuntimeException("Unknown user principal type");
    }
}
