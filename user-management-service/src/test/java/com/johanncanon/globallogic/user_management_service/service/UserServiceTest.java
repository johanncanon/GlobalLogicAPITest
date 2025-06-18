package com.johanncanon.globallogic.user_management_service.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.johanncanon.globallogic.user_management_service.config.security.JwtUtil;
import com.johanncanon.globallogic.user_management_service.dto.CreateUserRequest;
import com.johanncanon.globallogic.user_management_service.dto.PhoneRequest;
import com.johanncanon.globallogic.user_management_service.dto.UserResponse;
import com.johanncanon.globallogic.user_management_service.entity.Phone;
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
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private User user;
    private Phone phone;

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

        // Crear y agregar teléfono al usuario para cubrir mapPhoneToPhoneRequest
        phone = new Phone("1234567", "1", "57");
        user.addPhone(phone);

        user.prePersist();
    }

    @Test
    void createUser_Success() {
        // Arrange
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
        // Verificar que los teléfonos se mapearon correctamente
        assertNotNull(userResponse.getPhones());
        assertEquals(1, userResponse.getPhones().size());
        assertEquals(phone.getNumber(), userResponse.getPhones().get(0).getNumber());
        assertEquals(phone.getCityCode(), userResponse.getPhones().get(0).getCityCode());
        assertEquals(phone.getCountryCode(), userResponse.getPhones().get(0).getCountryCode());
    }

    @Test
    void createUser_EmailAlreadyExists() {

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(createUserRequest);
        });
    }

    @Test
    void authenticate_Success() {

        doReturn(Optional.of(user)).when(authService).getCurrentUserBySecurityContext();
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");

        var jwtResponse = userService.authenticate();

        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals(user.getName(), jwtResponse.getUser().getName());
        // Verificar que los teléfonos se mapearon correctamente
        assertNotNull(jwtResponse.getUser().getPhones());
        assertEquals(1, jwtResponse.getUser().getPhones().size());
        assertEquals(phone.getNumber(), jwtResponse.getUser().getPhones().get(0).getNumber());
    }

    @Test
    void authenticate_UserNotFound() {
        doReturn(Optional.empty()).when(authService).getCurrentUserBySecurityContext();

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.authenticate();
        });
    }

    @Test
    void authenticate_InvalidPassword() {

        doReturn(Optional.of(user)).when(authService).getCurrentUserBySecurityContext();
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");

        var jwtResponse = userService.authenticate();

        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals(user.getName(), jwtResponse.getUser().getName());
    }

    @Test
    void getUserById_Success() {
        String userId = user.getId().toString();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserById(userId);

        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getId());
        // Verificar que los teléfonos se mapearon correctamente
        assertNotNull(userResponse.getPhones());
        assertEquals(1, userResponse.getPhones().size());
        assertEquals(phone.getNumber(), userResponse.getPhones().get(0).getNumber());
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
        // Verificar que los teléfonos se mapearon correctamente
        assertNotNull(userResponse.getPhones());
        assertEquals(1, userResponse.getPhones().size());
        assertEquals(phone.getNumber(), userResponse.getPhones().get(0).getNumber());
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
        // Verificar que los teléfonos se mapearon correctamente
        assertNotNull(users.get(0).getPhones());
        assertEquals(1, users.get(0).getPhones().size());
        assertEquals(phone.getNumber(), users.get(0).getPhones().get(0).getNumber());
    }

    @Test
    void getAllUsers_Empty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        var users = userService.getAllUsers();

        assertEquals(0, users.size());
    }

    @Test
    void mapToUserResponse_WithMultiplePhones() {
        User userWithMultiplePhones = new User();
        userWithMultiplePhones.setName("Test User");
        userWithMultiplePhones.setEmail("test@example.com");
        userWithMultiplePhones.setPassword("encodedPassword");
        userWithMultiplePhones.setToken("jwtToken");
        userWithMultiplePhones.setIsActive(true);

        Phone phone1 = new Phone("1234567", "1", "57");
        Phone phone2 = new Phone("9876543", "2", "57");
        Phone phone3 = new Phone("5555555", "3", "57");

        userWithMultiplePhones.addPhone(phone1);
        userWithMultiplePhones.addPhone(phone2);
        userWithMultiplePhones.addPhone(phone3);

        userWithMultiplePhones.prePersist();

        when(userRepository.findById(userWithMultiplePhones.getId())).thenReturn(Optional.of(userWithMultiplePhones));

        UserResponse userResponse = userService.getUserById(userWithMultiplePhones.getId().toString());

        assertNotNull(userResponse);
        assertEquals(userWithMultiplePhones.getName(), userResponse.getName());
        assertNotNull(userResponse.getPhones());
        assertEquals(3, userResponse.getPhones().size());
        assertEquals(phone1.getNumber(), userResponse.getPhones().get(0).getNumber());
        assertEquals(phone1.getCityCode(), userResponse.getPhones().get(0).getCityCode());
        assertEquals(phone1.getCountryCode(), userResponse.getPhones().get(0).getCountryCode());
        assertEquals(phone2.getNumber(), userResponse.getPhones().get(1).getNumber());
        assertEquals(phone2.getCityCode(), userResponse.getPhones().get(1).getCityCode());
        assertEquals(phone2.getCountryCode(), userResponse.getPhones().get(1).getCountryCode());
        assertEquals(phone3.getNumber(), userResponse.getPhones().get(2).getNumber());
        assertEquals(phone3.getCityCode(), userResponse.getPhones().get(2).getCityCode());
        assertEquals(phone3.getCountryCode(), userResponse.getPhones().get(2).getCountryCode());
    }

    @Test
    void mapToUserResponse_WithNullPhones() {
        User userWithoutPhones = new User();
        userWithoutPhones.setName("Test User");
        userWithoutPhones.setEmail("test@example.com");
        userWithoutPhones.setPassword("encodedPassword");
        userWithoutPhones.setToken("jwtToken");
        userWithoutPhones.setIsActive(true);
        userWithoutPhones.setPhones(null); // Establecer phones como null

        userWithoutPhones.prePersist();

        when(userRepository.findById(userWithoutPhones.getId())).thenReturn(Optional.of(userWithoutPhones));

        UserResponse userResponse = userService.getUserById(userWithoutPhones.getId().toString());

        assertNotNull(userResponse);
        assertEquals(userWithoutPhones.getName(), userResponse.getName());
        assertNull(userResponse.getPhones());
    }
}