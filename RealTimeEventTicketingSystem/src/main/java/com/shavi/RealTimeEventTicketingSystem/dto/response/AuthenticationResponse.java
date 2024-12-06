package com.shavi.RealTimeEventTicketingSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponse {
    private String message;
    private String role;

    // Constructor to initialize both message and role
    public AuthenticationResponse(String message, String role) {
        this.message = message;
        this.role = role;
    }

    // Constructor for cases where only message is returned (on failure)
    public AuthenticationResponse(String message) {
        this.message = message;
        this.role = null; // No role if the login failed
    }
}
