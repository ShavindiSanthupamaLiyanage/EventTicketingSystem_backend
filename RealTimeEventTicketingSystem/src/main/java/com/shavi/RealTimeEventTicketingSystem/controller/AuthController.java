package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.dto.request.AuthenticationRequest;
import com.shavi.RealTimeEventTicketingSystem.dto.response.AuthenticationResponse;
import com.shavi.RealTimeEventTicketingSystem.entity.User;
import com.shavi.RealTimeEventTicketingSystem.entity.Vendor;
import com.shavi.RealTimeEventTicketingSystem.service.CustomerService;
import com.shavi.RealTimeEventTicketingSystem.service.UserService;
import com.shavi.RealTimeEventTicketingSystem.service.VendorService;
import com.shavi.RealTimeEventTicketingSystem.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private VendorService vendorService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

//    // Vendor login endpoint
//    @PostMapping("/login/vendor")
//    public ResponseEntity<AuthenticationResponse> loginVendor(@RequestBody AuthenticationRequest authRequest) {
//        try {
//            Authentication authentication = vendorService.authenticateVendor(authRequest.getUsername(), authRequest.getPassword());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return ResponseEntity.ok(new AuthenticationResponse("Vendor login successful"));
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid vendor credentials"));
//        }
//    }
//
//
//
//    // Customer login endpoint
//    @PostMapping("/login/customer")
//    public ResponseEntity<AuthenticationResponse> loginCustomer(@RequestBody AuthenticationRequest authRequest) {
//        try {
//            Authentication authentication = customerService.authenticateCustomer(authRequest.getUsername(), authRequest.getPassword());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return ResponseEntity.ok(new AuthenticationResponse("Customer login successful"));
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid customer credentials"));
//        }
//    }

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

