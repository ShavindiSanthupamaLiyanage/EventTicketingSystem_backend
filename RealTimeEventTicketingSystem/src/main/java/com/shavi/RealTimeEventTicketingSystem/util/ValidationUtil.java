package com.shavi.RealTimeEventTicketingSystem.util;

import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    /**
     * Validates the UserDTO fields.
     *
     * @param userDTO The user data transfer object to validate.
     * @return A list of validation error messages. Empty if no errors.
     */
    public static List<String> validateUserDTO(UserDto userDTO) {
        List<String> errors = new ArrayList<>();

        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            errors.add("Username cannot be empty.");
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            errors.add("Password cannot be empty.");
        }

        if (userDTO.getEmail() == null || !EMAIL_PATTERN.matcher(userDTO.getEmail()).matches()) {
            errors.add("Email is invalid or empty.");
        }

        if (userDTO.getPhoneNumber() == null || !PHONE_PATTERN.matcher(userDTO.getPhoneNumber()).matches()) {
            errors.add("Phone number must be exactly 10 digits.");
        }

        return errors;
    }
}

