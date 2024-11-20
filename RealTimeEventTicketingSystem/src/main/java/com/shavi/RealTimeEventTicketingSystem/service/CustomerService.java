package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
import com.shavi.RealTimeEventTicketingSystem.entity.Customer;
import com.shavi.RealTimeEventTicketingSystem.entity.Vendor;
import com.shavi.RealTimeEventTicketingSystem.enums.UserRole;
import com.shavi.RealTimeEventTicketingSystem.exception.ResourceNotFoundException;
import com.shavi.RealTimeEventTicketingSystem.exception.ValidationException;
import com.shavi.RealTimeEventTicketingSystem.repository.CustomerRepository;
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

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
    @Autowired
    private AuthenticationManager authenticationManager;

    //customer Registration
    public Customer registerCustomer(UserDto userDto) {
        // Validate the UserDTO
        List<String> validationErrors = ValidationUtil.validateUserDTO(userDto);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        // Check for existing username, email, and phone number
        if (customerRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username is already registered.");
        }
        if (customerRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }
        if (customerRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already registered.");
        }

        // Create and save the Customer
        Customer customer = new Customer();
        customer.setUsername(userDto.getUsername());
        customer.setPassword(userDto.getPassword());
        customer.setEmail(userDto.getEmail());
        customer.setPhoneNumber(userDto.getPhoneNumber());
        customer.setRole(UserRole.CUSTOMER);

        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Successfully registered customer with ID: {}", savedCustomer.getId());

        return savedCustomer;
    }

    // Authenticate customer's credentials
    public Authentication authenticateCustomer(String username, String password) {
        List<Customer> customers = customerRepository.findByUsername(username);

        if (customers.isEmpty()) {
            logger.error("Invalid login attempt for vendor with username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Assuming usernames are unique, pick the first vendor
        Customer customer = customers.get(0);

        if(!password.equals(customer.getPassword())) {
            logger.error("Invalid login attempt for vendor with username: {}", username);
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken
                (customer.getUsername(), null);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get customers by ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    // Update an existing customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        existingCustomer.setUsername(updatedCustomer.getUsername());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPassword(updatedCustomer.getPassword());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        customerRepository.save(updatedCustomer);
        return customerRepository.save(updatedCustomer);

    }

    // Delete Vendor
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

