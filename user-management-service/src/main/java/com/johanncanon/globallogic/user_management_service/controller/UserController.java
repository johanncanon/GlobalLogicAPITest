package com.johanncanon.globallogic.user_management_service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johanncanon.globallogic.user_management_service.dto.CreateUserRequest;
import com.johanncanon.globallogic.user_management_service.dto.UserResponse;
import com.johanncanon.globallogic.user_management_service.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        var user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser() {
        var response = userService.authenticate();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/name/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name) {
        var user = userService.getUserByName(name);
        return ResponseEntity.ok(user);
    }

}
