package com.johanncanon.globallogic.user_management_service.config.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "thisIsASecureAndLongEnoughSecretKeyForTestingWithJJWT";
    private final int testExpiration = 86400000; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Set private fields using reflection for testing
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", testExpiration);
    }

    @Test
    void shouldGenerateValidToken() {
        // Given
        String email = "testuser@test.com";

        // When
        String token = jwtUtil.generateToken(email);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void shouldExtractNameFromToken() {
        // Given
        String email = "testuser@test.com";
        String token = jwtUtil.generateToken(email);

        // When
        String extractedUsername = jwtUtil.getEmailFromToken(token);

        // Then
        assertEquals(email, extractedUsername);
    }

    @Test
    void shouldValidateValidToken() {
        // Given
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void shouldRejectInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void shouldRejectNullToken() {
        // When
        boolean isValid = jwtUtil.validateToken(null);

        // Then
        assertFalse(isValid);
    }

    @Test
    void shouldRejectEmptyToken() {
        // When
        boolean isValid = jwtUtil.validateToken("");

        // Then
        assertFalse(isValid);
    }

}
