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
    private final com.example.moneymoney.infrastructure.security.AuthenticationHelper authHelper;

    public UserController(com.example.moneymoney.application.service.UserService userService,
            com.example.moneymoney.infrastructure.security.AuthenticationHelper authHelper) {
        this.userService = userService;
        this.authHelper = authHelper;
    }

    @GetMapping("/me")
    public com.example.moneymoney.application.dto.user.UserResponseDTO getCurrentUser(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        User user = authHelper.getCurrentUser(userDetails);
        return com.example.moneymoney.application.dto.user.UserResponseDTO.fromDomain(user);
    }

    @GetMapping("/me/profile")
    public com.example.moneymoney.application.dto.user.UserResponseDTO getProfile(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return getCurrentUser(userDetails);
    }

    @org.springframework.web.bind.annotation.PutMapping("/me/profile")
    public com.example.moneymoney.application.dto.user.UserResponseDTO updateProfile(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody com.example.moneymoney.application.dto.user.UpdateProfileDTO dto) {
        User currentUser = authHelper.getCurrentUser(userDetails);
        User updatedUser = userService.updateUserProfile(currentUser.getId(), dto);
        return com.example.moneymoney.application.dto.user.UserResponseDTO.fromDomain(updatedUser);
    }
}
