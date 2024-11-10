//package com.shavi.RealTimeEventTicketingSystem.dto.response;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
////@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class AuthenticationResponse {
//    private String message;
//    private String token;
//
//    public AuthenticationResponse(String message, String token) {
//        this.message = message;
//        this.token = token;
//    }
//
//
//}


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