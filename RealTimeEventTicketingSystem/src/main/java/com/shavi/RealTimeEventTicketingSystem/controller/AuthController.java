package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.dto.request.AuthenticationRequest;
import com.shavi.RealTimeEventTicketingSystem.dto.response.AuthenticationResponse;
import com.shavi.RealTimeEventTicketingSystem.entity.User;
import com.shavi.RealTimeEventTicketingSystem.service.UserService;
import com.shavi.RealTimeEventTicketingSystem.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController extends LoggerConfiguration {

    @Autowired
    private UserService userService;

    @PostMapping("user/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDto userDto) {
        try {
            User registeredUser = userService.registerUser(userDto);

            // Build a JSON response with a message and the user ID
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", registeredUser.getId());

            // Log user registration
            logger.info("User registered successfully with ID: {}", registeredUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(500).body(Map.of("message", "User registration failed"));
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authRequest) {
        try {
            // Authenticate the user using the authentication service
            Authentication authentication = userService.authenticateUser(authRequest.getUsername(), authRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal(); // Cast to User
            String role = user.getUserRole().name();
            Integer userId = user.getId();

            // Create the response with both message and role
            AuthenticationResponse response = new AuthenticationResponse("User login successful", role, userId);
            logger.info("User with username '{}' logged in successfully.", authRequest.getUsername());
            return ResponseEntity.ok(response); // Return success response with message and role
        } catch (Exception e) {
            // In case of authentication failure, return an error message
            logger.warn("Failed login attempt for username '{}'", authRequest.getUsername());
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid User credentials", null, null));
        }
    }

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        logger.info("Fetching all users.");
        return userService.getAllUser();
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        logger.info("Fetching user with ID: {}", id);
        return ResponseEntity.ok(user);
    }

    // Update an existing User
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        logger.info("User with ID: {} updated successfully.", id);
        return ResponseEntity.ok(user);
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Integer id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            logger.info("User with ID: {} deleted successfully.", id);
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            logger.warn("User with ID: {} not found for deletion.", id);
            return ResponseEntity.status(404).body("User not found.");
        }
    }
}

