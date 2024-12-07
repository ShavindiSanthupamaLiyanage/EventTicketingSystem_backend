package com.shavi.RealTimeEventTicketingSystem.dto;

import com.shavi.RealTimeEventTicketingSystem.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
