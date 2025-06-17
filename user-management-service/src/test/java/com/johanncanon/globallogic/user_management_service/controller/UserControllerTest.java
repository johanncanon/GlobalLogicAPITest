package com.johanncanon.globallogic.user_management_service.controller;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johanncanon.globallogic.user_management_service.config.security.JwtUtil;
import com.johanncanon.globallogic.user_management_service.dto.CreateUserRequest;
import com.johanncanon.globallogic.user_management_service.dto.JwtResponse;
import com.johanncanon.globallogic.user_management_service.dto.LoginRequest;
import com.johanncanon.globallogic.user_management_service.dto.UserResponse;
import com.johanncanon.globallogic.user_management_service.service.AuthService;
import com.johanncanon.globallogic.user_management_service.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("johan canon");
        request.setEmail("johann.canon@globallogic.com");
        request.setPassword("aB12defghi");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID().toString());
        userResponse.setName(request.getName());
        userResponse.setEmail(request.getEmail());

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("johan canon"));
    }

    @Test
    void shouldLoginUser() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("johann.canon@globallogic.com");
        request.setPassword("aB12defghi");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID().toString());
        userResponse.setName("johan canon");
        userResponse.setEmail(request.getUsername());

        String fakeToken = "fake-jwt-token";

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(fakeToken);
        jwtResponse.setUser(userResponse);

        when(userService.authenticate()).thenReturn(jwtResponse);
        when(jwtUtil.generateToken(userResponse.getEmail())).thenReturn(fakeToken);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(fakeToken))
                .andExpect(jsonPath("$.user.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.user.name").value(userResponse.getName()));
    }

    @Test
    @WithMockUser
    void shouldGetAllUsers() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID().toString());
        userResponse.setName("Test User");
        userResponse.setEmail("test@test.com");

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userResponse));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    @WithMockUser
    void shouldGetUserById() throws Exception {
        String userId = UUID.randomUUID().toString();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setName("Test User");
        userResponse.setEmail("test@test.com");

        when(userService.getUserById(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @WithMockUser
    void shouldGetUserByName() throws Exception {
        String userName = "Test User";
        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID().toString());
        userResponse.setName(userName);
        userResponse.setEmail("test@test.com");

        when(userService.getUserByName(userName)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/name/" + userName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userName));
    }
}