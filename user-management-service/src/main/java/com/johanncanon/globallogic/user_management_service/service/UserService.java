package com.johanncanon.globallogic.user_management_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johanncanon.globallogic.user_management_service.config.security.JwtUtil;
import com.johanncanon.globallogic.user_management_service.dto.CreateUserRequest;
import com.johanncanon.globallogic.user_management_service.dto.JwtResponse;
import com.johanncanon.globallogic.user_management_service.dto.LoginRequest;
import com.johanncanon.globallogic.user_management_service.dto.PhoneRequest;
import com.johanncanon.globallogic.user_management_service.dto.UserResponse;
import com.johanncanon.globallogic.user_management_service.entity.Phone;
import com.johanncanon.globallogic.user_management_service.entity.User;
import com.johanncanon.globallogic.user_management_service.exception.custom.InvalidCredentialsException;
import com.johanncanon.globallogic.user_management_service.exception.custom.ResourceNotFoundException;
import com.johanncanon.globallogic.user_management_service.exception.custom.UserAlreadyExistsException;
import com.johanncanon.globallogic.user_management_service.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse createUser(CreateUserRequest request) {

        // Validate if username already exist
        if (userRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("Campo Name ya existe, intente otro nombre");
        }

        // validate if Email already exist
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email ya existe, intente otro email");
        }

        // Create user entity
        var userEntity = new User();
        userEntity.setName(request.getName());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setEmail(request.getEmail());

        userEntity.setToken(jwtUtil.generateToken(request.getName()));

        for (Phone phone : getPhonesFromRequest(request)) {
            userEntity.addPhone(phone);
        }

        var userSaved = userRepository.save(userEntity);

        return mapToUserResponse(userSaved);
    }

    public JwtResponse authenticate(LoginRequest request) {

        Optional<User> userOpt = userRepository.findByName(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new InvalidCredentialsException("Nombre o contraseña incorrectos");
        }
        var user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Constraseña incorrecta");
        }

        var jwtToken = jwtUtil.generateToken(user.getName());
        var userResponse = mapToUserResponse(user);

        return new JwtResponse(jwtToken, userResponse);
    }

    public UserResponse getUserById(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    public UserResponse getUserByName(String name) {

        var user = userRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getFormattedCreated(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsActive(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhones() != null ? mapPhoneToPhoneRequest(user.getPhones()) : null);
    }

    private List<PhoneRequest> mapPhoneToPhoneRequest(List<Phone> phones) {
        return phones.stream()
                .map(phone -> new PhoneRequest(
                        phone.getNumber(),
                        phone.getCityCode(),
                        phone.getCountryCode()))
                .collect(Collectors.toList());
    }

    private List<Phone> getPhonesFromRequest(CreateUserRequest request) {
        return request.getPhones().stream()
                .map(phoneRequest -> new Phone(
                        phoneRequest.getNumber(),
                        phoneRequest.getCityCode(),
                        phoneRequest.getCountryCode()))
                .collect(Collectors.toList());
    }

}
