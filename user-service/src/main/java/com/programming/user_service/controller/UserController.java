package com.programming.user_service.controller;

import com.programming.user_service.dto.LoginRequest;
import com.programming.user_service.dto.LoginResponse;
import com.programming.user_service.dto.UserResponse;
import com.programming.user_service.dto.UserRequest;
import com.programming.user_service.model.User;
import com.programming.user_service.repository.UserRepository;
import com.programming.user_service.service.JwtService;
import com.programming.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplateBean;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private void createUser(@RequestBody UserRequest userRequest){
        userService.createUser(userRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private UserResponse getUserById(@PathVariable UUID id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void updateUser(@PathVariable UUID id, @RequestBody UserRequest userRequest){
        userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteUser(@PathVariable UUID id, @RequestHeader("Authorization") String authorizationHeader){
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        // Add the token to the Authorization header for the request to deviceapp
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Create an HttpEntity with the headers
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Call the device service to delete devices associated with the user
        String deleteDevicesUrl = "http://deviceapp:8081/api/devices/user/" + id;
        restTemplateBean.exchange(deleteDevicesUrl, HttpMethod.DELETE, entity, Void.class);

        // Delete the user from user service
        userService.deleteUser(id);
    }


    @PostMapping("/login")
    private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest);
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(user.getId(),user.getRole(),jwtToken, jwtService.getExpirationTime()));

    }

}
