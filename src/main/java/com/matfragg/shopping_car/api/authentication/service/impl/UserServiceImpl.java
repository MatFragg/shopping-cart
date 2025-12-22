package com.matfragg.shopping_car.api.authentication.service.impl;

import com.matfragg.shopping_car.api.authentication.dto.response.UserResponse;
import com.matfragg.shopping_car.api.authentication.repository.UserRepository;
import com.matfragg.shopping_car.api.authentication.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername()
                ))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}