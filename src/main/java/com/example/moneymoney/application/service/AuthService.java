package com.example.moneymoney.application.service;

import com.example.moneymoney.application.dto.auth.LoginRequestDTO;
import com.example.moneymoney.application.dto.auth.LoginResponseDTO;
import com.example.moneymoney.application.dto.auth.RegisterRequestDTO;
import com.example.moneymoney.application.port.out.TokenGeneratorPort;
import com.example.moneymoney.application.port.out.UserRepositoryPort;
import com.example.moneymoney.domain.exception.EmailAlreadyExistsException;
import com.example.moneymoney.domain.exception.UserNotFoundException;
import com.example.moneymoney.domain.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenGeneratorPort tokenGenerator;

    public AuthService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, TokenGeneratorPort tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
    }

    public void register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        User newUser = new User(
                dto.getName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getIncome());

        userRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(dto.getEmail()));

        String jwtToken = tokenGenerator.generateToken(user);
        return new LoginResponseDTO(jwtToken);
    }
}
