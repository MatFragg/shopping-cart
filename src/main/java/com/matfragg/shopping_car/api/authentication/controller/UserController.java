package com.matfragg.shopping_car.api.authentication.controller;

import com.matfragg.shopping_car.api.authentication.dto.response.UserResponse;
import com.matfragg.shopping_car.api.authentication.service.UserService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name="Users", description="Endpoints for user management")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve user details by their ID.")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", userResponse));
    }
}
