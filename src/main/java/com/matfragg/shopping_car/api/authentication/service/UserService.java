package com.matfragg.shopping_car.api.authentication.service;

import com.matfragg.shopping_car.api.authentication.dto.response.UserResponse;

public interface UserService {
    UserResponse findById(Long id);
    boolean existsById(Long id);
}