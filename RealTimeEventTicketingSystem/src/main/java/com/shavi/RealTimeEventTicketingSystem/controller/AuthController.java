package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.dto.request.AuthenticationRequest;
import com.shavi.RealTimeEventTicketingSystem.dto.response.AuthenticationResponse;
import com.shavi.RealTimeEventTicketingSystem.service.CustomerService;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Vendor login endpoint
    @PostMapping("/login/vendor")
    public ResponseEntity<AuthenticationResponse> loginVendor(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = vendorService.authenticateVendor(authRequest.getUsername(), authRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new AuthenticationResponse("Vendor login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid vendor credentials"));
        }
    }



    // Customer login endpoint
    @PostMapping("/login/customer")
    public ResponseEntity<AuthenticationResponse> loginCustomer(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = customerService.authenticateCustomer(authRequest.getUsername(), authRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new AuthenticationResponse("Customer login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid customer credentials"));
        }
    }
}
