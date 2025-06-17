package com.johanncanon.globallogic.user_management_service.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.johanncanon.globallogic.user_management_service.entity.User;
import com.johanncanon.globallogic.user_management_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("encodedPassword");
        testUser.setToken("jwtToken");
        testUser.setIsActive(true);
        testUser.prePersist();
    }

    @Test
    void getCurrentUserEmail_Success() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        SecurityContextHolder.setContext(securityContext);

        // Act
        String result = authService.getCurrentUserEmail();

        // Assert
        assertNotNull(result);
        assertEquals(TEST_EMAIL, result);
    }

    @Test
    void getCurrentUserEmail_WithEmptyAuthenticationName() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("");
        SecurityContextHolder.setContext(securityContext);

        // Act
        String result = authService.getCurrentUserEmail();

        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void getCurrentUserBySecurityContext_UserFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = authService.getCurrentUserBySecurityContext();

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        assertEquals(testUser.getName(), result.get().getName());
        assertEquals(testUser.getId(), result.get().getId());
    }

    @Test
    void getCurrentUserBySecurityContext_UserNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("nonexistent@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = authService.getCurrentUserBySecurityContext();

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    void getCurrentUserBySecurityContext_WithEmptyEmail() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = authService.getCurrentUserBySecurityContext();

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    void getCurrentUserBySecurityContext_IntegrationTest() {
        // Arrange
        TestingAuthenticationToken authToken = new TestingAuthenticationToken(TEST_EMAIL, null);
        authToken.setAuthenticated(true);

        when(securityContext.getAuthentication()).thenReturn(authToken);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = authService.getCurrentUserBySecurityContext();

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        assertEquals(testUser.getName(), result.get().getName());
    }
}