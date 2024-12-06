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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("user/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);

        // Build a JSON response with a message and the user ID
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("userId", registeredUser.getId());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/user/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authRequest) {
        try {
            // Authenticate the user using the authentication service
            Authentication authentication = userService.authenticateUser(authRequest.getUsername(), authRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal(); // Cast to User
            String role = user.getUserRole().name();

            // Retrieve the user's role (assuming it's an Enum)
//            String role = ((User) authentication.getPrincipal()).getUserRole().name();

            // Create the response with both message and role
            AuthenticationResponse response = new AuthenticationResponse("User login successful", role);
            return ResponseEntity.ok(response); // Return success response with message and role
        } catch (Exception e) {
            // In case of authentication failure, return an error message
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid User credentials", null));
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

