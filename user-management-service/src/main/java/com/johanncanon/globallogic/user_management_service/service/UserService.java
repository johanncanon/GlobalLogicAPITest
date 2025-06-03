package com.johanncanon.globallogic.user_management_service.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johanncanon.globallogic.user_management_service.config.security.JwtUtil;
import com.johanncanon.globallogic.user_management_service.dto.CreateUserRequest;
import com.johanncanon.globallogic.user_management_service.dto.JwtResponse;
import com.johanncanon.globallogic.user_management_service.dto.LoginRequest;
import com.johanncanon.globallogic.user_management_service.dto.UserResponse;
import com.johanncanon.globallogic.user_management_service.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService( UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public UserResponse createUser( CreateUserRequest request ) {
        return null;
    }

    public JwtResponse authenticate( LoginRequest request ) {
        return null;
    }
    
    public UserResponse getByUserId( Long userId ) {
        return null;
    }

    public UserResponse getByUsername( String username ) {
        return null;
    }

    public List<UserResponse> getAllUsers() {
        return null;
    }

    private UserResponse mapToUserResponse( User user ) {
        return null;
    }

}
