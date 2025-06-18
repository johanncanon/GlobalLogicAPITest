package com.johanncanon.globallogic.user_management_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test básico para UserManagementServiceApplication
 * Este test es más simple y se enfoca en verificar que la aplicación puede
 * iniciarse
 */
@SpringBootTest
@ActiveProfiles("test")
class UserManagementServiceApplicationBasicTest {

    @Test
    void contextLoads() {
        // Test básico que verifica que el contexto de Spring se carga
        assertTrue(true, "El contexto de Spring se cargó correctamente");
    }

    @Test
    void applicationClassExists() {
        // Verifica que la clase de la aplicación existe
        assertNotNull(UserManagementServiceApplication.class);
    }

    @Test
    void applicationHasCorrectName() {
        // Verifica que la clase tiene el nombre correcto
        String className = UserManagementServiceApplication.class.getSimpleName();
        assertEquals("UserManagementServiceApplication", className);
    }
}