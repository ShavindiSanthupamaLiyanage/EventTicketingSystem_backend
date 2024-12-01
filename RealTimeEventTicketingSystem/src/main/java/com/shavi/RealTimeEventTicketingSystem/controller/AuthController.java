package com.shavi.RealTimeEventTicketingSystem.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("user/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);
        return ResponseEntity.ok("User registered successfully with ID: " + registeredUser.getId());
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = userService.authenticateUser(authRequest.getUsername(), authRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new AuthenticationResponse("User login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid User credentials"));
        }
    }

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update an existing User
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Integer id) {
        boolean deleted = userService.deleteUser(id);
        return deleted
                ? ResponseEntity.ok("User deleted successfully.")
                : ResponseEntity.status(404).body("User not found.");
    }
}

