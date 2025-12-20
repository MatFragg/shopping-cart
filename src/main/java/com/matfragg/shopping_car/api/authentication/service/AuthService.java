package com.matfragg.shopping_car.api.authentication.service;

import com.matfragg.shopping_car.api.authentication.dto.request.LoginRequest;
import com.matfragg.shopping_car.api.authentication.dto.request.RegisterRequest;
import com.matfragg.shopping_car.api.authentication.dto.response.AuthResponse;

/**
 * Servicio de autenticación y autorización
 * Todos los métodos retornan DTOs para mantener la separación de capas
 */
public interface AuthService {

    /**
     * Registra un nuevo usuario en el sistema
     *
     * @param request Datos del usuario a registrar
     * @return AuthResponse con los datos del usuario creado
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Autentica un usuario y genera un token JWT
     *
     * @param request Credenciales del usuario (email y password)
     * @return AuthResponse con el token JWT y datos del usuario
     */
    AuthResponse login(LoginRequest request);

    /**
     * Obtiene los datos del usuario actual autenticado
     *
     * @return AuthResponse con los datos del usuario
     */
    AuthResponse getCurrentUser();

    /**
     * Verifica la cuenta de un usuario mediante un token
     *
     * @param token Token de verificación enviado por email
     * @return AuthResponse con los datos del usuario verificado
     */
    //AuthResponse verifyAccount(String token);

    /**
     * Refresca el token JWT usando un refresh token válido
     *
     * @param request Contiene el refresh token
     * @return AuthResponse con el nuevo token JWT
     */
    //AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * Envía un email con instrucciones para restablecer la contraseña
     *
     * @param request Contiene el email del usuario
     */
    //void forgotPassword(ForgotPasswordRequest request);

    /**
     * Restablece la contraseña usando un token válido
     *
     * @param request Contiene el token y la nueva contraseña
     */
    //void resetPassword(ResetPasswordRequest request);
}