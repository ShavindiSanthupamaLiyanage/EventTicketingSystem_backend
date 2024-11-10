package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.entity.Vendor;
import com.shavi.RealTimeEventTicketingSystem.enums.UserRole;
import com.shavi.RealTimeEventTicketingSystem.exception.ResourceNotFoundException;
import com.shavi.RealTimeEventTicketingSystem.exception.ValidationException;
import com.shavi.RealTimeEventTicketingSystem.repository.VendorRepository;
import com.shavi.RealTimeEventTicketingSystem.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    //vendor Registration
    public Vendor registerVendor(UserDto userDto) {
        logger.info("Starting registration process for vendor with username: {}", userDto.getUsername());

        // Validate the UserDTO
        List<String> validationErrors = ValidationUtil.validateUserDTO(userDto);
        if (!validationErrors.isEmpty()) {
            logger.warn("Validation errors for username {}: {}", userDto.getUsername(), validationErrors);
            throw new ValidationException(validationErrors);
        }

        // Check for existing username, email, and phone number
        if (vendorRepository.existsByUsername(userDto.getUsername())) {
            logger.warn("Username {} is already registered.", userDto.getUsername());
            throw new RuntimeException("Username is already registered.");
        }
        if (vendorRepository.existsByEmail(userDto.getEmail())) {
            logger.warn("Email {} is already registered.", userDto.getEmail());
            throw new RuntimeException("Email is already registered.");
        }
        if (vendorRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            logger.warn("Phone number {} is already registered.", userDto.getPhoneNumber());
            throw new RuntimeException("Phone number is already registered.");
        }

        // Create and save the Vendor
        Vendor vendor = new Vendor();
        vendor.setUsername(userDto.getUsername());
        vendor.setPassword(userDto.getPassword());
        vendor.setEmail(userDto.getEmail());
        vendor.setPhoneNumber(userDto.getPhoneNumber());
        vendor.setRole(UserRole.VENDOR);

        Vendor savedVendor = vendorRepository.save(vendor);
        logger.info("Successfully registered vendor with ID: {}", savedVendor.getId());

        return savedVendor;

    }

    // Authenticate vendor's credentials
    public Authentication authenticateVendor(String username, String password) {
        List<Vendor> vendors = vendorRepository.findByUsername(username);

        if (vendors.isEmpty()) {
            logger.error("Invalid login attempt for vendor with username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Assuming usernames are unique, pick the first vendor
        Vendor vendor = vendors.get(0);

        if(!password.equals(vendor.getPassword())) {
            logger.error("Invalid login attempt for vendor with username: {}", username);
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken
                (vendor.getUsername(), null);

    }

    // Get all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // Get vendor by ID
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + id));
    }

    // Update an existing vendor
    public Vendor updateVendor(Long id, Vendor updatedVendor) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + id));
            existingVendor.setUsername(updatedVendor.getUsername());
            existingVendor.setEmail(updatedVendor.getEmail());
            existingVendor.setPassword(updatedVendor.getPassword());
            existingVendor.setPhoneNumber(updatedVendor.getPhoneNumber());
            vendorRepository.save(existingVendor);
            return vendorRepository.save(existingVendor);

    }


}