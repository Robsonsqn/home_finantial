package com.example.moneymoney.infrastructure.adapters.in.web;

import com.example.moneymoney.application.service.AuthService;
import com.example.moneymoney.application.dto.auth.LoginRequestDTO;
import com.example.moneymoney.application.dto.auth.LoginResponseDTO;
import com.example.moneymoney.application.dto.auth.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO dto) {
        logger.info("Attempting to register user with email: {}", dto.getEmail());
        try {
            authService.register(dto);
            logger.info("User registered successfully: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            logger.error("Registration failed for email: {}", dto.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        logger.info("Attempting login for user: {}", dto.getEmail());
        try {
            LoginResponseDTO response = authService.login(dto);
            logger.info("Login successful for user: {}", dto.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Login failed for user: {}", dto.getEmail());
            throw e;
        }
    }
}
