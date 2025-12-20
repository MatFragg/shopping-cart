package com.matfragg.shopping_car.api.authentication.service.impl;

import com.matfragg.shopping_car.api.authentication.dto.request.LoginRequest;
import com.matfragg.shopping_car.api.authentication.dto.request.RegisterRequest;
import com.matfragg.shopping_car.api.authentication.dto.response.AuthResponse;
import com.matfragg.shopping_car.api.authentication.exceptions.UnauthorizedException;
import com.matfragg.shopping_car.api.authentication.mapper.UserMapper;
import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.authentication.repository.UserRepository;
import com.matfragg.shopping_car.api.authentication.service.AuthService;
import com.matfragg.shopping_car.api.shared.exceptions.BadRequestException;
import com.matfragg.shopping_car.api.shared.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager  authenticationManager,
                           PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.username()))
            throw new BadRequestException("Username already exists");

        User newUser = userMapper.toEntity(request);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(authentication);

        // Convert user to DTO
        AuthResponse userResponse = userMapper.toResponse(user);

        // Return response with token and user data
        return new AuthResponse(userResponse.id(), userResponse.username(), token);
    }

    @Override
    public AuthResponse getCurrentUser() {
        return null;
    }
}
