package com.programming.user_service.service;

import com.programming.user_service.controller.handlers.LoginFailed;
import com.programming.user_service.dto.LoginRequest;
import com.programming.user_service.dto.LoginResponse;
import com.programming.user_service.dto.UserResponse;
import com.programming.user_service.dto.UserRequest;
import com.programming.user_service.model.User;
import com.programming.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public void createUser(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .role(userRequest.getRole())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        userRepository.save(user);
        log.info("User created: {}", user);
    }


    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public void updateUser(UUID id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if(userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }

        if(userRequest.getRole() != null) {
            user.setRole(userRequest.getRole());
        }

        if(userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        if(userRequest.getUsername() != null) {
            user.setUsername(userRequest.getUsername());
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    public User login(LoginRequest loginRequest) {
//        userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword()).orElseThrow(() -> new RuntimeException("User not found"));
       Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
       if(user.isPresent()) {
           String password = loginRequest.getPassword();
           String encodedPassword = user.get().getPassword();
           if(passwordEncoder.matches(password, encodedPassword)) {
               userRepository.findByUsernameAndPassword(loginRequest.getUsername(),encodedPassword).orElseThrow(() -> new LoginFailed("Login failed"));
               return userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new LoginFailed("Login failed"));
           }
           else {
               throw new LoginFailed("Wrong username or password");
           }
       }
       else {
           throw new LoginFailed("User not found");
       }

    }
}
