package com.johanncanon.globallogic.user_management_service.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.johanncanon.globallogic.user_management_service.entity.User;
import com.johanncanon.globallogic.user_management_service.repository.UserRepository;
import com.johanncanon.globallogic.user_management_service.exception.custom.UnauthorizedException;

@Service
public class AuthService {

    public final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public String getCurrentUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UnauthorizedException("No se ha encontrado autenticación en el contexto de seguridad");
        }

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("El usuario no está autenticado.");
        }

        var email = authentication.getName();
        if (email == null || email.isEmpty() || "anonymousUser".equals(email)) {
            throw new UnauthorizedException("Token de usuario no válido o faltante.");
        }

        return email;
    }

    public Optional<User> getCurrentUserBySecurityContext() {
        try {
            var emailByCurrentUser = getCurrentUserEmail();
            return repository.findByEmail(emailByCurrentUser);
        } catch (Exception e) {
            throw new UnauthorizedException("Error de usuario: " + e.getMessage());
        }
    }

}
