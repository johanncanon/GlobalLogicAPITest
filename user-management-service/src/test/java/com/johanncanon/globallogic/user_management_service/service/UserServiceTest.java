package com.johanncanon.globallogic.user_management_service.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.johanncanon.globallogic.user_management_service.config.security.JwtUtil;
import com.johanncanon.globallogic.user_management_service.dto.CreateUserRequest;
import com.johanncanon.globallogic.user_management_service.dto.LoginRequest;
import com.johanncanon.globallogic.user_management_service.dto.PhoneRequest;
import com.johanncanon.globallogic.user_management_service.dto.UserResponse;
import com.johanncanon.globallogic.user_management_service.entity.User;
import com.johanncanon.globallogic.user_management_service.exception.custom.InvalidCredentialsException;
import com.johanncanon.globallogic.user_management_service.exception.custom.ResourceNotFoundException;
import com.johanncanon.globallogic.user_management_service.exception.custom.UserAlreadyExistsException;
import com.johanncanon.globallogic.user_management_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private User user;

    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Test User");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password");
        createUserRequest.setPhones(Collections.singletonList(new PhoneRequest("1234567", "1", "57")));

        user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword("encodedPassword");
        user.setToken("jwtToken");
        user.setIsActive(true);
        user.prePersist();
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponse userResponse = userService.createUser(createUserRequest);

        // Assert
        assertNotNull(userResponse);
        assertEquals(user.getName(), userResponse.getName());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertNotNull(userResponse.getId());
    }

    @Test
    void createUser_NameAlreadyExists() {
        when(userRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(createUserRequest);
        });
    }

    @Test
    void createUser_EmailAlreadyExists() {
        when(userRepository.existsByName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(createUserRequest);
        });
    }

    @Test
    void authenticate_Success() {
        var loginRequest = new LoginRequest("Test User", "password");
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");

        var jwtResponse = userService.authenticate(loginRequest);

        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals(user.getName(), jwtResponse.getUser().getName());
    }

    @Test
    void authenticate_UserNotFound() {
        var loginRequest = new LoginRequest("Test User", "password");
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.authenticate(loginRequest);
        });
    }

    @Test
    void authenticate_InvalidPassword() {
        var loginRequest = new LoginRequest("Test User", "wrongpassword");
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.authenticate(loginRequest);
        });
    }

    @Test
    void getUserById_Success() {
        String userId = user.getId().toString();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserById(userId);

        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(UUID.randomUUID().toString());
        });
    }

    @Test
    void getUserByName_Success() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));

        var userResponse = userService.getUserByName(user.getName());

        assertNotNull(userResponse);
        assertEquals(user.getName(), userResponse.getName());
    }

    @Test
    void getUserByName_NotFound() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByName("non-existent-name");
        });
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());
    }

    @Test
    void getAllUsers_Empty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        var users = userService.getAllUsers();

        assertEquals(0, users.size());
    }
}