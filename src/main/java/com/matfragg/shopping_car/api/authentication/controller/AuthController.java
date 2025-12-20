package com.matfragg.shopping_car.api.authentication.controller;

import com.matfragg.shopping_car.api.authentication.dto.request.LoginRequest;
import com.matfragg.shopping_car.api.authentication.dto.request.RegisterRequest;
import com.matfragg.shopping_car.api.authentication.dto.response.AuthResponse;
import com.matfragg.shopping_car.api.authentication.dto.response.UserResponse;
import com.matfragg.shopping_car.api.authentication.service.AuthService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param httpRequest HttpServletRequest
     * @return
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
     * Autentica un usuario y genera un token JWT
     *
     * @param request Credenciales del usuario (email y password)
     * @return ApiResponse con el token JWT y datos del usuario
     */
    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Authenticates a user and generates a JWT token.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("User Logged in succesfully", authResponse));
    }

}
