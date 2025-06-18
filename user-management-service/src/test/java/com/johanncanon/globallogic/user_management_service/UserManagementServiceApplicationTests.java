package com.johanncanon.globallogic.user_management_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = UserManagementServiceApplication.class)
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserManagementServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertThat(port).isGreaterThan(0);
	}

	@Test
	void applicationStartsSuccessfully() {
		// Verifica que la aplicación se inicia sin errores
		assertThat(restTemplate).isNotNull();
	}

	@Test
	void applicationHasCorrectPackage() {
		// Verifica que la clase está en el paquete correcto
		String packageName = UserManagementServiceApplication.class.getPackage().getName();
		assertThat(packageName).isEqualTo("com.johanncanon.globallogic.user_management_service");
	}

	@Test
	void applicationClassIsAnnotatedCorrectly() {
		// Verifica que la clase tiene la anotación @SpringBootApplication
		boolean hasSpringBootAnnotation = UserManagementServiceApplication.class
				.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class);
		assertThat(hasSpringBootAnnotation).isTrue();
	}
}
