package com.shavi.RealTimeEventTicketingSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponse {
    private String message;

    public AuthenticationResponse(String message) {
        this.message = message;
    }
}