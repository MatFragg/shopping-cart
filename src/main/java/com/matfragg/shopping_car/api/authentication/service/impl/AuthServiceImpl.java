package com.matfragg.shopping_car.api.authentication.service.impl;

import com.matfragg.shopping_car.api.authentication.dto.request.LoginRequest;
import com.matfragg.shopping_car.api.authentication.dto.request.RegisterRequest;
import com.matfragg.shopping_car.api.authentication.dto.response.AuthResponse;
import com.matfragg.shopping_car.api.authentication.dto.response.CustomerInfo;
import com.matfragg.shopping_car.api.authentication.exceptions.UnauthorizedException;
import com.matfragg.shopping_car.api.authentication.mapper.UserMapper;
import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.authentication.model.enums.Roles;
import com.matfragg.shopping_car.api.authentication.repository.UserRepository;
import com.matfragg.shopping_car.api.authentication.service.AuthService;
import com.matfragg.shopping_car.api.customers.dto.request.CreateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.shared.exceptions.BadRequestException;
import com.matfragg.shopping_car.api.shared.exceptions.ResourceNotFoundException;
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
    private final CustomerService customerService;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager  authenticationManager,
                           PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserMapper userMapper, CustomerService customerService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
        this.customerService = customerService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.username()))
            throw new BadRequestException("Username already exists");

        if(userRepository.existsByEmail(request.email()))
            throw new BadRequestException("Email already exists");

        User newUser = userMapper.toEntity(request);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.addRole(Roles.valueOf("ROLE_USER"));
        User savedUser = userRepository.save(newUser);

        CustomerResponse customerResponse = null;

        if (savedUser.getRoles().contains(Roles.ROLE_USER)) {
            try {
                CreateCustomerRequest customerRequest = new CreateCustomerRequest(
                        request.firstName(),
                        request.lastName(),
                        request.phone(),
                        request.shippingAddress(),
                        savedUser.getId()
                );

                customerResponse = customerService.createCustomer(customerRequest);
            } catch (Exception e) {
                throw new BadRequestException("Error creating customer profile: " + e.getMessage());
            }
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getUsername(),
                request.password()
        );
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        CustomerInfo customerInfo = null;
        if (customerResponse != null) {
            customerInfo = new CustomerInfo(
                    customerResponse.id(),
                    customerResponse.firstName(),
                    customerResponse.lastName(),
                    customerResponse.phone(),
                    customerResponse.shippingAddress()
            );
        }
        AuthResponse userResponse = userMapper.toResponse(savedUser);

        return new AuthResponse(
                userResponse.id(),
                userResponse.username(),
                userResponse.email(),
                token,
                customerInfo
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        CustomerInfo customerInfo = null;
        try {
            CustomerResponse customer = customerService.findByUserId(user.getId());
            customerInfo = new CustomerInfo(
                    customer.id(),
                    customer.firstName(),
                    customer.lastName(),
                    customer.phone(),
                    customer.shippingAddress()
            );
        } catch (ResourceNotFoundException e) {
            throw new UnauthorizedException("Customer profile not found for user");
        }

        AuthResponse userResponse = userMapper.toResponse(user);

        return new AuthResponse(
                userResponse.id(),
                userResponse.username(),
                userResponse.email(),
                token,
                customerInfo
        );
    }

    @Override
    public AuthResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user found");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        CustomerInfo customerInfo = null;
        try {
            CustomerResponse customer = customerService.findByUserId(user.getId());
            customerInfo = new CustomerInfo(
                    customer.id(),
                    customer.firstName(),
                    customer.lastName(),
                    customer.phone(),
                    customer.shippingAddress()
            );
        } catch (ResourceNotFoundException e) {
            throw new UnauthorizedException("Customer profile not found for user");
        }

        AuthResponse userResponse = userMapper.toResponse(user);

        return new AuthResponse(
                userResponse.id(),
                userResponse.username(),
                userResponse.email(),
                null,
                customerInfo
        );
    }
}
