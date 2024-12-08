package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.entity.User;
import com.shavi.RealTimeEventTicketingSystem.exception.ResourceNotFoundException;
import com.shavi.RealTimeEventTicketingSystem.exception.ValidationException;
import com.shavi.RealTimeEventTicketingSystem.repository.UserRepository;
import com.shavi.RealTimeEventTicketingSystem.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends LoggerConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    //user Registration
    public User registerUser(UserDto userDto) {
        logger.info("Starting registration process for user with username: {}", userDto.getUsername());

        // Validate the UserDTO
        List<String> validationErrors = ValidationUtil.validateUserDTO(userDto);
        if (!validationErrors.isEmpty()) {
            logger.warn("Validation errors for username {}: {}", userDto.getUsername(), validationErrors);
            throw new ValidationException(validationErrors);
        }

        // Check for existing username, email, and phone number
        if (userRepository.existsByUsername(userDto.getUsername())) {
            logger.warn("Username {} is already registered.", userDto.getUsername());
            throw new RuntimeException("Username is already registered.");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            logger.warn("Email {} is already registered.", userDto.getEmail());
            throw new RuntimeException("Email is already registered.");
        }
        if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            logger.warn("Phone number {} is already registered.", userDto.getPhoneNumber());
            throw new RuntimeException("Phone number is already registered.");
        }

        // Create and save the User
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUserRole(userDto.getRole());

        User savedUser = userRepository.save(user);
        logger.info("Successfully registered user with ID: {}", savedUser.getId());

        return savedUser;

    }

    // Authenticate User's credentials
    public Authentication authenticateUser(String username, String password) {
        logger.info("Attempting to authenticate user with username: {}", username);

        List<User> users = userRepository.findByUsername(username);

        if (users.isEmpty()) {
            logger.error("Invalid login attempt for user with username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Assuming usernames are unique, pick the first user
        User user = users.get(0);

        if(!password.equals(user.getPassword())) {
            logger.error("Invalid login attempt for user with username: {}", username);
            throw new BadCredentialsException("Invalid password");
        }

        // Attach the user's role in the authentication object
        logger.info("User with username '{}' authenticated successfully.", username);
        return new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority(user.getUserRole().name())));

    }

    // Get all user
    public List<User> getAllUser() {
        logger.info("Fetching all users from the database.");
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Integer id) {
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });
    }

    // Update an existing user
    public User updateUser(Integer id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        userRepository.save(existingUser);

        logger.info("Successfully updated user with ID: {}", id);
        return userRepository.save(existingUser);
    }

    // Delete User
    public boolean deleteUser(Integer id) {
        logger.info("Attempting to delete user with ID: {}", id);

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("User with ID: {} deleted successfully.", id);
            return true;
        } else {
            logger.warn("User not found with ID: {}", id);
            return false;
        }
    }
}



