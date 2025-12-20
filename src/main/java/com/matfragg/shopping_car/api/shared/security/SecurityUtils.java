package com.matfragg.shopping_car.api.shared.security;

import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.authentication.repository.UserRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Optional<String> getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                });
    }

    public Optional<User> getCurrentUser() {
        return getCurrentUsername()
                .flatMap(userRepository::findByUsername);
    }

    public static boolean hasRole(String role) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}