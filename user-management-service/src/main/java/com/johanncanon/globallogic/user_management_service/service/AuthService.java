package com.johanncanon.globallogic.user_management_service.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.johanncanon.globallogic.user_management_service.entity.User;
import com.johanncanon.globallogic.user_management_service.repository.UserRepository;

@Service
public class AuthService {

    public final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Optional<User> getCurrentUserBySecurityContext() {
        var emailByCurrentUser = getCurrentUserEmail();
        return repository.findByEmail(emailByCurrentUser);
    }

}
