//package com.shavi.RealTimeEventTicketingSystem.controller;
//
//import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
//import com.shavi.RealTimeEventTicketingSystem.entity.Customer;
//import com.shavi.RealTimeEventTicketingSystem.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/customers")
//public class CustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerCustomer(@RequestBody UserDto userDto) {
//        Customer registeredCustomer = customerService.registerCustomer(userDto);
//        return ResponseEntity.ok("Customer registered successfully with ID: " + registeredCustomer.getId());
//    }
//
//    // Get all customers
//    @GetMapping("/customers")
//    public List<Customer> getAllCustomers() {
//        return customerService.getAllCustomers();
//    }
//
//    // Get vendor by ID
//    @GetMapping("/customer/{id}")
//    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
//        Customer customer = customerService.getCustomerById(id); // This will throw ResourceNotFoundException if vendor not found
//        return ResponseEntity.ok(customer);
//    }
//
//    // Update an existing vendor
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
//        Customer customer = customerService.updateCustomer(id, updatedCustomer); // This will throw ResourceNotFoundException if vendor not found
//        return ResponseEntity.ok(customer);
//    }
//
//    // Delete event
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
//        boolean deleted = customerService.deleteCustomer(id);
//        return deleted
//                ? ResponseEntity.ok("Customer deleted successfully.")
//                : ResponseEntity.status(404).body("Customer not found.");
//    }
//}
