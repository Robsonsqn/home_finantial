package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.auth.LoginRequestDTO;
import com.example.moneymoney.application.dto.auth.LoginResponseDTO;
import com.example.moneymoney.application.dto.auth.RegisterRequestDTO;
import com.example.moneymoney.infrastructure.security.JwtService;
import com.example.moneymoney.infrastructure.persistence.entity.UserJpaEntity;
import com.example.moneymoney.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserJpaRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        UserJpaEntity newUser = new UserJpaEntity();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setIncome(dto.getIncome());
        newUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        return new LoginResponseDTO(jwtToken);
    }
}
