package com.matfragg.shopping_car.api.authentication.mapper;

import com.matfragg.shopping_car.api.authentication.dto.request.RegisterRequest;
import com.matfragg.shopping_car.api.authentication.dto.response.AuthResponse;
import com.matfragg.shopping_car.api.authentication.model.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AuthResponse toResponse(User user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                null
        );
    }

    public User toEntity(RegisterRequest registerRequest) {
        return new User(
                registerRequest.username(),
                registerRequest.password()
        );
    }
}
