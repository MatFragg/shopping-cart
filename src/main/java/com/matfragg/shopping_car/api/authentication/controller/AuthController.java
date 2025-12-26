package com.matfragg.shopping_car.api.authentication.controller;

import com.matfragg.shopping_car.api.authentication.dto.request.LoginRequest;
import com.matfragg.shopping_car.api.authentication.dto.request.RegisterRequest;
import com.matfragg.shopping_car.api.authentication.dto.response.AuthResponse;
import com.matfragg.shopping_car.api.authentication.service.AuthService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User registration endpoint
     * @param request RegisterRequest
     * @return ApiResponse with AuthResponse
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar a new User", description = "Creates a new user account.")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {

        AuthResponse userResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario registrado exitosamente.", userResponse));
    }

    /**
     * Authenticate user and generate JWT token endpoint
     *
     * @param request Credentials for login
     * @return ApiResponse with AuthResponse containing JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Authenticates a user and generates a JWT token.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("User Logged in successfully", authResponse));
    }

    /**
     * Get the currently authenticated user
     *
     * @return ApiResponse with AuthResponse of the current user
     */
    @GetMapping("/me")
    @Operation(summary = "Get Current User", description = "Retrieves the currently authenticated user.")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser() {
        AuthResponse userResponse = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("Current user retrieved successfully", userResponse));
    }
}
